package edu.dirtybit.battlechat.model;

import edu.dirtybit.battlechat.MockConfiguration;
import edu.dirtybit.battlechat.model.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class GameStateTest {

    @Test
    public void GameState_ShouldStart_WhenPlayersEqualsPlayerCount() {
        MockConfiguration config = new MockConfiguration();
        GameState game = new GameState(config, new Player("first"));

        int playersToHave = Integer.parseInt(config.getProperty(MockConfiguration.ConfigKeys.PLAYER_COUNT.toString()));
        while(game.getPlayers().size() < playersToHave) {
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
    public void GameState_ShouldValidateFleet() {
        MockConfiguration config = new MockConfiguration();
        GameState game = new GameState(config, new Player("test"));
        Fleet fleet = game.getPlayers().get(0).getFleet();
        Board board = game.getBoards().get(0);

        GameStateTest.SetupBaseFleet(fleet);

        assertTrue(game.validateFleet(fleet, board));
    }

    @Test
    public void GameState_ShouldNotValidateFleet_WhenShipOutOfBounds() {
        MockConfiguration config = new MockConfiguration();
        GameState game = new GameState(config, new Player("test"));
        Fleet fleet = game.getPlayers().get(0).getFleet();
        Board board = game.getBoards().get(0);

        GameStateTest.SetupBaseFleet(fleet);
        fleet.getShips().get(0).setLocation(-1, board.getHeight()+1);

        assertTrue(game.validateFleet(fleet, board));
    }

    @Test
    public void GameState_ShouldNotValidateFleet_WhenShipsOverlap() {
        MockConfiguration config = new MockConfiguration();
        GameState game = new GameState(config, new Player("test"));
        Fleet fleet = game.getPlayers().get(0).getFleet();
        Board board = game.getBoards().get(0);

        GameStateTest.SetupBaseFleet(fleet);
        Ship ship1 = fleet.getShips().get(0);
        Ship ship2 = fleet.getShips().get(1);
        ship1.setLocation(ship2.getX()+1, ship2.getY()-1);
        ship1.setRotation(Rotation.Vertical);

        assertTrue(game.validateFleet(fleet, board));
    }

    private static Fleet SetupBaseFleet(Fleet fleet) {
        for (int i = 0; i < fleet.getShips().size(); i++)
        {
            fleet.getShips().get(i).setLocation(0, i);
        }
        return fleet;
    }

}
