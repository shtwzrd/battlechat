package edu.dirtybit.battlechat.model;

import java.util.ArrayList;

public class Board extends BaseBoard {

    public Board(int width, int height)
    {
        super(width, height);
    }

    public void setCell(int x, int y, CellType celltype)
    {
        this.cells[x][y] = celltype;
    }
}
