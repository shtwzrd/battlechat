package edu.dirtybit.battlechat.exceptions;

public class FiringOutOfTurnException extends Exception {

    public FiringOutOfTurnException() {
        super("Client attempted to fire a shot but it wasn't that player's turn.");
    }
}
