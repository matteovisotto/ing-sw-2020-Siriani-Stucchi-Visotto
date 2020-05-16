
package it.polimi.ingsw.model.simplegod;

import it.polimi.ingsw.model.*;

import java.util.List;
/**
 This class is intended to represent the Demeter's GodCard
 */
public class Demeter extends GodCard {
    private Cell firstBuilt;
    private boolean moved = false;
    private boolean built = false;
    public Demeter() {
        super(SimpleGods.DEMETER, Phase. BUILD);
    }

    /**
     * This method is used to get the first cell built by the player in this turn.
     * @return the first built cell.
     */
    public Cell getFirstBuilt() {
        return firstBuilt;
    }

    /**
     * This method is used to set the first building's cell built by the player.
     * @param firstBuilt represent the first cell built by the player in this turn.
     */
    public void setFirstBuilt(Cell firstBuilt) {
        this.firstBuilt = firstBuilt;
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

    public Phase getPhase() {
        return phase;
    }

    /**
     * This method makes a player's worker build another time, but not on the same position; it could be used only if the player decide to activate his power.
     * @param objectList contain the model of the actual game (objectList.get(0)) and the cell in which it will be built another time.
     * @see Model {@link Model} In the model is contained the increaseLevel Method.
     */
    @Override
    public void usePower(List<Object> objectList) {
        Model model = (Model)objectList.get(0);
        Cell cell = (Cell)objectList.get(1);
        model.increaseLevel(cell, Blocks.getBlock(cell.getLevel().getBlockId()+1)); //mi sa che va meglio se si implementa con PlayerBuilt
    }
    @Override
    public void reset() {
        this.moved = false;
    }
}
