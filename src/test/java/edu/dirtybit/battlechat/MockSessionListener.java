package edu.dirtybit.battlechat;

import edu.dirtybit.battlechat.model.GameMessage;
import edu.dirtybit.battlechat.model.GameMessageType;

import java.util.ArrayDeque;
import java.util.Queue;

public class MockSessionListener implements SessionListener  {

    public Queue<GameMessage> messagesRecieved;
    public int notificationCount;
    public MockSessionListener() {
        this.notificationCount = 0;
        messagesRecieved = new ArrayDeque<>();
    }

    @Override
    public void notifySubscriber(Session obj, GameMessage msg) {
        this.notificationCount++;
        this.messagesRecieved.offer(msg);
    }

    public boolean containsMessageType(GameMessageType type) {
        for(GameMessage m : this.messagesRecieved) {
            if (m.getMessageType() == type) return true;
        }
        return false;
    }

    public GameMessage getFirstMessageByType(GameMessageType type) {
         for(GameMessage m : this.messagesRecieved) {
            if (m.getMessageType() == type) return m;
        }
        return null;
    }
}