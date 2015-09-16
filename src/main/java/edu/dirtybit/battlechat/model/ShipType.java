package edu.dirtybit.battlechat.model;

public enum ShipType {
    CANOE(1), DESTROYER(2), CRUISER(3), SUBMARINE(3), BATTLESHIP(4), CARRIER(5);

    private int length;

    ShipType(int length) {
        this.length = length;
    }

    public int getLength() {
        return length;
    }
}
