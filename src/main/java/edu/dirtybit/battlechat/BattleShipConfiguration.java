package edu.dirtybit.battlechat;

import java.util.HashMap;
import java.util.Map;

public class BattleShipConfiguration implements GameConfiguration {
    private Map<String, String> settings;

    public BattleShipConfiguration() {
        settings = new HashMap<>();
        settings.put(ConfigKeys.GRID_WIDTH.toString(), "10");
        settings.put(ConfigKeys.GRID_HEIGHT.toString(), "10");
        settings.put(ConfigKeys.PLAYER_COUNT.toString(), "2");

        settings.put(ConfigKeys.CANOE_COUNT.toString(), "0");
        settings.put(ConfigKeys.CRUISER_COUNT.toString(), "1");
        settings.put(ConfigKeys.SUBMARINE_COUNT.toString(), "1");
        settings.put(ConfigKeys.DESTROUYER_COUNT.toString(), "1");
        settings.put(ConfigKeys.BATTLESHIP_COUNT.toString(), "1");
        settings.put(ConfigKeys.CARRIER_COUNT.toString(), "1");
    }

    public String getProperty(String key) {
        return settings.containsKey(key) ? settings.get(key) : "";
    }

    public enum ConfigKeys {
        GRID_WIDTH,
        GRID_HEIGHT,
        PLAYER_COUNT,
        CANOE_COUNT,
        CRUISER_COUNT,
        SUBMARINE_COUNT,
        DESTROUYER_COUNT,
        BATTLESHIP_COUNT,
        CARRIER_COUNT
    }
}
