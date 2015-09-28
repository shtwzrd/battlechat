package edu.dirtybit.battlechat.model;

public class Perspective extends BaseBoard {

    public Perspective(int width, int height)
    {
        super(width, height);
        this.clear();
    }

    @Override
    public void setCell(int x, int y, CellType celltype)
    {
        if (celltype == CellType.SHIP) { celltype = CellType.EMPTY; }
        super.setCell(x, y, celltype);
    }
}
