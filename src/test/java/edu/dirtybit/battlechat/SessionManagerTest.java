package edu.dirtybit.battlechat;

import org.junit.Test;
import static org.junit.Assert.*;
import edu.dirtybit.battlechat.model.Player;

import java.util.UUID;

public class SessionManagerTest {

    @Test
    public void SessionManager_ShouldReturnUUIDMatchingPlayerId_OnEnterQueue() {
        SessionManager manager = new SessionManager();
        Player player = new Player("test");
        UUID playerId = manager.enterQueue(player);

        assertEquals(playerId, player.getId());
    }

    @Test
    public void SessionManager_ShouldReturnSessionContainingPlayer_OnEnterQueue() {
        SessionManager manager = new SessionManager();
        Player player = new Player("test");
        UUID playerId = manager.enterQueue(player);

        Session session = manager.getSessionContainingPlayer(playerId);
        assertEquals(session.getInitiator(), player.getId());
    }

    @Test
    public void SessionManager_ShouldReturnNonNullSession_OnEnterQueue() {
        SessionManager manager = new SessionManager();
        Player player = new Player("test");
        UUID playerId = manager.enterQueue(player);

        Session session = manager.getSessionContainingPlayer(playerId);
        assertNotNull(session);
    }

    @Test
    public void SessionManager_ShouldGiveCorrectSessionOnReverseLookup_ViaGetSessionContainingPlayer() {
        SessionManager manager = new SessionManager();
        Player player = new Player("test");
        UUID playerId = manager.enterQueue(player);


        Session session = manager.getSessionContainingPlayer(playerId);
        Session alsoSession = manager.getSession(session.getId());

        assertEquals(session.getId(), alsoSession.getId());
    }

    @Test
    public void SessionManager_ShouldRemoveSessions_OnNotifySubscriber_WhenSessionIsCompleted() {
        SessionManager manager = new SessionManager();
        Player player = new Player("test");
        UUID playerId = manager.enterQueue(player);


        Session session = manager.getSessionContainingPlayer(playerId);
        session.status = SessionStatus.COMPLETED;
        manager.notifySubscriber(session);

        assertNull(manager.getSession(session.getId()));
        assertNull(manager.getSessionContainingPlayer(playerId));
    }
}
