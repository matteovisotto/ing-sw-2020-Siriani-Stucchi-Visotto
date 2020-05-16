
package it.polimi.ingsw.model.simplegod;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.messageModel.MessageType;
import it.polimi.ingsw.model.messageModel.PlayerMove;
import it.polimi.ingsw.utils.PlayerMessage;

import java.util.List;
/**
 This class is intended to represent the Arthemis's GodCard
 */
public class Arthemis extends GodCard {
    private Cell firstBuilt;
    private boolean moved = false;
    private boolean built = false;
    private boolean usedPower;
    private Worker previousWorker;

    public Arthemis() {
        super(SimpleGods.ARTHEMIS, Phase.MOVE);
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
        usedPower=true;
        Model model=(Model)objectList.get(0);
        model.setNextPhase(getPhase());
        model.setNextPlayerMessage(PlayerMessage.MOVE);
        model.setNextMessageType(MessageType.MOVE);
        model.notifyChanges();
    }
    @Override
    public void reset() {
        this.moved = false;
    }

    public boolean hasUsedPower() {
        return usedPower;
    }

    public void setUsedPower(boolean usedPower) {
        this.usedPower = usedPower;
    }

    public Worker getPreviousWorker() {
        return previousWorker;
    }

    public void setPreviousWorker(Worker previousWorker) {
        this.previousWorker = previousWorker;
    }
}
