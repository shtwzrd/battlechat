package edu.dirtybit.battlechat;

import edu.dirtybit.battlechat.model.GameMessage;

public class MockSessionListener implements SessionListener  {

    public int notificationCount;
    public GameMessage lastMessageReceived;
    public MockSessionListener() {
        this.notificationCount = 0;
    }

    @Override
    public void notifySubscriber(Session obj, GameMessage msg) {
        this.notificationCount++;
        this.lastMessageReceived = msg;
    }
}
