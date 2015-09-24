package edu.dirtybit.battlechat;

import edu.dirtybit.battlechat.BattleShipConfiguration;
import edu.dirtybit.battlechat.BattleShipConfiguration.ConfigKeys;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class BattleShipConfigurationTest {

    @Test
    public void BattleShipConfiguration_ShouldGetProperties() {
        BattleShipConfiguration config = new BattleShipConfiguration();

        String pfc_s = config.getProperty(ConfigKeys.GRID_WIDTH);
        String pfs_s = config.getProperty(ConfigKeys.GRID_WIDTH.toString());

        assertTrue(pfc_s == pfs_s);

        int pfc_i = config.getPropertyAsInt(ConfigKeys.GRID_WIDTH);
        int pfs_i = config.getPropertyAsInt(ConfigKeys.GRID_WIDTH.toString());

        assertTrue(pfc_i == pfs_i);
    }
}
