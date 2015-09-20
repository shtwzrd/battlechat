package edu.dirtybit.battlechat;

import edu.dirtybit.battlechat.model.GameMessage;

public class MockLobbyListener implements LobbyListener {

    public int callCount = 0;

    @Override
    public void recieveMessage(GameMessage message) {
        this.callCount++;
    }
}
