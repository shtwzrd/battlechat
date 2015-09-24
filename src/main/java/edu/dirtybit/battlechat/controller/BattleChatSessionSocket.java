package edu.dirtybit.battlechat.controller;

import java.io.IOException;
import java.util.UUID;
import java.util.Map;
import java.util.HashMap;

import edu.dirtybit.battlechat.Lobby;
import edu.dirtybit.battlechat.model.GameMessage;
import edu.dirtybit.battlechat.util.SerializationHelper;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import com.google.gson.Gson;

@WebSocket
public class BattleChatSessionSocket implements SessionSocket {
    private Map<UUID, Session> sessions = new HashMap<>();
    private static final String PROTOCOLv1 = "v1";

    @OnWebSocketConnect
    public void onConnect(Session session) {
        String protocol = session.getUpgradeRequest().getSubProtocols().get(0);
        UUID id = UUID.randomUUID();

        if(protocol.equals(PROTOCOLv1)) {
           id = UUID.fromString(session.getUpgradeRequest().getSubProtocols().get(1));
        }

        this.sessions.put(id, session);
        Lobby.INSTANCE.registerConnection(id, this);
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        this.sessions.keySet().forEach(s ->
        {
            if (!this.sessions.get(s).isOpen()) {
                Lobby.INSTANCE.clearConnection(s);
            }
        });
        this.sessions.keySet().removeIf(s -> !this.sessions.get(s).isOpen());
    }

    @OnWebSocketMessage
    public void onMessage(String message) throws IOException {
        Lobby.INSTANCE.notifyMessage(SerializationHelper.deserializeMessage(message));
    }

    public void sendMessage(GameMessage message) throws IOException {
        Gson gson = new Gson();
        String json = gson.toJson(message);
        this.sessions.get(message.getId()).getRemote().sendString(json);
    }
}
