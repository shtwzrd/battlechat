package edu.dirtybit.battlechat.model;

import edu.dirtybit.battlechat.BattleShipConfiguration;
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

        this.initializeBoards(
                cfg.getPropertyAsInt(ConfigKeys.PLAYER_COUNT),
                cfg.getPropertyAsInt(ConfigKeys.GRID_WIDTH),
                cfg.getPropertyAsInt(ConfigKeys.GRID_HEIGHT));

        this.phase = Phase.WAITING_FOR_OPPONENT;
        this.secondsToNextPhase = cfg.getPropertyAsInt(ConfigKeys.MATCHMAKING_TIMEOUT);
        this.firingTimeout = cfg.getPropertyAsInt(ConfigKeys.FIRING_TIMEOUT);
        this.currentPlayerIndex = 0;
    }

    public void handleMessage(GameMessage msg) {
        switch (msg.getMessageType()) {
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

    public void run() {
        do {
            tick();
        } while (this.phase != Phase.COMPLETED);
    }


    public void setCell(Player player, int x, int y, CellType celltype) {
        int index = this.getPlayerIndex(player);

        boards.get(index).setCell(x, y, celltype);
    }

    public ArrayList<Board> getBoards() {
        return boards;
    }

    public void setBoards(ArrayList<Board> boards) {
        this.boards = boards;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public void setCurrentPlayerIndex(int currentPlayerIndex) {
        this.currentPlayerIndex = currentPlayerIndex;
    }

    public Player nextPlayer() {
        // Increment player index
        this.currentPlayerIndex++;
        // Reset player index if out of bounds
        if (this.currentPlayerIndex == this.getPlayers().size()) {
            this.currentPlayerIndex = 0;
        }
        // Return the current player as a player object
        return this.getPlayers().get(this.currentPlayerIndex);
    }

    public boolean validateFleet(Fleet fleet, Board board) {
        boolean isvalid = true;

        if (board.isClear() == true) {
            int i = 0;
            while (i < fleet.getShips().size()) {
                Ship ship = fleet.getShips().get(i);

                // Check if the start coordinates are on the board
                if (ship.getX() < 0 || ship.getX() > board.getWidth() ||
                        ship.getY() < 0 || ship.getY() > board.getHeight()) {
                    isvalid = false;
                } else {
                    // Check if end coordinates are on the board
                    switch (ship.getRotation()) {
                        case Horizontal:
                            int endx = ship.getX() + ship.getShiptype().getLength() - 1;
                            if (endx < 0 || endx > board.getWidth()) {
                                isvalid = false;
                            } else {
                                for (int x = ship.getX(); x >= endx; x++) {
                                    if (board.getCells()[x][ship.getY()] == CellType.Empty) {
                                        board.getCells()[x][ship.getY()] = CellType.Ship;
                                    } else {
                                        isvalid = false;
                                    }
                                }
                            }
                            break;
                        case Vertical:
                            int endy = ship.getY() + ship.getShiptype().getLength() - 1;
                            if (endy < 0 || endy > board.getWidth()) {
                                isvalid = false;
                            } else {
                                for (int y = ship.getY(); y >= endy; y++) {
                                    if (board.getCells()[ship.getX()][y] == CellType.Empty) {
                                        board.getCells()[ship.getX()][y] = CellType.Ship;
                                    } else {
                                        isvalid = false;
                                    }
                                }
                            }
                            break;
                    }
                }

                if (!isvalid) {
                    i = fleet.getShips().size();
                } else {
                    i++;
                }
            }

            if (!isvalid) {
                board.clear();
            }
        } else {
            isvalid = false;
        }

        return isvalid;
    }

    private void handleFire(GameMessage<List<Coordinate>> shot) {
        Player player = this.getPlayerById(shot.getId());
        Board board = this.getBoards().get((this.getPlayerIndex(player)));

        // Check if not players own board?
        for (int i = 0; i < Math.min(shot.getBody().size(), cfg.getPropertyAsInt(ConfigKeys.SHOTS_PER_ROUND)); i++) {
            Coordinate c = shot.getBody().get(i);
            if (board.getCells()[c.getX()][c.getY()] == CellType.Ship) {
                board.getCells()[c.getX()][c.getY()] = CellType.Hit;
            } else {
                board.getCells()[c.getX()][c.getY()] = CellType.Miss;
            }
        }

    }

    public GameMessage status(Player p) {
        Phase ph = this.phase;
        if (ph == Phase.COMBAT) {
            ph = this.getPlayers().get(this.currentPlayerIndex).getId().equals(p.getId()) ? Phase.YOU_FIRING : Phase.OPPONENT_FIRING;
        }
        return new GameMessage<>(GameMessageType.UPDATE, p.getId(), new BattleChatStatus(ph, this.secondsToNextPhase));
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
                this.currentPlayerIndex++;
                this.nextPlayer();
                this.secondsToNextPhase = this.firingTimeout;
                break;
        }
    }

    private void initializeBoards(int players, int width, int height) {
        for (int i = 0; i < players; i++) {
            this.boards.add(new Board(width, height));
        }
    }

    private int getPlayerIndex(Player player) {
        return getPlayers().indexOf(player);
    }

    private void handlePlacement(GameMessage<Fleet> placement) {
        Player p = this.getPlayerById(placement.getId());
        int pi = this.getPlayerIndex(p);
        if (validateFleet(placement.getBody(), this.boards.get(pi))) {
            p.setFleet(placement.getBody());
            this.hasPlaced.set(pi, true);
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
