package edu.dirtybit.battlechat;

import org.junit.Test;
import static org.junit.Assert.*;
import edu.dirtybit.battlechat.model.Player;

public class SessionFactoryTest {

    // If this fails, check that the ConfigurationProvider is specifying the correct type for Sessions
    @Test
    public void SessionFactory_ShouldNotReturnNull_WhenCreatingSession() {
        Session session = SessionFactory.INSTANCE.createSession(new MockConfiguration(), new Player("test"));
        assertNotNull(session);
    }
}
