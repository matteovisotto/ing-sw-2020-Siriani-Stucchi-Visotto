
package it.polimi.ingsw.model.simplegod;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.messageModel.MessageType;
import it.polimi.ingsw.utils.PlayerMessage;

import java.util.List;
/**
 This class is intended to represent the Atlas's GodCard
 */
public class Atlas extends GodCard {
    private boolean usedPower = false;

    public Atlas() {
        super(Gods.ATLAS, Phase.MOVE);
    }

    public Phase getPhase() {
        return phase;
    }

    /**
     * This method makes a player's worker build a DOME at any level; it could be used only if the player decide to activate his power.
     * @param objectList contain the cell in which it will be built the DOME (objectList.get(0)).
     */
    @Override
    public void usePower(List<Object> objectList) {
        usedPower = true;
        Model model = (Model)objectList.get(0);
        model.setNextPhase(Phase.next(getPhase()));
        model.setNextPlayerMessage(PlayerMessage.BUILD);
        model.setNextMessageType(MessageType.BUILD);
        model.notifyChanges();
    }

    public boolean hasUsedPower() {
        return usedPower;
    }

    public void setUsedPower(boolean usedPower) {
        this.usedPower = usedPower;
    }
}
