package edu.dirtybit.battlechat.model;

import java.util.ArrayList;

public abstract class BaseBoard {
    protected CellType[][] cells;

    public BaseBoard(int width, int height)
    {
        this.cells = new CellType[width][height];

        for (int x = 0; x < width; x++)
        {
            for (int y = 0; y < height; y++)
            {
                this.cells[x][y] = CellType.Empty;
            }
        }
    }

    public CellType[][] getCells() {
        return this.cells;
    }
}
