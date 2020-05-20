
package it.polimi.ingsw.model.simplegod;

import it.polimi.ingsw.model.*;

import java.util.List;
/**
 This class is intended to represent the Minotaur's GodCard
 */
public class Minotaur extends GodCard {
    private boolean moved = false;
    private boolean built = false;
    private Cell cell;
    public Minotaur() {
        super(Gods.MINOTAUR, Phase.MOVE);
    }

    public Phase getPhase() {
        return phase;
    }

    public Cell getFirstBuilt(){
        return cell;
    }

    public void setFirstBuilt(Cell firstBuilt){
        this.cell = firstBuilt;
    }
    public boolean isMoved() {
        return moved;
    }

    public void hasMoved(boolean moved) {
        this.moved = moved;
    }

    public boolean hasBuilt() {
        return built;
    }

    public void setBuild(boolean built){
        this.built = built;
    }

    /**
     * This method makes a player's worker move into an enemy worker's cell, forcing him to move one space straight backwards to an unoccupied space at any level; it could be used only if the player decide to activate his power.
     * @param objectList contain the worker of the Minotaur's player (objectList.get(0)), the enemy's worker (objectList.get(1)) and the cell straight backwards of the enemy's worker.
     */
    @Override
    public void usePower(List<Object> objectList) {
        Worker minotaurWorker = (Worker)objectList.get(0);
        Worker enemyWorker = (Worker)objectList.get(1);
        Cell behindCell = (Cell)objectList.get(2);
        Cell c, c2;
        //scambia le celle dei worker
        behindCell.useCell();
        c = enemyWorker.getCell();
        enemyWorker.setCell(behindCell);
        c2 = minotaurWorker.getCell();
        minotaurWorker.setCell(c);
        c2.freeCell();


    }
    @Override
    public void reset() {
        this.moved = false;
    }
}
