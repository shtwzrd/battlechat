package edu.dirtybit.battlechat.exceptions;

public class FiringAtOwnBoardException extends Exception {

    public FiringAtOwnBoardException(int index) {
        super(String.format("Client attempted to fire at board with index %d, but that is their own board", index));
    }
}
