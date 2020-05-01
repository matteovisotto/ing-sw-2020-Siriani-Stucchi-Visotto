
package it.polimi.ingsw.model.simplegod;

import it.polimi.ingsw.model.*;

import java.util.List;
/**
 This class is intended to represent the Prometheus's GodCard
 */
public class Prometheus extends GodCard {
    private boolean built = false;
    private final Phase phase = Phase.MOVE;//VA CAMBIATO IN BEGINNING
    private boolean moved = false;
    private Cell cell;
    public Prometheus() {
        super(SimpleGods.PROMETHEUS);
    }

    public boolean hasBuilt() {
        return built;
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

    }

    public Phase getPhase() {
        return phase;
    }

    /**
     * This method makes a player build; it could be used only if the player decide to activate his power.
     * @param objectList contain the model of the actual game (objectList.get(0)) and the cell in which it will be built (objectList.get(1)).
     */
    @Override
    public void usePower(List<Object> objectList) {
        Model model = (Model)objectList.get(0);
        Cell cell = (Cell)objectList.get(1);//possibilitá che vada scritto con il PlayerBuilt
        model.increaseLevel(cell, Blocks.getBlock(cell.getLevel().getBlockId()+1));
        built = true;
    }

    /**
     * This method set to false the built variable; is used to reset the turn. It represent that a worker build before moving (so Prometheus power has been activated) and the player's connot moved up anymore in this turn.
     */
    @Override
    public void reset() {
        built = false;
    }
}
