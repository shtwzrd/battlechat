package edu.dirtybit.battlechat;

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

    public UUID enterQueue() {
        if(!this.queue.isEmpty()) {
            return addToExisting(this.queue.peek());
        } else {
            return addToNewGame();
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

    private UUID addToNewGame() {
       Session newGame = SessionFactory.INSTANCE.createSession(defaultConfig);
       newGame.subscribe(this);
       this.sessions.put(newGame.getId(), newGame);
       this.playerToSession.put(newGame.getInitiator(), newGame.getId());
       this.queue.add(newGame);
       return newGame.getInitiator();
    }

    private UUID addToExisting(Session session) {
       return session.enqueuePlayer();
    }
}
