package edu.dirtybit.battlechat;

import edu.dirtybit.battlechat.model.GameMessage;

public interface LobbyListener {
    void recieveMessage(GameMessage message);
}
