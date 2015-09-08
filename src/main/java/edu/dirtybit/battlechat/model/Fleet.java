package edu.dirtybit.battlechat.model;

import java.util.ArrayList;

public class Fleet {
    private static final int CRUISERS = 1;
    private static final int SUBMARINES = 1;
    private static final int DESTROYERS = 1;
    private static final int BATTLESHIPS = 1;
    private static final int CARRIERS = 1;

    private ArrayList<Ship> ships = new ArrayList<Ship>();

    public Fleet()
    {
        this.initializeShips(CRUISERS, SUBMARINES, DESTROYERS, BATTLESHIPS, CARRIERS);
    }

    public Fleet(int cruisers, int submarines, int destroyers, int battleships, int carriers) {
        this.initializeShips(cruisers, submarines, destroyers, battleships, carriers);
    }

    private void initializeShips(int cruisers, int submarines, int destroyers, int battleships, int carriers)
    {
        int i = 0;
        for (i = 0; i < cruisers; i++) {
            this.ships.add(new Ship(0,0,Rotation.Horizontal,ShipType.CRUISER));
        }
        for (i = 0; i < submarines; i++) {
            this.ships.add(new Ship(0,0,Rotation.Horizontal,ShipType.SUBMARINE));
        }
        for (i = 0; i < destroyers; i++) {
            this.ships.add(new Ship(0,0,Rotation.Horizontal,ShipType.DESTROYER));
        }
        for (i = 0; i < battleships; i++) {
            this.ships.add(new Ship(0,0,Rotation.Horizontal,ShipType.BATTLESHIP));
        }
        for (i = 0; i < carriers; i++) {
            this.ships.add(new Ship(0,0,Rotation.Horizontal,ShipType.CARRIER));
        }
    }
}
