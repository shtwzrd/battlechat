package edu.dirtybit.battlechat.model;

public class BattleChatStatus {
    private int secondsToPhaseChange;
    private Phase gamePhase;

    public int getSecondsToPhaseChange() {
        return secondsToPhaseChange;
    }

    public void setSecondsToPhaseChange(int secondsToPhaseChange) {
        this.secondsToPhaseChange = secondsToPhaseChange;
    }

    public Phase getGamePhase() {
        return gamePhase;
    }

    public void setGamePhase(Phase gamePhase) {
        this.gamePhase = gamePhase;
    }


    public enum Phase {
        WAITING_FOR_OPPONENT,
        PLACEMENT_PHASE,
        YOU_FIRING,
        OPPONENT_FIRING,
        YOU_WIN,
        YOU_LOSE
    }
}
