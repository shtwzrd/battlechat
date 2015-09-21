package edu.dirtybit.battlechat.model;

import edu.dirtybit.battlechat.BattleShipConfiguration;
import edu.dirtybit.battlechat.BattleShipConfiguration.ConfigKeys;
import edu.dirtybit.battlechat.GameConfiguration;
import org.junit.Test;
import static org.junit.Assert.*;

public class GameStateTest {

    @Test
    public void GameState_ShouldStart_WhenPlayersEqualsPlayerCount() {
        GameConfiguration config = new BattleShipConfiguration();
        GameState game = new GameState(config, new Player("first"));

        int playersToHave = Integer.parseInt(config.getProperty(ConfigKeys.PLAYER_COUNT.toString()));
        while(game.getPlayers().size() < playersToHave) {
            game.enqueuePlayer(new Player("next"));
        }

        assertTrue(game.shouldStart());
    }

    @Test
    public void GameState_ShouldInitializeBoardsToAllEmpty_OnConstruction() {
        GameConfiguration config = new BattleShipConfiguration();
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
