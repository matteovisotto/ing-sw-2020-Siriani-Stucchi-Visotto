/**
 This class is intended to represent the Hephaestus's GodCard
 */
package it.polimi.ingsw.model.simplegod;

import it.polimi.ingsw.model.*;

import java.util.List;

public class Hephaestus extends GodCard {
    private Cell firstBuilt;
    private boolean moved = false;
    private boolean built = false;
    private final Phase phase = Phase.BUILD;
    public Hephaestus() {
        super(SimpleGods.HEPHAESTUS);
    }

    public Cell getFirstBuilt() {
        return firstBuilt;
    }

    public void setFirstBuilt(Cell firstBuilt) {
        this.firstBuilt = firstBuilt;
    }

    public boolean isMoved() {
        return moved;
    }

    public void hasMoved(boolean moved) {

    }

    public boolean hasBuilt() {
        return built;
    }

    public Phase getPhase() {
        return phase;
    }

    @Override
    public void usePower(List<Object> objectList) {
        Model model = (Model)objectList.get(0);
        model.increaseLevel(firstBuilt, Blocks.getBlock(firstBuilt.getLevel().getBlockId()+1));
    }
    @Override
    public void reset() {

    }
}
