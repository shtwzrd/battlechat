package edu.dirtybit.battlechat.model;

import edu.dirtybit.battlechat.BattleShipConfiguration;
import edu.dirtybit.battlechat.GameConfiguration;

import java.util.ArrayList;

public class Fleet {
    private ArrayList<Ship> ships;

    public Fleet(int canoes, int cruisers, int submarines, int destroyers, int battleships, int carriers) {
        this.ships = new ArrayList<>();

        int i = 0;
        for (i = 0; i < canoes; i++) {
            this.ships.add(new Ship(0, 0, Rotation.Horizontal, ShipType.CANOE));
        }
        for (i = 0; i < cruisers; i++) {
            this.ships.add(new Ship(0, 0, Rotation.Horizontal, ShipType.CRUISER));
        }
        for (i = 0; i < submarines; i++) {
            this.ships.add(new Ship(0, 0, Rotation.Horizontal, ShipType.SUBMARINE));
        }
        for (i = 0; i < destroyers; i++) {
            this.ships.add(new Ship(0, 0, Rotation.Horizontal, ShipType.DESTROYER));
        }
        for (i = 0; i < battleships; i++) {
            this.ships.add(new Ship(0, 0, Rotation.Horizontal, ShipType.BATTLESHIP));
        }
        for (i = 0; i < carriers; i++) {
            this.ships.add(new Ship(0, 0, Rotation.Horizontal, ShipType.CARRIER));
        }
    }

    public static Fleet fromConfig(GameConfiguration config) {
        int canoes = Integer.parseInt(config.getProperty(BattleShipConfiguration.ConfigKeys.CANOE_COUNT.toString()));
        int cruisers = Integer.parseInt(config.getProperty(BattleShipConfiguration.ConfigKeys.CRUISER_COUNT.toString()));
        int submarines = Integer.parseInt(config.getProperty(BattleShipConfiguration.ConfigKeys.SUBMARINE_COUNT.toString()));
        int destroyers = Integer.parseInt(config.getProperty(BattleShipConfiguration.ConfigKeys.DESTROYER_COUNT.toString()));
        int battleships = Integer.parseInt(config.getProperty(BattleShipConfiguration.ConfigKeys.BATTLESHIP_COUNT.toString()));
        int carriers = Integer.parseInt(config.getProperty(BattleShipConfiguration.ConfigKeys.CARRIER_COUNT.toString()));

        return new Fleet(canoes, cruisers, submarines, destroyers, battleships, carriers);
    }

    public ArrayList<Ship> getShips() {
        return ships;
    }
}
