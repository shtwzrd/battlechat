package edu.dirtybit.battlechat;

import edu.dirtybit.battlechat.model.Player;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

public class SessionManager implements SessionListener {
    private ConcurrentMap<UUID, UUID> playerToSession;
    private ConcurrentMap<UUID, Session> sessions;
    private ConcurrentLinkedQueue<Session> queue;
    private BattleShipConfiguration defaultConfig = new BattleShipConfiguration();

    public SessionManager() {
        this.sessions = new ConcurrentHashMap<>();
        this.playerToSession = new ConcurrentHashMap<>();
        this.queue = new ConcurrentLinkedQueue<>();
    }

    public Session getSessionContainingPlayer(UUID player) {
        return this.sessions.get(this.playerToSession.get(player));
    }

    public Session getSession(UUID session) {
        return this.sessions.get(session);
    }

    public UUID enterQueue(Player player) {
        if(!this.queue.isEmpty()) {
            return addToExisting(this.queue.peek(), player);
        } else {
            return addToNewGame(player);
        }
    }

    public void notifySubscriber(Session obj) {
        switch (obj.getStatus()) {
            case COMPLETED:
                while(this.playerToSession.values().remove(obj.getId()));
                this.sessions.remove(obj.getId());
                break;
            case IN_PROGRESS:
                this.queue.remove(obj);
                break;
        }
    }

    private UUID addToNewGame(Player player) {
       Session newGame = SessionFactory.INSTANCE.createSession(defaultConfig, player);
       newGame.subscribe(this);
       this.sessions.put(newGame.getId(), newGame);
       this.playerToSession.put(newGame.getInitiator(), newGame.getId());
       this.queue.add(newGame);
       return newGame.getInitiator();
    }

    private UUID addToExisting(Session session, Player player) {
       return session.enqueuePlayer(player);
    }
}
