package edu.dirtybit.battlechat;

import edu.dirtybit.battlechat.model.GameMessage;
import edu.dirtybit.battlechat.model.GameMessageType;
import org.eclipse.jetty.websocket.api.*;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.UUID;

public class LobbyTest {

    @Test
    public void Lobby_ShouldNotifySubscribers_OnNotifyMessage() {
        MockLobbyListener listener = new MockLobbyListener();
        Lobby.INSTANCE.subscribe(listener);

        Lobby.INSTANCE.notifyMessage(new GameMessage(GameMessageType.CHAT, UUID.randomUUID(), "test"));

        assertEquals(listener.callCount, 1);
    }

        @Test
    public void Lobby_ShouldSendMessage() {
        UUID id = UUID.randomUUID();
        MockSessionSocket socket = new MockSessionSocket();
        Lobby.INSTANCE.registerConnection(id, socket);
        Lobby.INSTANCE.message(new GameMessage(GameMessageType.CHAT, id, "test"));

        assertTrue(socket.sentMessage);
    }

}
