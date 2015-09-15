package edu.dirtybit.battlechat.model;

import java.util.UUID;

public class Player {
    private UUID uuid;
    private Fleet fleet;
    private String givenName;

    public Player(String givenName)
    {
        uuid = UUID.randomUUID();
        fleet = new Fleet();
        this.givenName = givenName;
    }

    public UUID getId() {
        return uuid;
    }

    public Fleet getFleet() {
        return fleet;
    }

    public void setFleet(Fleet fleet) {
        this.fleet = fleet;
    }

    public String getGivenName() {
        return this.givenName;
    }
}
