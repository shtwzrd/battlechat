package edu.dirtybit.battlechat.model;

import edu.dirtybit.battlechat.BattleShipConfiguration;
import org.junit.Test;
import static org.junit.Assert.*;

public class BoardTest {

    @Test
    public void Board_ShouldInitializeFleet_PostCreation() {
        Board board = new Board(new BattleShipConfiguration());
        assertNotNull(board.getFleet());
    }
}
