package edu.dirtybit.battlechat;

public class MockSessionListener implements SessionListener  {

    public int notificationCount;
    public MockSessionListener() {
        this.notificationCount = 0;
    }

    @Override
    public void notifySubscriber(Session obj) {
        this.notificationCount++;
    }
}
