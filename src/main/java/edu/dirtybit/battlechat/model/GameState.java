package edu.dirtybit.battlechat.model;

import edu.dirtybit.battlechat.BattleShipConfiguration;
import edu.dirtybit.battlechat.GameConfiguration;
import edu.dirtybit.battlechat.Session;
import edu.dirtybit.battlechat.exceptions.InvalidFleetsizeException;
import edu.dirtybit.battlechat.exceptions.ShipOutOfBoundsException;
import edu.dirtybit.battlechat.exceptions.ShipsOverlapException;

import java.util.ArrayList;
import java.util.Iterator;

public class GameState extends Session {

    private ArrayList<Board> boards;
    private int currentPlayerIndex;

    public GameState(GameConfiguration config, Player player) {
        super(config, player);
        this.boards = new ArrayList<>();

        this.initializeBoards(config);
    }

    @Override
    public boolean shouldStart() {
        return this.getPlayers().size() == Integer.parseInt(this.getConfig()
                .getProperty(BattleShipConfiguration.ConfigKeys.PLAYER_COUNT.toString()));
    }

    private void initializeBoards(GameConfiguration config) {
        int players = Integer.parseInt(config.getProperty(BattleShipConfiguration.ConfigKeys.PLAYER_COUNT.toString()));

        for (int i = 0; i < players; i++) {
            this.boards.add(new Board(config));
        }
    }

    private int getPlayerIndex(Player player) {
        return getPlayers().indexOf(player);
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

    public boolean validateFleet(Fleet fleet, Board board) throws ShipOutOfBoundsException, ShipsOverlapException, InvalidFleetsizeException {
        boolean isvalid = true;

        // check that the fleet contains the right ships
        int canoes,cruisers,submarines,destroyers,battleships,carriers;
        canoes = cruisers = submarines = destroyers = battleships = carriers = 0;
        for (Ship ship : fleet.getShips())
        {
            switch (ship.getShiptype())
            {
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
        if (    canoes != Integer.parseInt(this.getConfig().getProperty(BattleShipConfiguration.ConfigKeys.CANOE_COUNT.toString())) ||
                destroyers != Integer.parseInt(this.getConfig().getProperty(BattleShipConfiguration.ConfigKeys.DESTROUYER_COUNT.toString())) ||
                cruisers != Integer.parseInt(this.getConfig().getProperty(BattleShipConfiguration.ConfigKeys.CRUISER_COUNT.toString())) ||
                submarines != Integer.parseInt(this.getConfig().getProperty(BattleShipConfiguration.ConfigKeys.SUBMARINE_COUNT.toString())) ||
                battleships != Integer.parseInt(this.getConfig().getProperty(BattleShipConfiguration.ConfigKeys.BATTLESHIP_COUNT.toString())) ||
                carriers != Integer.parseInt(this.getConfig().getProperty(BattleShipConfiguration.ConfigKeys.CARRIER_COUNT.toString())) )
        {
            isvalid = false;
            throw new InvalidFleetsizeException(InvalidFleetsizeException.MakeMessage(this.getConfig(),canoes,cruisers,submarines,destroyers,battleships,carriers));
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
                    isvalid = false;
                    throw new ShipOutOfBoundsException(ship, endx, endy);
                } else {
                    // Check if end coordinates are on the board
                    switch (ship.getRotation()) {
                        case Horizontal:
                            if (endx < 0 || endx > board.getWidth()) {
                                isvalid = false;
                                throw new ShipOutOfBoundsException(ship, endx, endy);
                            } else {
                                for (int x = ship.getX(); x <= endx; x++) {
                                    if (board.getCells()[x][ship.getY()] == CellType.Empty) {
                                        board.getCells()[x][ship.getY()] = CellType.Ship;
                                    } else {
                                        isvalid = false;
                                        throw new ShipsOverlapException(x, ship.getY());
                                    }
                                }
                            }
                            break;
                        case Vertical:
                            if (endy < 0 || endy > board.getWidth()) {
                                isvalid = false;
                                throw new ShipOutOfBoundsException(ship, endx, endy);
                            } else {
                                for (int y = ship.getY(); y <= endy; y++) {
                                    if (board.getCells()[ship.getX()][y] == CellType.Empty) {
                                        board.getCells()[ship.getX()][y] = CellType.Ship;
                                    } else {
                                        isvalid = false;
                                        throw new ShipsOverlapException(ship.getX(), y);
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

    public void fire(Player player, Board board, int x, int y) {
        // Check if not players own board?

        if (board.getCells()[x][y] == CellType.Ship) {
            board.getCells()[x][y] = CellType.Hit;
        } else {
            board.getCells()[x][y] = CellType.Miss;
        }
    }
}
