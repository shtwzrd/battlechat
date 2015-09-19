package edu.dirtybit.battlechat;

import edu.dirtybit.battlechat.model.GameState;
import edu.dirtybit.battlechat.model.Player;

public enum SessionFactory {
    INSTANCE;

    public Session createSession(GameConfiguration config, Player player) throws Exception {
        return createSession(ConfigurationProvider.SessionType, config, player);
    }

    public Session createSession(Class type, GameConfiguration config, Player player) throws Exception {
        if (type.getName().equals((GameState.class.getName()))) {
            return new GameState(config, player);
        } else {
            throw new Exception("Bad Configuration: unknown Session Type");
        }
    }
}
