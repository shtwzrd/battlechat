package edu.dirtybit.battlechat.model;

import edu.dirtybit.battlechat.BattleShipConfiguration;
import edu.dirtybit.battlechat.BattleShipConfiguration.ConfigKeys;

import java.util.ArrayList;

public class Fleet {
    private ArrayList<Ship> ships;

    public Fleet(int canoes, int cruisers, int submarines, int destroyers, int battleships, int carriers) {
        this.ships = new ArrayList<>();

        int i = 0;
        for (i = 0; i < canoes; i++) {
            this.ships.add(new Ship(0, 0, Rotation.HORIZONTAL, ShipType.CANOE));
        }
        for (i = 0; i < cruisers; i++) {
            this.ships.add(new Ship(0, 0, Rotation.HORIZONTAL, ShipType.CRUISER));
        }
        for (i = 0; i < submarines; i++) {
            this.ships.add(new Ship(0, 0, Rotation.HORIZONTAL, ShipType.SUBMARINE));
        }
        for (i = 0; i < destroyers; i++) {
            this.ships.add(new Ship(0, 0, Rotation.HORIZONTAL, ShipType.DESTROYER));
        }
        for (i = 0; i < battleships; i++) {
            this.ships.add(new Ship(0, 0, Rotation.HORIZONTAL, ShipType.BATTLESHIP));
        }
        for (i = 0; i < carriers; i++) {
            this.ships.add(new Ship(0, 0, Rotation.HORIZONTAL, ShipType.CARRIER));
        }
    }

    public static Fleet fromConfig(BattleShipConfiguration config) {
        int canoes = config.getPropertyAsInt(ConfigKeys.CANOE_COUNT);
        int cruisers = config.getPropertyAsInt(ConfigKeys.CRUISER_COUNT);
        int submarines = config.getPropertyAsInt(ConfigKeys.SUBMARINE_COUNT);
        int destroyers = config.getPropertyAsInt(ConfigKeys.DESTROYER_COUNT);
        int battleships = config.getPropertyAsInt(ConfigKeys.BATTLESHIP_COUNT);
        int carriers = config.getPropertyAsInt(ConfigKeys.CARRIER_COUNT);

        return new Fleet(canoes, cruisers, submarines, destroyers, battleships, carriers);
    }

    public Ship getShipAt(int x, int y) {
        Ship value = null;
        for (int i = 0; i < this.ships.size(); i++) {
            Ship s = this.ships.get(i);
            if (s.isAt(x, y)) {
                value = s;
                i = this.ships.size();
            }
        }
        return value;
    }

    public ArrayList<Ship> getShips() {
        return ships;
    }
}
