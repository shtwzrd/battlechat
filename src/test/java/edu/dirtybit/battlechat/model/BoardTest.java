package edu.dirtybit.battlechat.model;

import edu.dirtybit.battlechat.GameConfiguration;
import edu.dirtybit.battlechat.MockConfiguration;
import org.junit.Test;
import static org.junit.Assert.*;

public class BoardTest {

    @Test
    public void Board_ShouldInitializeFleet_PostCreation() {
        MockConfiguration config = new MockConfiguration();
        Board board = new Board(config);
        assertNotNull(board.getFleet());
    }
}
