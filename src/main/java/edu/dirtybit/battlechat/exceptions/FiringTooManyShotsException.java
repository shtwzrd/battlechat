package edu.dirtybit.battlechat.exceptions;

public class FiringTooManyShotsException extends Exception {

    public FiringTooManyShotsException(int shotsFired, int shotsAllowed) {
        super(String.format("Client attempted to fire %d shot(s) but is only allowed %d", shotsFired, shotsAllowed));
    }
}
