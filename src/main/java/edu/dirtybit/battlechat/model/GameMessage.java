package edu.dirtybit.battlechat.model;

import java.util.UUID;

public class GameMessage<T> {

    private GameMessageType messageType;
    private UUID id;
    private T body;

    public GameMessage(GameMessageType type, UUID id, T body) {
        this.messageType = type;
        this.id = id;
        this.body = body;
    }

    public GameMessageType getMessageType() {
        return this.messageType;
    }

    public T getBody() {
        return this.body;
    }

    public UUID getId() {
        return this.id;
    }
}
