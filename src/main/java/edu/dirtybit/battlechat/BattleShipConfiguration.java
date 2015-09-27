package edu.dirtybit.battlechat;

import java.util.HashMap;
import java.util.Map;

public class BattleShipConfiguration implements GameConfiguration {
    private Map<String, String> settings;

    public BattleShipConfiguration() {
        settings = new HashMap<>();
        putDefaultsIfAbsent();
    }

    public BattleShipConfiguration(Map<String, String> settings) {
        this.settings = new HashMap<>();
        this.settings.putAll(settings);
        putDefaultsIfAbsent();
    }

    private void putDefaultsIfAbsent() {
        settings.putIfAbsent(ConfigKeys.GRID_WIDTH.toString(), "10");
        settings.putIfAbsent(ConfigKeys.GRID_HEIGHT.toString(), "10");
        settings.putIfAbsent(ConfigKeys.PLAYER_COUNT.toString(), "2");
        settings.putIfAbsent(ConfigKeys.INITIALIZATION_TIME.toString(), "10");
        settings.putIfAbsent(ConfigKeys.PLACEMENT_TIMEOUT.toString(), "60");
        settings.putIfAbsent(ConfigKeys.FIRING_TIMEOUT.toString(), "30");
        settings.putIfAbsent(ConfigKeys.CANOE_COUNT.toString(), "0");
        settings.putIfAbsent(ConfigKeys.CRUISER_COUNT.toString(), "1");
        settings.putIfAbsent(ConfigKeys.SUBMARINE_COUNT.toString(), "1");
        settings.putIfAbsent(ConfigKeys.DESTROYER_COUNT.toString(), "1");
        settings.putIfAbsent(ConfigKeys.BATTLESHIP_COUNT.toString(), "1");
        settings.putIfAbsent(ConfigKeys.CARRIER_COUNT.toString(), "1");
        settings.putIfAbsent(ConfigKeys.SHOTS_PER_ROUND.toString(), "1");
    }

    public String getProperty(String key) {
        return settings.containsKey(key) ? settings.get(key) : "";
    }

    public int getPropertyAsInt(String key) {
        return Integer.parseInt(getProperty(key));
    }

    public String getProperty(ConfigKeys key) {
        return settings.containsKey(key.toString()) ? settings.get(key.toString()) : "";
    }

    public int getPropertyAsInt(ConfigKeys key) {
        return Integer.parseInt(getProperty(key.toString()));
    }

    public enum ConfigKeys {
        GRID_WIDTH,
        GRID_HEIGHT,
        PLAYER_COUNT,
        INITIALIZATION_TIME,
        PLACEMENT_TIMEOUT,
        FIRING_TIMEOUT,
        SHOTS_PER_ROUND,
        CANOE_COUNT,
        CRUISER_COUNT,
        SUBMARINE_COUNT,
        DESTROYER_COUNT,
        BATTLESHIP_COUNT,
        CARRIER_COUNT
    }
}
