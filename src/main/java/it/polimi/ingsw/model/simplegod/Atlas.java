
package it.polimi.ingsw.model.simplegod;

import it.polimi.ingsw.model.*;

import java.util.List;
/**
 This class is intended to represent the Atlas's GodCard
 */
public class Atlas extends GodCard {
    private final Phase phase = Phase.BUILD;
    public Atlas() {
        super(SimpleGods.ATLAS);
    }
    private Cell cell;
    private boolean moved = false;
    private boolean built = false;

    public Phase getPhase() {
        return phase;
    }

    public Cell getFirstBuilt(){
        return cell;
    }

    public void setFirstBuilt(Cell firstBuilt){

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

    /**
     * This method makes a player's worker build a DOME at any level; it could be used only if the player decide to activate his power.
     * @param objectList In this List is contained the cell in which it will be built the DOME (objectList.get(0)).
     */
    @Override
    public void usePower(List<Object> objectList) {
        Cell cellBuild = (Cell)objectList.get(0);
        cellBuild.setLevel(Blocks.DOME);
    }
    @Override
    public void reset() {

    }
}
