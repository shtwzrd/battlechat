package edu.dirtybit.battlechat.model;

import edu.dirtybit.battlechat.BattleShipConfiguration;
import edu.dirtybit.battlechat.GameConfiguration;

public class Board extends BaseBoard {
    private Perspective perspective;
    private Fleet fleet;
    private int lives;
    private boolean cleared;

    public Board(GameConfiguration config) {
        super(Integer.parseInt(config.getProperty(BattleShipConfiguration.ConfigKeys.GRID_WIDTH.toString())), Integer.parseInt(config.getProperty(BattleShipConfiguration.ConfigKeys.GRID_HEIGHT.toString())));
        this.fleet = Fleet.fromConfig(config);
        this.perspective = new Perspective(this.width, this.height);
        this.clear();
        this.cleared = true;
    }

    @Override
    public void setCell(int x, int y, CellType celltype) {
        super.setCell(x, y, celltype);
        // it is technically possible to decrement the remaining lives by setting the same cell to Hit over and over, consider revising.
        // an "inelegant" solution has been implemented, it checks for any remaining Ship cells on the board when the lives are supposedly zero.
        if (celltype == CellType.Hit) { this.lives--; }
        this.perspective.setCell(x, y, celltype);
    }

    public Perspective getPerspective() {
        return this.perspective;
    }

    public Fleet getFleet() {
        return fleet;
    }

    public void setFleet(Fleet fleet) {
        this.fleet = fleet;
    }

    @Override
    public void clear() {
        super.clear();
        this.perspective.clear();
        this.cleared = true;
    }

    public boolean isClear() {
        return this.cleared;
    }

    public int getLivesRemaining() {
        // if no remaining lives, double check that all the ships have been destroyed.
        if (this.lives == 0) {
            for (int x = 0; x < this.width; x++) {
                for (int y = 0; y < this.width; y++) {
                    if (this.cells[x][y] == CellType.Ship) {
                        this.lives++;
                    }
                }
            }
        }
        return lives;
    }
}
