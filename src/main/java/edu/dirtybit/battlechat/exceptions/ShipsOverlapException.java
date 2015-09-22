package edu.dirtybit.battlechat.exceptions;

import edu.dirtybit.battlechat.model.Ship;

public class ShipsOverlapException extends Exception {

    public ShipsOverlapException(int x, int y) {
        super("Two ships overlapped at: (\"" + x + "\",\"" + y + "\")");
    }
}
