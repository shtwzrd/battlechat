package edu.dirtybit.battlechat;

import edu.dirtybit.battlechat.model.GameState;
import edu.dirtybit.battlechat.model.Player;

public enum SessionFactory {
    INSTANCE;

    public Session createSession(GameConfiguration config, Player player) {
      try {
          return createSessionInternal(config, player);
      } catch (Exception e) {
          e.printStackTrace();
          return null;
      }
    }

    private Session createSessionInternal(GameConfiguration config, Player player) throws Exception {
        if (ConfigurationProvider.SessionType.getName().equals((GameState.class.getName()))) {
            return new GameState(config, player);
        } else {
            throw new Exception("Bad Configuration: unknown Session Type");
        }
    }
}
