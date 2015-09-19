package edu.dirtybit.battlechat.edu.dirtybit.battlechat.model;

import edu.dirtybit.battlechat.MockConfiguration;
import org.junit.Test;
import static org.junit.Assert.*;
import edu.dirtybit.battlechat.model.GameState;
import edu.dirtybit.battlechat.model.CellType;
import edu.dirtybit.battlechat.model.Player;

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
            for(int i = 0; i < b.getCells().length; i++) {
               for(int j = 0; j < b.getCells()[0].length; j++) {
                  assertEquals(b.getCells()[i][j], CellType.Empty);
               }
            }
        });
    }

}
