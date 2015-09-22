package edu.dirtybit.battlechat;

import edu.dirtybit.battlechat.model.Player;

import java.util.*;

public abstract class Session {
    private GameConfiguration config;
    private UUID id;
    private List<Player> players;
    private Set<SessionListener> subscribers;
    protected SessionStatus status;

    public Session(GameConfiguration config, Player player)
    {
       this.config = config;
       this.id = UUID.randomUUID();
       this.players = new ArrayList<>();
       this.players.add(player);
       this.subscribers = new HashSet<>();
       this.status = SessionStatus.ENQUEUED;
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

    public UUID enqueuePlayer(Player player) {
        this.players.add(player);
        if(shouldStart()) {
            this.status = SessionStatus.IN_PROGRESS;
            this.notifySubscribers();
        }

        return player.getId();
    }

    public List<Player> getPlayers() {
        return this.players;
    }

    public Player getPlayerByName(String name) {
       for(Player p : this.players) {
           if(p.getGivenName().equals(name)) {
               return p;
           }
        }
        return null;
    }

    public Player getPlayerById(UUID id) {
       for(Player p : this.players) {
           if(p.getId().equals(id)) {
               return p;
           }
        }
        return null;
    }

    public Set<SessionListener> getSubscribers() {
        return this.subscribers;
    }

    public GameConfiguration getConfig() {
        return this.config;
    }

    public void notifySubscribers() {
       this.subscribers.forEach(x -> x.notifySubscriber(this));
    }

    public abstract boolean shouldStart();
}

