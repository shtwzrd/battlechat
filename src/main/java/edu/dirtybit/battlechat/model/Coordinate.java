package edu.dirtybit.battlechat.model;

public class Coordinate {
    private int boardIndex;
    private int X;
    private int Y;

    public Coordinate(int b, int x, int y) {
        this.boardIndex = b;
        this.X = x;
        this.Y = y;
    }

    public int getX() {
        return X;
    }

    public void setX(int x) {
        X = x;
    }

    public int getY() {
        return Y;
    }

    public void setY(int y) {
        Y = y;
    }

    public int getBoardIndex() {
        return boardIndex;
    }

    public void setBoardIndex(int index) {
        boardIndex = index;
    }

}
