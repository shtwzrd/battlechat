package edu.dirtybit.battlechat.model;

import edu.dirtybit.battlechat.BattleShipConfiguration;
import edu.dirtybit.battlechat.BattleShipConfiguration.ConfigKeys;
import org.junit.Test;
import static org.junit.Assert.*;

public class BoardTest {

    @Test
    public void Board_ShouldInitializeFleet_PostCreation() {
        Board board = new Board(new BattleShipConfiguration());
        assertNotNull(board.getFleet());
    }

    @Test
    public void Board_ShouldOverWriteFleet() {
        BattleShipConfiguration config = new BattleShipConfiguration();
        Board board = new Board(config);
        Fleet newfleet = Fleet.fromConfig(config);
        newfleet.getShips().get(0).setLocation(config.getPropertyAsInt(ConfigKeys.GRID_WIDTH)/2, config.getPropertyAsInt(ConfigKeys.GRID_HEIGHT)/2);
        board.setFleet(newfleet);

        assertTrue(board.getFleet().getShips().get(0).getX() == newfleet.getShips().get(0).getX());
        assertTrue(board.getFleet().getShips().get(0).getY() == newfleet.getShips().get(0).getY());
    }

    @Test
    public void Board_ShouldInitializePerspective_PostCreation() {
        Board board = new Board(new BattleShipConfiguration());
        assertNotNull(board.getPerspective());
    }
}
