package edu.dirtybit.battlechat.model;

import edu.dirtybit.battlechat.BattleShipConfiguration;
import edu.dirtybit.battlechat.GameConfiguration;
import edu.dirtybit.battlechat.Session;

import java.util.ArrayList;
import java.util.Iterator;

public class GameState extends Session {

    private ArrayList<Board> boards;
    private int currentPlayerIndex;

    public GameState(GameConfiguration config)
    {
        super(config);
        this.boards = new ArrayList<>();

        this.initializeBoards(
               Integer.parseInt(config.getProperty(BattleShipConfiguration.ConfigKeys.PLAYER_COUNT.toString())),
               Integer.parseInt(config.getProperty(BattleShipConfiguration.ConfigKeys.GRID_WIDTH.toString())),
               Integer.parseInt(config.getProperty(BattleShipConfiguration.ConfigKeys.GRID_HEIGHT.toString())));
    }

    @Override
    public void notifySubscribers() {
        this.getSubscribers().forEach(x -> x.notifySubscriber(this));
    }

    @Override
    public boolean shouldStart() {
        return this.getSubscribers().size() == Integer.parseInt(this.getConfig()
                .getProperty(BattleShipConfiguration.ConfigKeys.PLAYER_COUNT.toString()));
    }

    private void initializeBoards(int players, int width, int height)
    {
        for (int i = 0; i < players; i++)
        {
            this.boards.add(new Board(width, height));
        }
    }

    private int getPlayerIndex(Player player)
    {
        return getPlayers().indexOf(player);
    }

    public void setCell(Player player, int x, int y, CellType celltype)
    {
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

        int i = 0;
        while (i < fleet.getShips().size())
        {
            Ship ship = fleet.getShips().get(i);

            // Check if the start coordinates are on the board
            if (ship.getX() < 0 || ship.getX() > board.getWidth() ||
                ship.getY() < 0 || ship.getY() > board.getHeight()) { isvalid = false; }
            else {
                // Check if end coordinates are on the board
                switch (ship.getRotation()) {
                    case Horizontal:
                        int endx = ship.getX()+ship.getShiptype().getLength()-1;
                        if (endx < 0 || endx > board.getWidth()) { isvalid = false; }
                        else {
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
                        int endy = ship.getY()+ship.getShiptype().getLength()-1;
                        if (endy < 0 || endy > board.getWidth()) { isvalid = false; }
                        else {
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

                if (!isvalid) {
                    board.clear();
                    i = fleet.getShips().size();
                }
            }

            i++;
        }

        return isvalid;
    }
}
