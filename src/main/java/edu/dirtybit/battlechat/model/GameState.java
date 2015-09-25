package edu.dirtybit.battlechat.model;

import edu.dirtybit.battlechat.BattleShipConfiguration;
import edu.dirtybit.battlechat.exceptions.*;
import edu.dirtybit.battlechat.model.BattleChatStatus.Phase;
import edu.dirtybit.battlechat.BattleShipConfiguration.ConfigKeys;
import edu.dirtybit.battlechat.GameConfiguration;
import edu.dirtybit.battlechat.Session;

import java.util.ArrayList;
import java.util.List;

public class GameState extends Session implements Runnable {

    private ArrayList<Board> boards;
    private ArrayList<Boolean> hasPlaced;
    private int currentPlayerIndex;
    private Phase phase;
    private int secondsToNextPhase;
    private int firingTimeout;
    private BattleShipConfiguration cfg;

    public GameState(GameConfiguration config, Player player) {
        super(config, player);
        this.cfg = (BattleShipConfiguration) config;
        this.boards = new ArrayList<>();
        this.hasPlaced = new ArrayList<>();

        this.initializeBoards(config);
        this.phase = Phase.WAITING_FOR_OPPONENT;
        this.secondsToNextPhase = cfg.getPropertyAsInt(ConfigKeys.MATCHMAKING_TIMEOUT);
        this.firingTimeout = cfg.getPropertyAsInt(ConfigKeys.FIRING_TIMEOUT);
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
        }
    }

    public boolean shouldStart() {
        if (this.getPlayers().size() == this.cfg.getPropertyAsInt(ConfigKeys.PLAYER_COUNT)) {
            this.phase = Phase.PLACEMENT_PHASE;
            this.secondsToNextPhase = this.cfg.getPropertyAsInt(ConfigKeys.PLACEMENT_TIMEOUT);
            return true;
        }
        return false;
    }

    public GameMessage<BattleChatStatus> status(Player p) {
        Phase ph = this.phase;
        if (ph == Phase.COMBAT) {
            ph = this.getPlayers().get(this.currentPlayerIndex).getId().equals(p.getId()) ? Phase.YOU_FIRING : Phase.OPPONENT_FIRING;
        }
        return new GameMessage<>(GameMessageType.UPDATE, p.getId(), new BattleChatStatus(ph, this.secondsToNextPhase));
    }

    public void run() {
        do {
            tick();
        } while (this.phase != Phase.COMPLETED);
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
            if (board.getCells()[c.getX()][c.getY()] == CellType.Ship) {
                board.setCell(c.getX(), c.getY(), CellType.Hit);
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
                board.setCell(c.getX(), c.getY(), CellType.Miss);
                message += " ...and missed...";
            }
            this.handleMessage(new GameMessage(GameMessageType.CHAT, player.getId(), message));
        }
        return hit;
    }

    protected void placeFleet(Fleet fleet, Board board) throws ShipOutOfBoundsException, ShipsOverlapException, InvalidFleetsizeException {
        // check that the fleet contains the right ships
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
        }
        if (canoes != this.cfg.getPropertyAsInt(ConfigKeys.CANOE_COUNT) ||
                destroyers != this.cfg.getPropertyAsInt(ConfigKeys.DESTROYER_COUNT) ||
                cruisers != this.cfg.getPropertyAsInt(ConfigKeys.CRUISER_COUNT) ||
                submarines != this.cfg.getPropertyAsInt(ConfigKeys.SUBMARINE_COUNT) ||
                battleships != this.cfg.getPropertyAsInt(ConfigKeys.BATTLESHIP_COUNT) ||
                carriers != this.cfg.getPropertyAsInt(ConfigKeys.CARRIER_COUNT)) {
            throw new InvalidFleetsizeException(InvalidFleetsizeException.MakeMessage(this.cfg, canoes, cruisers, submarines, destroyers, battleships, carriers));
        }

        if (board.isClear() == true) {
            int i = 0;
            while (i < fleet.getShips().size()) {
                Ship ship = fleet.getShips().get(i);
                int endx = ship.getRotation() == Rotation.Horizontal ? ship.getX() + ship.getShiptype().getLength() - 1 : ship.getX();
                int endy = ship.getRotation() == Rotation.Vertical ? ship.getY() + ship.getShiptype().getLength() - 1 : ship.getY();

                // Check if the start coordinates are on the board
                if (ship.getX() < 0 || ship.getX() > board.getWidth() ||
                        ship.getY() < 0 || ship.getY() > board.getHeight()) {
                    board.clear();
                    throw new ShipOutOfBoundsException(ship, endx, endy);
                } else {
                    // Check if end coordinates are on the board
                    switch (ship.getRotation()) {
                        case Horizontal:
                            if (endx < 0 || endx > board.getWidth()) {
                                board.clear();
                                throw new ShipOutOfBoundsException(ship, endx, endy);
                            } else {
                                for (int x = ship.getX(); x <= endx; x++) {
                                    if (board.getCells()[x][ship.getY()] == CellType.Empty) {
                                        board.setCell(x, ship.getY(), CellType.Ship);
                                    } else {
                                        board.clear();
                                        throw new ShipsOverlapException(x, ship.getY());
                                    }
                                }
                            }
                            break;
                        case Vertical:
                            if (endy < 0 || endy > board.getWidth()) {
                                board.clear();
                                throw new ShipOutOfBoundsException(ship, endx, endy);
                            } else {
                                for (int y = ship.getY(); y <= endy; y++) {
                                    if (board.getCells()[ship.getX()][y] == CellType.Empty) {
                                        board.setCell(ship.getX(), y, CellType.Ship);
                                    } else {
                                        board.clear();
                                        throw new ShipsOverlapException(ship.getX(), y);
                                    }
                                }
                            }
                            break;
                    }
                }
                i++;
            }
        }
    }

    private void initializeBoards(GameConfiguration config) {
        int players = Integer.parseInt(config.getProperty(BattleShipConfiguration.ConfigKeys.PLAYER_COUNT.toString()));

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
        if (this.secondsToNextPhase == 0) {
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
            case PLACEMENT_PHASE:
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
    }

    private int getPlayerIndex(Player player) {
        return getPlayers().indexOf(player);
    }

    private void handleFire(GameMessage<List<Coordinate>> shot) {
        try {
            this.fire(shot.getBody(), this.getPlayerById(shot.getId()));
        } catch (FiringOutOfTurnException e) {
            this.notifySubscribers(new GameMessage<>(GameMessageType.ERROR, shot.getId(), e.getMessage()));
        } catch (FiringAtOwnBoardException e) {
            this.notifySubscribers(new GameMessage<>(GameMessageType.ERROR, shot.getId(), e.getMessage()));
        } catch (FiringTooManyShotsException e) {
            this.notifySubscribers(new GameMessage<>(GameMessageType.ERROR, shot.getId(), e.getMessage()));
        }
    }

    private void handlePlacement(GameMessage<Fleet> placement) {
        Player p = this.getPlayerById(placement.getId());
        int pi = this.getPlayerIndex(p);
        try {
            placeFleet(placement.getBody(), this.boards.get(pi));
            this.boards.get(pi).setFleet(placement.getBody());
            this.hasPlaced.set(pi, true);
        } catch (ShipOutOfBoundsException e) {
            this.notifySubscribers(new GameMessage<>(GameMessageType.ERROR, placement.getId(), e.getMessage()));
        } catch (ShipsOverlapException e) {
            this.notifySubscribers(new GameMessage<>(GameMessageType.ERROR, placement.getId(), e.getMessage()));
        } catch (InvalidFleetsizeException e) {
            this.notifySubscribers(new GameMessage<>(GameMessageType.ERROR, placement.getId(), e.getMessage()));
        }

        boolean start = true;
        for (boolean b : this.hasPlaced) {
            if (!b) {
                start = false;
            }
        }

        if (start) {
            this.phase = Phase.COMBAT;
        }
    }
}
