package edu.dirtybit.battlechat.model;

import edu.dirtybit.battlechat.MockSessionListener;
import edu.dirtybit.battlechat.model.BattleChatStatus.Phase;
import edu.dirtybit.battlechat.BattleShipConfiguration;
import edu.dirtybit.battlechat.BattleShipConfiguration.ConfigKeys;
import edu.dirtybit.battlechat.GameConfiguration;
import edu.dirtybit.battlechat.exceptions.InvalidFleetsizeException;
import edu.dirtybit.battlechat.exceptions.ShipOutOfBoundsException;
import edu.dirtybit.battlechat.exceptions.ShipsOverlapException;
import org.junit.Test;

import static org.junit.Assert.*;
import java.util.Map;
import java.util.HashMap;

public class GameStateTest {

    @Test
    public void GameState_PhaseShouldBeWaitingForOpponent_WhenPlayersLessThanPlayerCount() {
        GameConfiguration config = new BattleShipConfiguration();
        Player player = new Player("one");
        GameState game = new GameState(config, player);

        assertEquals(game.status(player).getBody().getGamePhase(), Phase.WAITING_FOR_OPPONENT);
    }

    @Test
    public void GameState_ShouldStart_WhenPlayersEqualsPlayerCount() {
        GameConfiguration config = new BattleShipConfiguration();
        GameState game = new GameState(config, new Player("first"));

        int playersToHave = config.getPropertyAsInt(ConfigKeys.PLAYER_COUNT.toString());
        while(game.getPlayers().size() < playersToHave) {
            game.enqueuePlayer(new Player("next"));
        }

        assertTrue(game.shouldStart());
    }

    @Test
    public void GameState_PhaseShouldBePlacement_WhenGameHasStarted() {
        GameConfiguration config = new BattleShipConfiguration();
        Player player = new Player("one");
        GameState game = new GameState(config, player);

        int playersToHave = config.getPropertyAsInt(ConfigKeys.PLAYER_COUNT.toString());
        while(game.getPlayers().size() < playersToHave) {
            game.enqueuePlayer(new Player("next"));
        }

        assertEquals(game.status(player).getBody().getGamePhase(), Phase.PLACEMENT_PHASE);
    }

    @Test
    public void GameState_PhaseShouldBeYouFiring_ForInitiator_AfterPlacementPhase() throws InterruptedException {
        Map<String, String> custom = new HashMap<>();
        custom.put(ConfigKeys.PLACEMENT_TIMEOUT.toString(), "1");

        BattleShipConfiguration cfg = new BattleShipConfiguration(custom);
        Player initiator = new Player("one");
        GameState game = new GameState(cfg, initiator);

        int playersToHave = cfg.getPropertyAsInt(ConfigKeys.PLAYER_COUNT);
        while(game.getPlayers().size() < playersToHave) {
            game.enqueuePlayer(new Player("next"));
        }

        Thread.sleep(1000);

        assertEquals(game.status(initiator).getBody().getGamePhase(), Phase.YOU_FIRING);
    }

        @Test
    public void GameState_PhaseShouldBeOpponentFiring_ForInitiator_AfterYouFiring() throws InterruptedException {
        Map<String, String> custom = new HashMap<>();
        custom.put(ConfigKeys.PLACEMENT_TIMEOUT.toString(), "1");
        custom.put(ConfigKeys.FIRING_TIMEOUT.toString(), "1");

        BattleShipConfiguration cfg = new BattleShipConfiguration(custom);
        Player initiator = new Player("one");
        GameState game = new GameState(cfg, initiator);

        int playersToHave = cfg.getPropertyAsInt(ConfigKeys.PLAYER_COUNT);
        while(game.getPlayers().size() < playersToHave) {
            game.enqueuePlayer(new Player("next"));
        }

        Thread.sleep(2000);

        assertEquals(game.status(initiator).getBody().getGamePhase(), Phase.YOU_FIRING);
    }

    @Test
    public void GameState_ShouldInitializeBoardsToAllEmpty_OnConstruction() {
        GameConfiguration config = new BattleShipConfiguration();
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
        GameState game = new GameState(new BattleShipConfiguration(), new Player("test"));
        Board board = game.getBoards().get(0);
        Fleet fleet = board.getFleet();

        GameStateTest.SetupBaseFleet(fleet);

        game.placeFleet(fleet, board);
    }

    @Test(expected = InvalidFleetsizeException.class)
    public void GameState_ShouldNotValidateFleet_IfTooManyShips() throws ShipsOverlapException, ShipOutOfBoundsException, InvalidFleetsizeException {
        GameState game = new GameState(new BattleShipConfiguration(), new Player("test"));
        Board board = game.getBoards().get(0);
        Fleet fleet = board.getFleet();

        GameStateTest.SetupBaseFleet(fleet);
        fleet.getShips().add(new Ship(board.getWidth() / 2, board.getHeight() / 2, Rotation.Horizontal, ShipType.CANOE));

        game.placeFleet(fleet, board);
    }

