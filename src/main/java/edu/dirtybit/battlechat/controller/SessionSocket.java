package edu.dirtybit.battlechat.controller;

import edu.dirtybit.battlechat.model.GameMessage;

import java.io.IOException;

public interface SessionSocket {
    void sendMessage(GameMessage message) throws IOException;
}
