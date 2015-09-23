package edu.dirtybit.battlechat.model;

import edu.dirtybit.battlechat.GameConfiguration;

import java.util.UUID;

public class Player {
    private UUID uuid;
    private String givenName;

    public Player(String givenName)
    {
        this.uuid = UUID.randomUUID();
        this.givenName = givenName;
    }

    public UUID getId() {
        return uuid;
    }

    public String getGivenName() {
        return this.givenName;
    }
}
