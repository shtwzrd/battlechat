package edu.dirtybit.battlechat;

import edu.dirtybit.battlechat.controller.SessionSocket;
import edu.dirtybit.battlechat.model.GameMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.io.IOException;

public enum Lobby {
    INSTANCE;

    private Map<UUID, SessionSocket> players = new HashMap<>();
    private Set<LobbyListener> listeners = new HashSet<>();

    public void registerConnection(UUID id, SessionSocket socket) {
        this.players.put(id, socket);
    }

    public void message(GameMessage message) {
        SessionSocket s = this.players.get(message.getId());
        if(s != null) {
            try {
                s.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void clearConnection(UUID id) {
        this.players.remove(id);
    }

    public void notifyMessage(GameMessage message) {
        this.listeners.forEach(l -> l.receiveMessage(message));
    }

    public void subscribe(LobbyListener listener) {
        this.listeners.add(listener);
    }
}
