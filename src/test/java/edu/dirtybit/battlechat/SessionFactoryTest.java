package edu.dirtybit.battlechat;

import org.junit.Test;
import static org.junit.Assert.*;
import edu.dirtybit.battlechat.model.Player;
import edu.dirtybit.battlechat.model.GameState;

public class SessionFactoryTest {

    // If this fails, check that the ConfigurationProvider is specifying the correct type for Sessions
    @Test
    public void SessionFactory_ShouldNotReturnNull_WhenCreatingSessionFromConfigProvider() throws Exception {
        Session session = SessionFactory.INSTANCE.createSession(new MockConfiguration(), new Player("test"));
        assertNotNull(session);
    }

    @Test
    public void SessionFactory_ShouldNotReturnNull_WhenCreatingKnownSessionType() throws Exception {
        Session session = SessionFactory.INSTANCE.createSession(GameState.class, new MockConfiguration(), new Player("test"));
        assertNotNull(session);
    }

    @Test(expected = Exception.class)
    public void SessionFactory_ShouldThrowException_OnUnknownSessionType() throws Exception {
        Session session = SessionFactory.INSTANCE.createSession(Object.class, new MockConfiguration(), new Player("test"));
    }
}
