package edu.dirtybit.battlechat.model;

import java.util.ArrayList;

public class Perspective extends BaseBoard {

    public Perspective(int width, int height)
    {
        super(width, height);
    }

    public void setCell(int x, int y, CellType celltype)
    {
        if (celltype == CellType.Ship) { celltype = CellType.Empty; }
        this.cells[x][y] = celltype;
    }
}
