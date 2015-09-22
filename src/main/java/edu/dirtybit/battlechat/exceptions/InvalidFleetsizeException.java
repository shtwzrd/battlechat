package edu.dirtybit.battlechat.exceptions;

import edu.dirtybit.battlechat.BattleShipConfiguration;
import edu.dirtybit.battlechat.GameConfiguration;
import edu.dirtybit.battlechat.model.Fleet;
import edu.dirtybit.battlechat.model.Ship;

public class InvalidFleetsizeException extends Exception {

    public InvalidFleetsizeException(String message) {
        super(message);
    }

    public static String MakeMessage(GameConfiguration config, int canoes, int cruisers, int submarines, int destroyers, int battleships, int carriers)
    {
        String message = "Incorrect fleet size: ";
        message += "canoes = " + canoes + "(" + Integer.parseInt(config.getProperty(BattleShipConfiguration.ConfigKeys.CANOE_COUNT.toString())) + "), ";
        message += "cruisers = " + cruisers + "(" + Integer.parseInt(config.getProperty(BattleShipConfiguration.ConfigKeys.CRUISER_COUNT.toString())) + "), ";
        message += "submarines = " + submarines + "(" + Integer.parseInt(config.getProperty(BattleShipConfiguration.ConfigKeys.CANOE_COUNT.toString())) + "), ";
        message += "destroyers = " + canoes + "(" + Integer.parseInt(config.getProperty(BattleShipConfiguration.ConfigKeys.CANOE_COUNT.toString())) + "), ";
        message += "battleships = " + canoes + "(" + Integer.parseInt(config.getProperty(BattleShipConfiguration.ConfigKeys.CANOE_COUNT.toString())) + "), ";
        message += "carriers = " + canoes + "(" + Integer.parseInt(config.getProperty(BattleShipConfiguration.ConfigKeys.CANOE_COUNT.toString())) + ")";
        return message;
    }
}
