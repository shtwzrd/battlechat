package edu.dirtybit.battlechat.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class PlayerTest {

    @Test
    public void Player_ShouldInitializeFleet_PostCreation() {
       Player player = new Player("test");
       assertNotNull(player.getFleet());
    }

    @Test
    public void Player_ShouldBeAssignedAnId_PostCreation() {
       Player player = new Player("test");
       assertNotNull(player.getId());
    }

    @Test
    public void Player_ShouldReportGivenName_PassedInConstructor() {
       Player player = new Player("test");
       assertEquals(player.getGivenName(), "test");
    }
}
