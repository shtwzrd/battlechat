package edu.dirtybit.battlechat;

import edu.dirtybit.battlechat.controller.SessionSocket;
import edu.dirtybit.battlechat.model.GameMessage;

import java.io.IOException;

public class MockSessionSocket implements SessionSocket {
    public boolean sentMessage = false;
    @Override
    public void sendMessage(GameMessage message) throws IOException {
       this.sentMessage = true;
    }
}
