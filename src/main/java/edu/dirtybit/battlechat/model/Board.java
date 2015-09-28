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
        this.lives = this.countLives();
    }

    @Override
    public void setCell(int x, int y, CellType celltype) {
        super.setCell(x, y, celltype);
        // it is technically possible to decrement the remaining lives by setting the same cell to HIT over and over, consider revising.
        // an "inelegant" solution has been implemented, it checks for any remaining SHIP cells on the board when the lives are supposedly zero.
        //if (celltype == CellType.HIT) { this.lives--; }

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
           return this.countLives();
    }

    private int countLives() {
        int livesinships = 0;
        for (Ship s : this.getFleet().getShips()) {
            livesinships += s.getHealth();
        }

        int livesonboard = 0;
        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
                if (this.cells[x][y] == CellType.SHIP) {
                    livesonboard++;
                }
            }
        }

        if (livesinships != livesonboard) {
            // try to resync, if ever fleet and board lives should go out of sync
            for (int x = 0; x < this.width; x++) {
                for (int y = 0; y < this.height; y++) {
                    if (this.cells[x][y] == CellType.HIT) {
                        Ship s = this.getFleet().getShipAt(x,y);
                        if (s != null) {
                            s.removeCell(x,y);
                        }
                    }
                }
            }
        }

        return livesinships;
    }
}
