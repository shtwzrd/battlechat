package edu.dirtybit.battlechat;

public interface GameConfiguration {
    String getProperty(String key);
    int getPropertyAsInt(String key);
}
