package edu.dirtybit.battlechat.model;

import edu.dirtybit.battlechat.GameConfiguration;
import edu.dirtybit.battlechat.Session;
import edu.dirtybit.battlechat.SessionListener;
import edu.dirtybit.battlechat.SessionStatus;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

public class GameState extends Session {

    private ArrayList<Player> players;
    private ArrayList<Board> boards;
    private HashSet<SessionListener> subscribers;
    private SessionStatus status;

    public GameState(GameConfiguration config)
    {
        super(config);
        this.boards = new ArrayList<>();
        this.status = SessionStatus.ENQUEUED;

        this.initializeBoards(Integer.parseInt(config.getProperty("PLAYER_COUNT")),
               Integer.parseInt(config.getProperty("GRID_WIDTH")),
               Integer.parseInt(config.getProperty("GRID_HEIGHT")));
    }

    private void initializeBoards(int players, int width, int height)
    {
        for (int i = 0; i < players; i++)
        {
            this.players.add(new Player());
            this.boards.add(new Board(width, height));
        }
    }

    private int getPlayerIndex(Player player)
    {
        return this.players.indexOf(player);
    }

    public void setCell(Player player, int x, int y, CellType celltype)
    {
        int index = this.getPlayerIndex(player);

        boards.get(index).setCell(x, y, celltype);
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public ArrayList<Board> getBoards() {
        return boards;
    }

    public void setBoards(ArrayList<Board> boards) {
        this.boards = boards;
    }

    public UUID queuePlayer() {
        Player player = new Player();
        this.players.add(player);
        return player.getId();
    }


}
