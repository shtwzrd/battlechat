package edu.dirtybit.battlechat.model;

import java.util.ArrayList;

public class Board extends BaseBoard
{
    private Perspective perspective;
    private boolean cleared;

    public Board(int width, int height)
    {
        super(width, height);
        this.perspective = new Perspective(width, height);
        this.width = width;
        this.height = height;
        this.clear();
        this.cleared = true;
    }

    @Override
    public void setCell(int x, int y, CellType celltype)
    {
        super.setCell(x, y, celltype);
        this.perspective.setCell(x, y, celltype);
    }

    public Perspective getPerspective()
    {
        return this.perspective;
    }

    @Override
    public void clear()
    {
        super.clear();
        this.perspective.clear();
        this.cleared = true;
    }

    public boolean isClear() {
        return this.cleared;
    }
}
