package edu.dirtybit.battlechat.model;

import java.util.UUID;

public class Player {
    private UUID uuid;
    private Fleet fleet;

    public Player()
    {
        uuid = UUID.randomUUID();
        fleet = new Fleet();
    }

    public UUID getUuid() {
        return uuid;
    }

    /*public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }*/

    public Fleet getFleet() {
        return fleet;
    }

    public void setFleet(Fleet fleet) {
        this.fleet = fleet;
    }
}
