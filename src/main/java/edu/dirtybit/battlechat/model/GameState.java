package edu.dirtybit.battlechat.model;

import edu.dirtybit.battlechat.BattleShipConfiguration;
import edu.dirtybit.battlechat.exceptions.*;
import edu.dirtybit.battlechat.model.BattleChatStatus.Phase;
import edu.dirtybit.battlechat.BattleShipConfiguration.ConfigKeys;
import edu.dirtybit.battlechat.GameConfiguration;
import edu.dirtybit.battlechat.Session;
import edu.dirtybit.battlechat.SessionStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameState extends Session implements Runnable {

    private ArrayList<Board> boards;
    private ArrayList<Boolean> hasPlaced;
    private int currentPlayerIndex;
    private Phase phase;
    private int secondsToNextPhase;
    private int firingTimeout;
    private int placementTimeout;
    private BattleShipConfiguration cfg;

    public GameState(GameConfiguration config, Player player) {
        super(config, player);
        this.cfg = (BattleShipConfiguration) config;
        this.boards = new ArrayList<>();
        this.hasPlaced = new ArrayList<>();
        for(int i = 0; i < cfg.getPropertyAsInt(ConfigKeys.PLAYER_COUNT); i++) {
            this.hasPlaced.add(false);
        }

        this.initializeBoards(this.cfg);
        this.phase = Phase.NOT_STARTED;
        this.secondsToNextPhase = cfg.getPropertyAsInt(ConfigKeys.INITIALIZATION_TIME);
        this.firingTimeout = cfg.getPropertyAsInt(ConfigKeys.FIRING_TIMEOUT);
        this.placementTimeout = cfg.getPropertyAsInt(ConfigKeys.PLACEMENT_TIMEOUT);
        this.currentPlayerIndex = 0;
    }

    public void handleMessage(GameMessage msg) {
        switch (msg.getMessageType()) {
            case CONFIG_REQUEST:
                notifySubscribers(new GameMessage<>(GameMessageType.CONFIGURATION, msg.getId(), this.cfg));
                break;
            case PLACEMENT:
                this.handlePlacement(msg);
                break;
            case FIRE:
                this.handleFire(msg);
                break;
            case EVENT:
                this.notifySubscribers(msg);
                break;
        }
    }

    public void broadcastMessage(GameMessageType type, String msg) {
        this.getPlayers().forEach(p -> notifySubscribers(new GameMessage(type, p.getId(), msg)));
    }

    public boolean shouldStart() {
        if (this.getPlayers().size() == this.cfg.getPropertyAsInt(ConfigKeys.PLAYER_COUNT)) {
            return true;
        }
        return false;
    }

    public GameMessage<BattleChatStatus> status(Player p) {
        Phase ph = this.phase;
        if (ph == Phase.COMBAT) {
            ph = this.getPlayers().get(this.currentPlayerIndex).getId().equals(p.getId()) ? Phase.YOU_FIRING : Phase.OPPONENT_FIRING;
        }
        return new GameMessage<>(GameMessageType.STATUS, p.getId(), new BattleChatStatus(ph, this.secondsToNextPhase));
    }

    public void run() {
        do {
            tick();
        } while (this.phase != Phase.COMPLETED);
        this.setStatus(SessionStatus.COMPLETED);
    }

    public ArrayList<Board> getBoards() {
        return boards;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    // Returns true on a hit and false on a miss
    protected boolean fire(List<Coordinate> shots, Player player) throws FiringTooManyShotsException, FiringOutOfTurnException, FiringAtOwnBoardException {
        int shotsAllowed = cfg.getPropertyAsInt(ConfigKeys.SHOTS_PER_ROUND);

        if (shots.size() > shotsAllowed) {
            throw new FiringTooManyShotsException(shots.size(), shotsAllowed);
        }

        if (this.getPlayerIndex(player) != this.currentPlayerIndex) {
            throw new FiringOutOfTurnException();
        }

        boolean hit = false;
        for (int i = 0; i < shotsAllowed; i++) {
            Coordinate c = shots.get(i);
            if (c.getBoardIndex() == this.getPlayerIndex(player)) {
                throw new FiringAtOwnBoardException(c.getBoardIndex());
            }
            Board board = this.boards.get(c.getBoardIndex());
            String message = player.getGivenName() + " shot at " + c.getX() + "," + c.getY();
            if (board.getCells()[c.getX()][c.getY()] == CellType.SHIP) {
                board.setCell(c.getX(), c.getY(), CellType.HIT);
                message += " ...and HIT!";
                Ship s = board.getFleet().getShipAt(c.getX(), c.getY());
                if (s != null) {
                    s.removeCell(c.getX(), c.getY());
                    if (s.getHealth() == 0) {
                        message += " ...and sunk " + this.getPlayers().get(c.getBoardIndex()).getGivenName() + "'s " + s.getShiptype().toString() + ".";
                    }
                }
                hit = true;
            } else {
                board.setCell(c.getX(), c.getY(), CellType.MISS);
                message += " ...and missed...";
            }
            this.broadcastMessage(GameMessageType.EVENT, message);
            this.secondsToNextPhase = 3;
        }
        return hit;
    }

    protected Fleet randomizeFleet(Fleet fleet) {
        Random rng = new Random();
        Board testboard = new Board(this.cfg);

        do {
            testboard.clear();
            for (int i = 0; i < fleet.getShips().size(); i++) {
                Ship ship = fleet.getShips().get(i);
                // Start with a random rotation
                ship.setRotation(rng.nextBoolean() == true ? Rotation.HORIZONTAL : Rotation.VERTICAL);
                // Get the max possible placement values given the rotation
                int xmax = ship.getRotation() == Rotation.HORIZONTAL ? testboard.getWidth() - (ship.getShiptype().getLength() - 1) : testboard.getWidth();
                int ymax = ship.getRotation() == Rotation.VERTICAL ? testboard.getHeight() - (ship.getShiptype().getLength() - 1) : testboard.getHeight();

                // If a ship fails 10 random placements, restart the fleet placement
                int attempts = 10;
                for (int j = 0; j < attempts; j++) {
                    // pick a random starting location
                    ship.setLocation(rng.nextInt(xmax), rng.nextInt(ymax));
                    int endx = ship.getRotation() == Rotation.HORIZONTAL ? ship.getX() + (ship.getShiptype().getLength() - 1) : ship.getX();
                    int endy = ship.getRotation() == Rotation.VERTICAL ? ship.getY() + (ship.getShiptype().getLength() - 1) : ship.getY();

                    // Test if ship can be placed
                    boolean canplace = true;
                    for (int x = ship.getX(); x > 0 && x <= endx; x++) {
                        for (int y = ship.getY(); y > 0 && y <= endy; y++) {
                            if (testboard.getCells()[x][y] != CellType.EMPTY) {
                                canplace = false;
                            }
                        }
                    }

                    // Then place the ship
                    if (canplace) {
                        for (int x = ship.getX(); x > 0 && x <= endx; x++) {
                            for (int y = ship.getY(); y > 0 && y <= endy; y++) {
                                testboard.setCell(x, y, CellType.SHIP);
                            }
                        }
                        // Exit for loop
                        j = attempts;
                    }
                }
            }
        } while (!this.validateFleet(fleet));

        return fleet;
    }

    protected boolean validateFleet(Fleet fleet) {
        boolean valid = true;
        Board testboard = new Board(this.cfg);

        try {
            this.placeFleet(fleet, testboard);
        } catch (Exception e) {
            valid = false;
        }

        return valid;
    }

    protected void placeFleet(Fleet fleet, Board board) throws ShipOutOfBoundsException, ShipsOverlapException, InvalidFleetsizeException {
        // Clear the board if not already empty
        if (!board.isClear()) {
            board.clear();
        }
        // Make variables to keep count of ships in the fleet
        int canoes, cruisers, submarines, destroyers, battleships, carriers;
        canoes = cruisers = submarines = destroyers = battleships = carriers = 0;

        for (Ship ship : fleet.getShips()) {
            switch (ship.getShiptype()) {
                case CANOE:
                    canoes++;
                    break;
                case DESTROYER:
                    destroyers++;
                    break;
                case CRUISER:
                    cruisers++;
                    break;
                case SUBMARINE:
                    submarines++;
                    break;
                case BATTLESHIP:
                    battleships++;
                    break;
                case CARRIER:
                    carriers++;
                    break;
            }

            // Get the end coordinates of the ship
            int endx = ship.getRotation() == Rotation.HORIZONTAL ? ship.getX() + ship.getShiptype().getLength() - 1 : ship.getX();
            int endy = ship.getRotation() == Rotation.VERTICAL ? ship.getY() + ship.getShiptype().getLength() - 1 : ship.getY();

            // Check if the start coordinates are on the board
            if (ship.getX() >= 0 && ship.getX() < board.getWidth() -1 && ship.getY() >= 0 || ship.getY() < board.getHeight() -1) {
                // Check if end coordinates are on the board
                if (endx < board.getWidth() && endy < board.getHeight()) {
                    // Check if the ship is being placed on top of another ship
                    for (int x = ship.getX(); x <= endx; x++) {
                        for (int y = ship.getY(); y <= endy; y++) {
                            if (board.getCells()[x][y] == CellType.EMPTY) {
                                board.setCell(x, y, CellType.SHIP);
                            } else {
                                board.clear();
                                throw new ShipsOverlapException(x, ship.getY());
                            }
                        }
                    }
                } else {
                    board.clear();
                    throw new ShipOutOfBoundsException(ship, endx, endy);
                }
            } else {
                board.clear();
                throw new ShipOutOfBoundsException(ship, endx, endy);
            }
        }

        // Check that the amount of ships in the fleet matched with the amount specified in the rules
        if (canoes != this.cfg.getPropertyAsInt(ConfigKeys.CANOE_COUNT) ||
                destroyers != this.cfg.getPropertyAsInt(ConfigKeys.DESTROYER_COUNT) ||
                cruisers != this.cfg.getPropertyAsInt(ConfigKeys.CRUISER_COUNT) ||
                submarines != this.cfg.getPropertyAsInt(ConfigKeys.SUBMARINE_COUNT) ||
                battleships != this.cfg.getPropertyAsInt(ConfigKeys.BATTLESHIP_COUNT) ||
                carriers != this.cfg.getPropertyAsInt(ConfigKeys.CARRIER_COUNT)) {
            throw new InvalidFleetsizeException(InvalidFleetsizeException.MakeMessage(this.cfg, canoes, cruisers, submarines, destroyers, battleships, carriers));
        }
    }

    private void initializeBoards(BattleShipConfiguration config) {
        int players = config.getPropertyAsInt(ConfigKeys.PLAYER_COUNT);

        for (int i = 0; i < players; i++) {
            this.boards.add(new Board(config));
        }
    }

    private Player nextPlayer() {
        // Increment player index
        this.currentPlayerIndex++;
        // Reset player index if out of bounds
        if (this.currentPlayerIndex == this.getPlayers().size()) {
            this.currentPlayerIndex = 0;
        }
        // Return the current player as a player object
        return this.getPlayers().get(this.currentPlayerIndex);
    }

    private void tick() {
        this.secondsToNextPhase--;
        if (this.secondsToNextPhase <= 0) {
            phaseChange();
        }
        this.getPlayers().forEach(p -> this.notifySubscribers(this.status(p)));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ie) {
            System.err.println("Sleep interrupted: " + ie.getMessage());
        }
    }

    private void phaseChange() {
        switch (this.phase) {
            case NOT_STARTED:
                this.phase = Phase.PLACEMENT_PHASE;
                this.secondsToNextPhase = this.placementTimeout;
                break;
            case PLACEMENT_PHASE:
                this.getBoards().stream().filter(board -> !this.validateFleet(board.getFleet())).forEach(board -> {
                    board.setFleet(randomizeFleet(board.getFleet()));
                    try {
                        this.placeFleet(board.getFleet(), board);
                    } catch (Exception e) {
                        System.err.println("Fleet randomization method generated an invalid placement");
                        e.printStackTrace();
                    }
                    notifySubscribers(new GameMessage(GameMessageType.EVENT,
                            this.getPlayers().get(this.boards.indexOf(board)).getId(),
                            "You didn't arrange your fleet in time, so it was randomized for you."));
                });
                this.phase = Phase.COMBAT;
                this.secondsToNextPhase = this.firingTimeout;
                break;
            case COMBAT:
                this.nextPlayer();
                if (this.boards.get(this.currentPlayerIndex).getLivesRemaining() == 0) {
                    this.phase = Phase.COMPLETED;
                }
                this.secondsToNextPhase = this.firingTimeout;
                break;
        }
        sendUpdate();
    }

    private void sendUpdate() {
        this.getPlayers().forEach(p ->
            this.notifySubscribers(new GameMessage<>(GameMessageType.UPDATE, p.getId(), renderPerspective(p))));
    }

    private BoardUpdate renderPerspective(Player p) {
        ArrayList<BaseBoard> boards = new ArrayList<>();
        for(int i = 0; i < this.getPlayers().size(); i++) {
            if(i == this.getPlayerIndex(p)) {
                boards.add(this.getBoards().get(i));
            } else {
                boards.add(this.getBoards().get(i).getPerspective());
            }
        }
        return new BoardUpdate(boards, this.getPlayerIndex(p));
    }

    private int getPlayerIndex(Player player) {
        return getPlayers().indexOf(player);
    }

    private void checkForVictory() {
        this.getBoards().stream().filter(b -> b.getLivesRemaining() == 0).forEach(l -> {
            int loserIndex = this.getBoards().indexOf(l);
            this.notifySubscribers(new GameMessage(GameMessageType.STATUS, this.getPlayers().get(loserIndex).getId(), new BattleChatStatus(Phase.YOU_LOSE, 0)));
            this.getPlayers().remove(loserIndex);
        });
        if(this.getPlayers().size() == 1) {
            this.notifySubscribers(new GameMessage(GameMessageType.STATUS, this.getPlayers().get(0).getId(), new BattleChatStatus(Phase.YOU_WIN, 0)));
            this.phase = Phase.COMPLETED;
        }
    }

    private void handleFire(GameMessage<List<Coordinate>> shot) {
        try {
            boolean hit = this.fire(shot.getBody(), this.getPlayerById(shot.getId()));
            if(hit) {
                shot.getBody().forEach(s -> {
                    this.notifySubscribers(new GameMessage(GameMessageType.HIT, shot.getId(), s));
                });
            } else {
                 shot.getBody().forEach(s -> {
                    this.notifySubscribers(new GameMessage(GameMessageType.MISS, shot.getId(), s));
                });
            }
        } catch (FiringOutOfTurnException | FiringAtOwnBoardException | FiringTooManyShotsException e) {
            this.notifySubscribers(new GameMessage<>(GameMessageType.ERROR, shot.getId(), e.getMessage()));
        }
        checkForVictory();
    }

    private void handlePlacement(GameMessage<Fleet> placement) {
        Player p = this.getPlayerById(placement.getId());
        int pi = this.getPlayerIndex(p);
        try {
            placement.getBody().getShips().forEach(s -> s.resetCells());
            placeFleet(placement.getBody(), this.boards.get(pi));
            this.boards.get(pi).setFleet(placement.getBody());
            this.hasPlaced.set(pi, true);
            notifySubscribers(new GameMessage(GameMessageType.EVENT, placement.getId(), "You have successfully arranged your fleet."));
        } catch (ShipOutOfBoundsException | ShipsOverlapException | InvalidFleetsizeException e) {
            this.notifySubscribers(new GameMessage<>(GameMessageType.ERROR, placement.getId(), e.getMessage()));
        }

        boolean start = true;
        for (boolean b : this.hasPlaced) {
            if (!b) {
                start = false;
            }
        }
        if (start) {
            this.secondsToNextPhase = 3;
        }
    }
}