    @Test(expected = ShipOutOfBoundsException.class)
    public void GameState_ShouldNotValidateFleet_WhenShipOutOfBounds() throws ShipsOverlapException, ShipOutOfBoundsException, InvalidFleetsizeException {
        GameState game = new GameState(new BattleShipConfiguration(), new Player("test"));
        Board board = game.getBoards().get(0);
        Fleet fleet = board.getFleet();

        GameStateTest.SetupBaseFleet(fleet);
        fleet.getShips().get(0).setLocation(-1, board.getHeight() + 1);

        game.placeFleet(fleet, board);
    }

    @Test(expected = ShipsOverlapException.class)
    public void GameState_ShouldNotValidateFleet_WhenShipsOverlap() throws ShipsOverlapException, ShipOutOfBoundsException, InvalidFleetsizeException {
        GameState game = new GameState(new BattleShipConfiguration(), new Player("test"));
        Board board = game.getBoards().get(0);
        Fleet fleet = board.getFleet();

        GameStateTest.SetupBaseFleet(fleet);
        Ship ship1 = fleet.getShips().get(0);
        Ship ship2 = fleet.getShips().get(1);
        ship1.setLocation(ship2.getX() + 1, ship2.getY() - 1);
        ship1.setRotation(Rotation.Vertical);

        game.placeFleet(fleet, board);
    }

    @Test
    public void GameState_ShouldSendInvalidFleetSizeError_WhenFleetHasTooManyShips() {
        Player player1 = new Player("testone");
        Player player2 = new Player("testtwo");
        GameState game = new GameState(new BattleShipConfiguration(), player1);
        game.enqueuePlayer(player2);
        Board board = game.getBoards().get(0);
        Fleet fleet = board.getFleet();

        MockSessionListener listener = new MockSessionListener();
        game.subscribe(listener);

        GameStateTest.SetupBaseFleet(fleet);
        fleet.getShips().add(new Ship(board.getWidth() / 2, board.getHeight() / 2, Rotation.Horizontal, ShipType.CANOE));

        GameMessage<Fleet> message = new GameMessage<>(GameMessageType.PLACEMENT, player1.getId(), fleet);
        game.handleMessage(message);

        assertEquals(listener.lastMessageReceived.getMessageType(), GameMessageType.ERROR);
        assertEquals(listener.lastMessageReceived.getId(), player1.getId());
    }

    @Test
    public void GameState_ShouldSendShipOutOfBoundsError_WhenShipOutOfBounds() {
        Player player1 = new Player("testone");
        Player player2 = new Player("testtwo");
        GameState game = new GameState(new BattleShipConfiguration(), player1);
        game.enqueuePlayer(player2);
        Board board = game.getBoards().get(0);
        Fleet fleet = board.getFleet();

        MockSessionListener listener = new MockSessionListener();
        game.subscribe(listener);

        GameStateTest.SetupBaseFleet(fleet);
        fleet.getShips().get(0).setLocation(-1, board.getHeight() + 1);

        GameMessage<Fleet> message = new GameMessage<>(GameMessageType.PLACEMENT, player1.getId(), fleet);
        game.handleMessage(message);

        assertEquals(listener.lastMessageReceived.getMessageType(), GameMessageType.ERROR);
        assertEquals(listener.lastMessageReceived.getId(), player1.getId());
    }

    @Test
    public void GameState_ShouldSendShipsOverlapError_WhenShipsOverlap() {
        Player player1 = new Player("testone");
        Player player2 = new Player("testtwo");
        GameState game = new GameState(new BattleShipConfiguration(), player1);
        game.enqueuePlayer(player2);
        Board board = game.getBoards().get(0);
        Fleet fleet = board.getFleet();

        MockSessionListener listener = new MockSessionListener();
        game.subscribe(listener);

        GameStateTest.SetupBaseFleet(fleet);
        Ship ship1 = fleet.getShips().get(0);
        Ship ship2 = fleet.getShips().get(1);
        ship1.setLocation(ship2.getX() + 1, ship2.getY() - 1);
        ship1.setRotation(Rotation.Vertical);

        GameMessage<Fleet> message = new GameMessage<>(GameMessageType.PLACEMENT, player1.getId(), fleet);
        game.handleMessage(message);

        assertEquals(listener.lastMessageReceived.getMessageType(), GameMessageType.ERROR);
        assertEquals(listener.lastMessageReceived.getId(), player1.getId());
    }

    private static Fleet SetupBaseFleet(Fleet fleet) {
        for (int i = 0; i < fleet.getShips().size(); i++) {
            fleet.getShips().get(i).setLocation(0, i);
        }
        return fleet;
    }

}
