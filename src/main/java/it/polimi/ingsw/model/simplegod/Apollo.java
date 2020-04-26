
package it.polimi.ingsw.model.simplegod;

import it.polimi.ingsw.model.*;

import java.util.List;
/**
 This class is intended to represent the Apollo's GodCard
 */
public class Apollo extends GodCard {
    private final Phase phase = Phase.MOVE;
    private Cell cell;
    private boolean built = false;
    private boolean moved = false;
    public Apollo(){
        super(SimpleGods.APOLLO);
    }

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

    }

    public boolean hasBuilt() {
        return built;
    }

    /**
     * This method switches the position of two different workers; it could be used only if the player decide to activate his power
     * @param objectList contain the positions of the two different workers; objectList.get(0) = Apollo's worker; objectList.get(1) = other worker
     */
    @Override
    public void usePower(List<Object> objectList) {
        Worker w1 = (Worker)objectList.get(0);
        Worker w2 = (Worker)objectList.get(1);
        Cell c;
        //scambia le celle dei worker
        c = w2.getCell();
        w2.setCell(w1.getCell());
        w1.setCell(c);
    }

    @Override
    public void reset() {

    }
}
