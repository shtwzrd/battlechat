package edu.dirtybit.battlechat;
import edu.dirtybit.battlechat.model.GameMessage;

public interface SessionListener {

    void notifySubscriber(Session obj, GameMessage msg);
}
