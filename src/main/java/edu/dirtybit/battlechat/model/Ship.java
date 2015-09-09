package edu.dirtybit.battlechat.model;

public class Ship {
    private int x;
    private int y;
    private Rotation rotation;
    private ShipType shiptype;

    public Ship(ShipType shiptype)
    {
        this(0, 0, Rotation.Horizontal, shiptype);
    }

    public Ship(int x, int y, Rotation rotation, ShipType shiptype)
    {
        this.x = x;
        this.y = y;
        this.rotation = rotation;
        this.shiptype = shiptype;
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Rotation getRotation() {
        return this.rotation;
    }

    public void setRotation(Rotation rotation) {
        this.rotation = rotation;
    }

    public ShipType getShiptype() {
        return shiptype;
    }

    public void setShiptype(ShipType shiptype) {
        this.shiptype = shiptype;
    }
}
