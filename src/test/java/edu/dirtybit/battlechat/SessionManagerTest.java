package edu.dirtybit.battlechat;

import edu.dirtybit.battlechat.model.GameMessage;
import edu.dirtybit.battlechat.model.GameMessageType;
import org.junit.Test;
import static org.junit.Assert.*;
import edu.dirtybit.battlechat.model.Player;

import java.util.UUID;

public class SessionManagerTest {

    @Test
    public void SessionManager_ShouldReturnUUIDMatchingPlayerId_OnEnterQueue() throws Exception {
        SessionManager manager = new SessionManager();
        Player player = new Player("test");
        UUID playerId = manager.enterQueue(player);

        assertEquals(playerId, player.getId());
    }

    @Test
    public void SessionManager_ShouldReturnSessionContainingPlayer_OnEnterQueue() throws Exception  {
        SessionManager manager = new SessionManager();
        Player player = new Player("test");
        UUID playerId = manager.enterQueue(player);

        Session session = manager.getSessionContainingPlayer(playerId);
        assertEquals(session.getInitiator(), player.getId());
    }

    @Test
    public void SessionManager_ShouldReturnNonNullSession_OnEnterQueue() throws Exception  {
        SessionManager manager = new SessionManager();
        Player player = new Player("test");
        UUID playerId = manager.enterQueue(player);

        Session session = manager.getSessionContainingPlayer(playerId);
        assertNotNull(session);
    }

    @Test
    public void SessionManager_ShouldGiveCorrectSessionOnReverseLookup_ViaGetSessionContainingPlayer() throws Exception  {
        SessionManager manager = new SessionManager();
        Player player = new Player("test");
        UUID playerId = manager.enterQueue(player);


        Session session = manager.getSessionContainingPlayer(playerId);
        Session alsoSession = manager.getSession(session.getId());

        assertEquals(session.getId(), alsoSession.getId());
    }

    @Test
    public void SessionManager_ShouldRemoveSessions_OnNotifySubscriber_WhenSessionIsCompleted() throws Exception  {
        SessionManager manager = new SessionManager();
        Player player = new Player("test");
        UUID playerId = manager.enterQueue(player);


        Session session = manager.getSessionContainingPlayer(playerId);
        session.status = SessionStatus.COMPLETED;
        manager.notifySubscriber(session, new GameMessage(GameMessageType.CHAT, player.getId(), ""));

        assertNull(manager.getSession(session.getId()));
        assertNull(manager.getSessionContainingPlayer(playerId));
    }

    @Test
    public void SessionManager_ShouldMatchMakePlayers_WhenTwoQueue() throws Exception {
        SessionManager manager = new SessionManager();

        Player player = new Player("one");
        UUID playerOneId = manager.enterQueue(player);
        Session session1 = manager.getSessionContainingPlayer(playerOneId);
        assertNotNull(session1);

        Player player2 = new Player("two");
        UUID playerTwoId = manager.enterQueue(player2);
        Session session2 = manager.getSessionContainingPlayer(playerTwoId);
        assertNotNull(session2);

        assertEquals(session1.getId(), session2.getId());
    }
}
