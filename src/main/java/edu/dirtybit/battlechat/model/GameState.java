package edu.dirtybit.battlechat.model;

import java.util.ArrayList;

/**
 * Created by philip on 08-09-15.
 */
public class GameState {
    private static final int DEFAULTSIZE = 8;

    private Player[] players = new Player[2];
    private Board[] boards = null;

    public GameState()
    {
        this.initializeBoards(GameState.DEFAULTSIZE, GameState.DEFAULTSIZE);
    }

    public GameState(int square)
    {
        this.initializeBoards(square, square);
    }

    public GameState(int width, int height)
    {
        this.initializeBoards(width, height);
    }

    private void initializeBoards(int width, int height)
    {
        if (this.boards == null)
        {
            this.boards = new Board[2];
            this.boards[0] = new Board(width, height);
            this.boards[1] = new Board(width, height);
        }
    }

    public Perspective getPerspective(Player player)
    {
        /* Some logic that generates a players view of the Board */
        return new Perspective(1,1);
    }
}
