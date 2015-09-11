package edu.dirtybit.battlechat.model;

import java.util.ArrayList;

public class GameState {
    private static final int DEFAULTPLAYERS = 2;
    private static final int DEFAULTSIZE = 8;

    private ArrayList<Player> players;
    private ArrayList<Board> boards;

    public GameState()
    {
        this(DEFAULTSIZE, DEFAULTSIZE);
    }

    public GameState(int square)
    {
        this(square, square);
    }

    public GameState(int width, int height)
    {
        this.players = new ArrayList<Player>();
        this.boards = new ArrayList<Board>();

        this.initializeBoards(DEFAULTPLAYERS, width, height);
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
}
