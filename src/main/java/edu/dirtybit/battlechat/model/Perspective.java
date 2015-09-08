package edu.dirtybit.battlechat.model;

/**
 * Created by philip on 08-09-15.
 */
public class Perspective {
    private int[][] cells1;
    private int[][] cells2;

    public Perspective(int width, int height)
    {
        this.cells1 = new int[width][height];
        this.cells2 = new int[width][height];
    }
}
