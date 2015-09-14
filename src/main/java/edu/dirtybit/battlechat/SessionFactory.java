package edu.dirtybit.battlechat;

import edu.dirtybit.battlechat.model.GameState;

public enum SessionFactory {
    INSTANCE;
    private SessionFactory instance;

    public static SessionFactory getInstance() {
        return INSTANCE.instance;
    }

    public Session createSession(GameConfiguration config) {
      try {
          return createSessionInternal(config);
      } catch (Exception e) {
          e.printStackTrace();
      }
      return null;
    }

    private Session createSessionInternal(GameConfiguration config) throws Exception {
        if (ConfigurationProvider.SessionType.getName().equals((GameState.class.getName()))) {
            return new GameState(config);
        } else {
            throw new Exception("Bad Configuration: unknown Session Type");
        }
    }


}
