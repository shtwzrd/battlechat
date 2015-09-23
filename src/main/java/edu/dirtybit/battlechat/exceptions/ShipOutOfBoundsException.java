package edu.dirtybit.battlechat.exceptions;

import edu.dirtybit.battlechat.model.Ship;

public class ShipOutOfBoundsException extends Exception {

    private Ship ship;

    public ShipOutOfBoundsException(Ship ship, int endx, int endy) {
        super("Ship of type \"" + ship.getShiptype().toString() + "\" was placed outside the board, or extends out of the board at: (\"" + ship.getX() + "\",\"" + ship.getY() + "\" - \"" + endx + "\",\"" + endy + "\")");
        this.ship = ship;
    }
}
