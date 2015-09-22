package edu.dirtybit.battlechat;

import java.util.HashMap;
import java.util.Map;

public class MockConfiguration implements GameConfiguration {
    private Map<String, String> settings;

    public MockConfiguration() {
        settings = new HashMap<>();
        settings.put(BattleShipConfiguration.ConfigKeys.GRID_WIDTH.toString(), "10");
        settings.put(BattleShipConfiguration.ConfigKeys.GRID_HEIGHT.toString(), "10");
        settings.put(BattleShipConfiguration.ConfigKeys.PLAYER_COUNT.toString(), "2");

        settings.put(BattleShipConfiguration.ConfigKeys.CANOE_COUNT.toString(), "0");
        settings.put(BattleShipConfiguration.ConfigKeys.CRUISER_COUNT.toString(), "1");
        settings.put(BattleShipConfiguration.ConfigKeys.SUBMARINE_COUNT.toString(), "1");
        settings.put(BattleShipConfiguration.ConfigKeys.DESTROUYER_COUNT.toString(), "1");
        settings.put(BattleShipConfiguration.ConfigKeys.BATTLESHIP_COUNT.toString(), "1");
        settings.put(BattleShipConfiguration.ConfigKeys.CARRIER_COUNT.toString(), "1");
    }

    public String getProperty(String key) {
        return settings.containsKey(key) ? settings.get(key) : "";
    }
}
