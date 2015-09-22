package edu.dirtybit.battlechat.model;

import edu.dirtybit.battlechat.BattleShipConfiguration;
import edu.dirtybit.battlechat.MockConfiguration;
import edu.dirtybit.battlechat.exceptions.InvalidFleetsizeException;
import edu.dirtybit.battlechat.exceptions.ShipOutOfBoundsException;
import edu.dirtybit.battlechat.exceptions.ShipsOverlapException;
import edu.dirtybit.battlechat.model.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class GameStateTest {

    @Test
    public void GameState_ShouldStart_WhenPlayersEqualsPlayerCount() {
        MockConfiguration config = new MockConfiguration();
        GameState game = new GameState(config, new Player("first"));

        int playersToHave = Integer.parseInt(config.getProperty(BattleShipConfiguration.ConfigKeys.PLAYER_COUNT.toString()));
        while (game.getPlayers().size() < playersToHave) {
            game.enqueuePlayer(new Player("next"));
        }

        assertTrue(game.shouldStart());
    }

    @Test
    public void GameState_ShouldInitializeBoardsToAllEmpty_OnConstruction() {
        MockConfiguration config = new MockConfiguration();
        GameState game = new GameState(config, new Player("test"));

        game.getBoards().forEach(b -> {
            for (int i = 0; i < b.getCells().length; i++) {
                for (int j = 0; j < b.getCells()[0].length; j++) {
                    assertEquals(b.getCells()[i][j], CellType.Empty);
                }
            }
        });
    }

    @Test
    public void GameState_ShouldValidateFleet() throws ShipsOverlapException, ShipOutOfBoundsException, InvalidFleetsizeException {
        MockConfiguration config = new MockConfiguration();
        GameState game = new GameState(config, new Player("test"));
        Board board = game.getBoards().get(0);
        Fleet fleet = board.getFleet();

        GameStateTest.SetupBaseFleet(fleet);

        game.validateFleet(fleet, board);
    }

    @Test(expected = InvalidFleetsizeException.class)
    public void GameState_ShouldNotValidateFleet_IfTooManyShips() throws ShipsOverlapException, ShipOutOfBoundsException, InvalidFleetsizeException {
        MockConfiguration config = new MockConfiguration();
        GameState game = new GameState(config, new Player("test"));
        Board board = game.getBoards().get(0);
        Fleet fleet = board.getFleet();

        GameStateTest.SetupBaseFleet(fleet);
        fleet.getShips().add(new Ship(board.getWidth()/2, board.getHeight()/2, Rotation.Horizontal, ShipType.CANOE));

        game.validateFleet(fleet, board);
    }

    @Test(expected = ShipOutOfBoundsException.class)
    public void GameState_ShouldNotValidateFleet_WhenShipOutOfBounds() throws ShipsOverlapException, ShipOutOfBoundsException, InvalidFleetsizeException {
        MockConfiguration config = new MockConfiguration();
        GameState game = new GameState(config, new Player("test"));
        Board board = game.getBoards().get(0);
        Fleet fleet = board.getFleet();

        GameStateTest.SetupBaseFleet(fleet);
        fleet.getShips().get(0).setLocation(-1, board.getHeight() + 1);

        game.validateFleet(fleet, board);
    }

    @Test(expected = ShipsOverlapException.class)
    public void GameState_ShouldNotValidateFleet_WhenShipsOverlap() throws ShipsOverlapException, ShipOutOfBoundsException, InvalidFleetsizeException {
        MockConfiguration config = new MockConfiguration();
        GameState game = new GameState(config, new Player("test"));
        Board board = game.getBoards().get(0);
        Fleet fleet = board.getFleet();

        GameStateTest.SetupBaseFleet(fleet);
        Ship ship1 = fleet.getShips().get(0);
        Ship ship2 = fleet.getShips().get(1);
        ship1.setLocation(ship2.getX() + 1, ship2.getY() - 1);
        ship1.setRotation(Rotation.Vertical);

        game.validateFleet(fleet, board);
    }

    private static Fleet SetupBaseFleet(Fleet fleet) {
        for (int i = 0; i < fleet.getShips().size(); i++) {
            fleet.getShips().get(i).setLocation(0, i);
        }
        return fleet;
    }

}
