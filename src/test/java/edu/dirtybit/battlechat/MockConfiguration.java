package edu.dirtybit.battlechat;

public class MockConfiguration implements GameConfiguration {

    public MockConfiguration()
    {

    }

    @Override
    public String getProperty(String key) {
        return key;
    }
}
