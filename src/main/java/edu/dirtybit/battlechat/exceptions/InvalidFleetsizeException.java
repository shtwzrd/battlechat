package edu.dirtybit.battlechat.exceptions;

import edu.dirtybit.battlechat.BattleShipConfiguration;

public class InvalidFleetsizeException extends Exception {

    public InvalidFleetsizeException(String message) {
        super(message);
    }

    public static String MakeMessage(BattleShipConfiguration config, int canoes, int cruisers, int submarines, int destroyers, int battleships, int carriers)
    {
        String message = "Incorrect fleet size: ";
        message += "canoes = " + canoes + "(" + config.getPropertyAsInt(BattleShipConfiguration.ConfigKeys.CANOE_COUNT) + "), ";
        message += "cruisers = " + cruisers + "(" + config.getPropertyAsInt(BattleShipConfiguration.ConfigKeys.CRUISER_COUNT) + "), ";
        message += "submarines = " + submarines + "(" + config.getPropertyAsInt(BattleShipConfiguration.ConfigKeys.CANOE_COUNT) + "), ";
        message += "destroyers = " + destroyers + "(" + config.getPropertyAsInt(BattleShipConfiguration.ConfigKeys.CANOE_COUNT) + "), ";
        message += "battleships = " + battleships + "(" + config.getPropertyAsInt(BattleShipConfiguration.ConfigKeys.CANOE_COUNT) + "), ";
        message += "carriers = " + carriers + "(" + config.getPropertyAsInt(BattleShipConfiguration.ConfigKeys.CANOE_COUNT) + ")";
        return message;
    }
}
