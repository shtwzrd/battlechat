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
    }

    public String getProperty(String key) {
        return settings.containsKey(key) ? settings.get(key) : "";
    }

    public enum ConfigKeys {
        GRID_WIDTH,
        GRID_HEIGHT,
        PLAYER_COUNT
    }
}
