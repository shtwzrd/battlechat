package edu.dirtybit.battlechat;

import edu.dirtybit.battlechat.model.Player;
import org.junit.Test;
import static org.junit.Assert.*;

public class SessionTest {

    @Test
    public void Session_ShouldHaveId_PostConstruction() {
       Player player = new Player("test");

       Session session = new MockSession(new MockConfiguration(), player);
       assertNotNull(session.getId());
    }

    @Test
    public void Session_StatusShouldBeEnqueued_PostConstruction() {
       Player player = new Player("test");

       Session session = new MockSession(new MockConfiguration(), player);
       assertEquals(session.getStatus(), SessionStatus.ENQUEUED);
    }

    @Test
    public void Session_ShouldAddSubscriberToList_WhenSubscribedTo() {
       Player player = new Player("test");

       Session session = new MockSession(new MockConfiguration(), player);
       SessionListener listener = new MockSessionListener();
       assertFalse(session.getSubscribers().contains(listener));
       session.subscribe(listener);
       assertTrue(session.getSubscribers().contains(listener));
    }

    @Test
    public void Session_InitatorShouldEqualPlayerPassedInConstructor_PostConstruction() {
        Player player = new Player("test");

        Session session = new MockSession(new MockConfiguration(), player);
        assertEquals(session.getInitiator(), player.getId());

        Player two = new Player("two");

        session.enqueuePlayer(two);
        assertEquals(session.getInitiator(), player.getId());
    }

    @Test
    public void Session_InitatorShouldStillEqualPlayerPassedInConstructor_AfterEnqueuing() {
        Player player = new Player("test");

        Session session = new MockSession(new MockConfiguration(), player);
        Player two = new Player("two");
        session.enqueuePlayer(two);
        assertEquals(session.getInitiator(), player.getId());
    }

    @Test
    public void Session_ShouldAddPlayerToPlayerList_PostConstruction() {
        Player player = new Player("test");

        Session session = new MockSession(new MockConfiguration(), player);
        assertEquals(session.getPlayers().size(), 1);
        assertEquals(session.getPlayers().get(0).getId(), player.getId());
    }

    @Test
    public void Session_ShouldAddPlayerToPlayerList_PostEnqueue() {
        Player player = new Player("test");

        Session session = new MockSession(new MockConfiguration(), player);
        Player two = new Player("two");
        session.enqueuePlayer(two);
        assertEquals(session.getPlayers().size(), 2);
        assertEquals(session.getPlayers().get(1).getId(), two.getId());
    }

    @Test
    public void Session_ShouldNotifySubscribers_OnNotifySubscribers() {
        Player player = new Player("test");

        Session session = new MockSession(new MockConfiguration(), player);
        MockSessionListener listener = new MockSessionListener();
        session.subscribe(listener);
        assertEquals(listener.notificationCount, 0);

        session.notifySubscribers();
        assertEquals(listener.notificationCount, 1);
    }

    @Test
    public void Session_ShouldNotifySubscribers_WhenSessionShouldStart() {
        Player player = new Player("test");

        Session session = new MockSession(new MockConfiguration(), player);
        MockSessionListener listener = new MockSessionListener();
        session.subscribe(listener);
        assertEquals(listener.notificationCount, 0);

        session.enqueuePlayer(new Player("two"));
        assertEquals(listener.notificationCount, 1);
    }
}
