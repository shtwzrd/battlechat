package edu.dirtybit.battlechat.model;

import java.util.ArrayList;

public class Board extends BaseBoard {
    private Perspective perspective;

    public Board(int width, int height)
    {
        super(width, height);
        this.perspective = new Perspective(width, height);
    }

    public void setCell(int x, int y, CellType celltype)
    {
        this.cells[x][y] = celltype;
        this.perspective.setCell(x, y, celltype);
    }

    public Perspective getPerspective()
    {
        return this.perspective;
    }
}
