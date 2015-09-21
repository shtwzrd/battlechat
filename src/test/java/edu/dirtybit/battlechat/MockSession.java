package edu.dirtybit.battlechat;

import edu.dirtybit.battlechat.model.Player;

public class MockSession extends Session {

    public MockSession(GameConfiguration config, Player player) {
        super(config, player);
    }

    @Override
    public boolean shouldStart() {
        return true;
    }

    @Override
    public void run() {
        // TODO
    }
}
