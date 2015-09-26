package edu.dirtybit.battlechat.model;

import java.util.ArrayList;

public class BoardUpdate {
    private ArrayList<BaseBoard> boards;
    private int yourBoardIndex;

    public BoardUpdate(ArrayList<BaseBoard> boards, int playersIndex) {
        this.boards = boards;
        this.yourBoardIndex = playersIndex;
    }
}
