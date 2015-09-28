package edu.dirtybit.battlechat.model;

public abstract class BaseBoard {
    protected CellType[][] cells;
    protected int width;
    protected int height;

    public BaseBoard(int width, int height) {
        this.width = width;
        this.height = height;
        this.cells = new CellType[width][height];
    }

    public CellType[][] getCells() {
        return this.cells;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setCell(int x, int y, CellType celltype) {
        this.cells[x][y] = celltype;
    }

    public void clear() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                this.cells[x][y] = CellType.EMPTY;
            }
        }
    }
}
