package edu.dirtybit.battlechat.model;

import edu.dirtybit.battlechat.BattleShipConfiguration;
import edu.dirtybit.battlechat.GameConfiguration;

import java.util.ArrayList;

public class Board extends BaseBoard
{
    private Perspective perspective;
    private Fleet fleet;
    private boolean cleared;

    public Board(GameConfiguration config)
    {
        super(Integer.parseInt(config.getProperty(BattleShipConfiguration.ConfigKeys.GRID_WIDTH.toString())), Integer.parseInt(config.getProperty(BattleShipConfiguration.ConfigKeys.GRID_HEIGHT.toString())));
        this.fleet = Fleet.fromConfig(config);
        this.perspective = new Perspective(this.width, this.height);
        this.clear();
        this.cleared = true;
    }

    @Override
    public void setCell(int x, int y, CellType celltype)
    {
        super.setCell(x, y, celltype);
        this.perspective.setCell(x, y, celltype);
    }

    public Perspective getPerspective()
    {
        return this.perspective;
    }

    public Fleet getFleet() {
        return fleet;
    }

    public void setFleet(Fleet fleet) {
        this.fleet = fleet;
    }

    @Override
    public void clear()
    {
        super.clear();
        this.perspective.clear();
        this.cleared = true;
    }

    public boolean isClear() {
        return this.cleared;
    }
}
