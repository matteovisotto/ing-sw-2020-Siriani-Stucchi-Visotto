
package it.polimi.ingsw.model.simplegod;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.messageModel.PlayerMove;

import java.util.List;
/**
 This class is intended to represent the Arthemis's GodCard
 */
public class Arthemis extends GodCard {
    private final Phase phase = Phase.MOVE;
    private Cell firstBuilt;
    private boolean moved = false;
    private boolean built = false;
    public Arthemis() {
        super(SimpleGods.ARTHEMIS);
    }

    /**
     * This method is used to see if the player has already moved.
     * @return a boolean: true -> the player has moved; false -> the player has yet to move.
     */
    public boolean isMoved() {
        return moved;
    }

    /**
     * This method is used to set if the player has moved.
     */
    public void hasMoved(boolean moved) {
        this.moved = moved;
    }

    public Cell getFirstBuilt(){
        return firstBuilt;
    }

    public void setFirstBuilt(Cell firstBuilt){
        this.firstBuilt = firstBuilt;
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
     * This method moves the player; it could be used only if the player decide to activate his power.
     * @param objectList contain the model of the actual game (objectList.get(0)) and the class to make the player move objectList.get(1).
     * @see PlayerMove {@link PlayerMove}
     */
    @Override
    public void usePower(List<Object> objectList) {
        Model model = (Model)objectList.get(0);
        model.move((PlayerMove) objectList.get(1));
    }
    @Override
    public void reset() {
        this.moved = false;
    }
}
