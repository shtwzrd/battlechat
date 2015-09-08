package edu.dirtybit.battlechat.model;

public class Board {
    private int[][] cells1;
    private int[][] cells2;

    public Board(int width, int height)
    {
        this.cells1 = new int[width][height];
        this.cells2 = new int[width][height];
    }
}
