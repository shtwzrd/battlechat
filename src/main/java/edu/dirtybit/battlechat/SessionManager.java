package edu.dirtybit.battlechat;

import edu.dirtybit.battlechat.controller.BattleChatSessionSocket;
import edu.dirtybit.battlechat.model.GameMessage;
import edu.dirtybit.battlechat.model.GameMessageType;
import edu.dirtybit.battlechat.model.Player;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

public class SessionManager implements SessionListener, LobbyListener {
    private ConcurrentMap<UUID, UUID> playerToSession;
    private ConcurrentMap<UUID, Session> sessions;
    private ConcurrentMap<UUID, BattleChatSessionSocket> sockets;
    private ConcurrentLinkedQueue<Session> queue;
    private BattleShipConfiguration defaultConfig = new BattleShipConfiguration();

    public SessionManager() {
        this.sessions = new ConcurrentHashMap<>();
        this.sockets = new ConcurrentHashMap<>();
        this.playerToSession = new ConcurrentHashMap<>();
        this.queue = new ConcurrentLinkedQueue<>();
        Lobby.INSTANCE.subscribe(this);
    }

    public Session getSessionContainingPlayer(UUID player) {
        UUID lookup = this.playerToSession.get(player);
        if(lookup == null) {
            return null;
        } else {
            return this.sessions.get(lookup);
        }
    }

    public Session getSession(UUID session) {
        return this.sessions.get(session);
    }

    public UUID enterQueue(Player player) throws Exception {
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

    @Override
    public void receiveMessage(GameMessage message) {
        if(message.getMessageType() == GameMessageType.CHAT) {
            Session s = this.getSessionContainingPlayer(message.getId());
            String sender = s.getPlayerById(message.getId()).getGivenName();
            s.getPlayers().forEach(p -> {
                Lobby.INSTANCE.message(new GameMessage(GameMessageType.CHAT,
                        p.getId(), String.format("%s: %s \n", sender, message.getBody())));
                });
            }
    }

    private UUID addToNewGame(Player player) throws Exception {
       Session newGame = SessionFactory.INSTANCE.createSession(defaultConfig, player);
       newGame.subscribe(this);
       this.sessions.put(newGame.getId(), newGame);
       this.playerToSession.put(newGame.getInitiator(), newGame.getId());
       this.queue.add(newGame);
       return newGame.getInitiator();
    }

    private UUID addToExisting(Session session, Player player) {
       this.playerToSession.put(player.getId(), session.getId());
       return session.enqueuePlayer(player);
    }


}
