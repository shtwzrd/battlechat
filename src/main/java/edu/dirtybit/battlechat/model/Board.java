package edu.dirtybit.battlechat.model;

import edu.dirtybit.battlechat.BattleShipConfiguration;
import edu.dirtybit.battlechat.BattleShipConfiguration.ConfigKeys;

public class Board extends BaseBoard {
    private transient Perspective perspective;
    private Fleet fleet;
    private transient boolean cleared;
    private int lives;

    public Board(BattleShipConfiguration config) {
        super(config.getPropertyAsInt(ConfigKeys.GRID_WIDTH), config.getPropertyAsInt(ConfigKeys.GRID_HEIGHT));
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
        //if (celltype == CellType.Hit) { this.lives--; }

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
            this.lives = this.countLives();
        }
        return lives;
    }

    private int countLives() {
        int livesinships = 0;
        for (Ship s : this.getFleet().getShips()) {
            lives = s.getHealth();
        }

        int livesonboard = 0;
        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.width; y++) {
                if (this.cells[x][y] == CellType.Ship) {
                    this.lives++;
                }
            }
        }

        if (livesinships != livesonboard) {
            // try to resync, if ever fleet and board lives should go out of sync
            for (int x = 0; x < this.width; x++) {
                for (int y = 0; y < this.width; y++) {
                    if (this.cells[x][y] == CellType.Hit) {
                        this.getFleet().getShipAt(x, y).removeCell(x, y);
                    }
                }
            }
        }

        return livesinships;
    }
}
