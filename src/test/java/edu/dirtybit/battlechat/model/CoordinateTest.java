package edu.dirtybit.battlechat.model;

import edu.dirtybit.battlechat.model.Coordinate;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class CoordinateTest {

    @Test
    public void Coordinate_ShouldChangeValues() {
        int initialB = 0;
        int initialX = 0;
        int initialY = 0;

        Coordinate coordinate = new Coordinate(initialB, initialX, initialY);

        assertTrue(initialB == coordinate.getBoardIndex());
        assertTrue(initialX == coordinate.getX());
        assertTrue(initialY == coordinate.getY());

        int newB = 5;
        int newX = 10;
        int newY = 8;

        coordinate.setBoardIndex(newB);
        coordinate.setX(newX);
        coordinate.setY(newY);

        assertTrue(newB == coordinate.getBoardIndex());
        assertTrue(newX == coordinate.getX());
        assertTrue(newY == coordinate.getY());
    }
}
