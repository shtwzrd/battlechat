package edu.dirtybit.battlechat.model;

public class BattleChatStatus {
    private int secondsToPhaseChange;
    private Phase gamePhase;

    public BattleChatStatus(Phase phase, int time) {
        this.gamePhase = phase;
        this.secondsToPhaseChange = time;
    }

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
        NOT_STARTED,
        PLACEMENT_PHASE,
        COMBAT,
        YOU_FIRING,
        OPPONENT_FIRING,
        COMPLETED,
        YOU_WIN,
        YOU_LOSE
    }
}
