package edu.dirtybit.battlechat;

import edu.dirtybit.battlechat.model.Player;

import java.util.*;

public abstract class Session {
    private GameConfiguration config;
    private UUID id;
    private List<Player> players;
    private Set<SessionListener> subscribers;
    private SessionStatus status;

    public Session(GameConfiguration config)
    {
       this.config = config;
       this.id = UUID.randomUUID();
       Player one = new Player();
       this.players = new ArrayList<>();
       this.players.add(one);
       this.subscribers = new HashSet<>();
    }


    public void subscribe(SessionListener subscriber) {
        this.subscribers.add(subscriber);
    }

    public SessionStatus getStatus() {
        return this.status;
    }

    public UUID getId() {
        return this.id;
    }

    public UUID getInitiator() {
        return this.players.get(0).getId();
    }

    public UUID enqueuePlayer() {
        Player player = new Player();
        this.players.add(player);
        if(shouldStart()) {
            this.status = SessionStatus.IN_PROGRESS;
            this.notifySubscribers(this);
        }

        return player.getId();
    }

    abstract void notifySubscribers(Session session);

    abstract boolean shouldStart();
}

