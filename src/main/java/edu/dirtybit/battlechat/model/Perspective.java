package edu.dirtybit.battlechat.model;

public class Perspective {
    private int[][] cells1;
    private int[][] cells2;

    public Perspective(int width, int height)
    {
        this.cells1 = new int[width][height];
        this.cells2 = new int[width][height];
    }
}
