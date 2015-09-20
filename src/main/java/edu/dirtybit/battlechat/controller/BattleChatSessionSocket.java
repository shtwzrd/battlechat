package edu.dirtybit.battlechat.controller;

import java.io.IOException;
import java.util.UUID;
import java.util.Map;
import java.util.HashMap;

import edu.dirtybit.battlechat.BattleShipConfiguration;
import edu.dirtybit.battlechat.Lobby;
import edu.dirtybit.battlechat.model.GameMessage;
import edu.dirtybit.battlechat.model.Perspective;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@WebSocket
public class BattleChatSessionSocket implements SessionSocket {
    private Map<UUID, Session> sessions = new HashMap<>();

    @OnWebSocketConnect
    public void onConnect(Session session) {
        UUID id = UUID.fromString(session.getUpgradeRequest().getSubProtocols().get(0));
        this.sessions.put(id, session);
        Lobby.INSTANCE.registerConnection(id, this);
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        this.sessions.keySet().forEach(s ->
        {
            if (!this.sessions.get(s).isOpen()) {
                this.sessions.remove(s);
                Lobby.INSTANCE.clearConnection(s);
            }
        });
    }

    @OnWebSocketMessage
    public void onMessage(String message) throws IOException {
        Lobby.INSTANCE.notifyMessage(deserializeMessage(message));
    }

    public void sendMessage(GameMessage message) throws IOException {
        Gson gson = new Gson();
        String json = gson.toJson(message);
        this.sessions.get(message.getId()).getRemote().sendString(json);
    }

    private GameMessage deserializeMessage(String message) {
        Gson gson = new Gson();
        GameMessage obj = gson.fromJson(message, GameMessage.class);

        TypeToken type = new TypeToken<GameMessage<String>>() {};
        switch(obj.getMessageType()) {
            case CHAT:
                type = new TypeToken<GameMessage<String>>() {};
                break;
            case CONFIGURATION:
                type = new TypeToken<GameMessage<BattleShipConfiguration>>() {};
                break;
            case PLACEMENT:
                // do something
                break;
            case STATUS:
                // do something
                break;
            case FIRE:
                // do something
                break;
            case UPDATE:
                type = new TypeToken<GameMessage<Perspective>>() {};
                break;
        }

        return gson.fromJson(message, type.getType());
    }
}
