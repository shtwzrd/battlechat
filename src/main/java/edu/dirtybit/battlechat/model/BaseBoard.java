package edu.dirtybit.battlechat.model;

import java.util.ArrayList;
import java.util.List;

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

    public List<Coordinate> getEmptyCoordinates(int boardIndex) {
        List<Coordinate> coords = new ArrayList<>();
         for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if(this.cells[x][y] == CellType.Empty || this.cells[x][y] == CellType.Ship) {
                    coords.add(new Coordinate(boardIndex, x, y));
                }
            }
        }
        return coords;
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
                this.cells[x][y] = CellType.Empty;
            }
        }
    }
}
