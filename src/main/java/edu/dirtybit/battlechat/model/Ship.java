package edu.dirtybit.battlechat.model;

import java.util.List;

/**
 * Created by philip on 08-09-15.
 */
public class Ship {
    private int x = 0;
    private int y = 0;
    private Rotation rotation = Rotation.Horizontal;
    private ShipType shiptype = null;

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
