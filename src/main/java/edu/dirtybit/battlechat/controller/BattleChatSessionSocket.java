package edu.dirtybit.battlechat.controller;

import java.io.IOException;
import java.util.UUID;
import java.util.HashMap;
import com.google.gson.Gson;

import edu.dirtybit.battlechat.Lobby;
import edu.dirtybit.battlechat.model.GameMessage;
import edu.dirtybit.battlechat.model.GameMessageType;
import edu.dirtybit.battlechat.util.SerializationHelper;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

@WebSocket
public class BattleChatSessionSocket implements SessionSocket {
    private HashMap<String, Session> addressToSessions = new HashMap<>();
    private HashMap<UUID, Session> uuidToSessions = new HashMap<>();

    @OnWebSocketConnect
    public void onConnect(Session session) {
        String addy = session.getRemoteAddress().toString();
        this.addressToSessions.put(addy, session);
        GameMessage message = new GameMessage(GameMessageType.REGISTRATION, UUID.randomUUID(), addy);
        Gson gson = new Gson();
        String json = gson.toJson(message);
        try {
            session.getRemote().sendString(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        this.uuidToSessions.keySet().forEach(s ->
        {
            if (!this.uuidToSessions.get(s).isOpen()) {
                Lobby.INSTANCE.clearConnection(s);
            }
        });
        this.uuidToSessions.keySet().removeIf((s -> !this.uuidToSessions.get(s).isOpen()));
    }

    @OnWebSocketMessage
    public void onMessage(String message) throws IOException {
        GameMessage packet = SerializationHelper.deserializeMessage(message);
        if(packet.getMessageType() == GameMessageType.REGISTRATION)  {
            UUID id = packet.getId();
            this.uuidToSessions.put(id, this.addressToSessions.get(packet.getBody()));
            Lobby.INSTANCE.registerConnection(id, this);
        } else {
            Lobby.INSTANCE.notifyMessage(SerializationHelper.deserializeMessage(message));
        }
    }

    public void sendMessage(GameMessage message) throws IOException {
        Gson gson = new Gson();
        String json = gson.toJson(message);
        this.uuidToSessions.get(message.getId()).getRemote().sendString(json);
    }
}
