
package it.polimi.ingsw.model.simplegod;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.messageModel.MessageType;
import it.polimi.ingsw.utils.PlayerMessage;

import java.util.List;
/**
 This class is intended to represent the Atlas's GodCard
 */
public class Atlas extends GodCard {
    public Atlas() {
        super(Gods.ATLAS, Phase.MOVE);
    }
    private Cell cell;
    private boolean moved = false;
    private boolean built = false;
    private boolean usedPower = false;

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
     * This method makes a player's worker build a DOME at any level; it could be used only if the player decide to activate his power.
     * @param objectList contain the cell in which it will be built the DOME (objectList.get(0)).
     */
    @Override
    public void usePower(List<Object> objectList) {
        usedPower = true;
        Model model = (Model)objectList.get(0);
        model.setNextPhase(getPhase());
        model.setNextPlayerMessage(PlayerMessage.BUILD);
        model.setNextMessageType(MessageType.BUILD);
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
}
