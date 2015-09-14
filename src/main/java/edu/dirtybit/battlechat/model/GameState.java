package edu.dirtybit.battlechat.model;

import edu.dirtybit.battlechat.BattleShipConfiguration;
import edu.dirtybit.battlechat.GameConfiguration;
import edu.dirtybit.battlechat.Session;

import java.util.ArrayList;

public class GameState extends Session {

    private ArrayList<Board> boards;

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

}
