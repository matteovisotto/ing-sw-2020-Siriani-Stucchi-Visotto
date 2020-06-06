
package it.polimi.ingsw.model.simplegod;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.messageModel.MessageType;
import it.polimi.ingsw.utils.PlayerMessage;

import java.util.List;
/**
 This class is intended to represent the Demeter's GodCard
 */
public class Demeter extends GodCard {
    private Cell firstBuilt;
    private boolean usedPower;

    /**
     * {@inheritDoc}
     */
    public Demeter() {
        super(Gods.DEMETER, Phase. BUILD);
    }

    /**
     * This method is used to get the first cell built by the player in this turn.
     * @return the first built cell.
     */
    public Cell getFirstBuild() {
        return firstBuilt;
    }

    /**
     * This method is used to set the first building's cell built by the player.
     * @param firstBuilt represent the first cell built by the player in this turn.
     */
    public void setFirstBuilt(Cell firstBuilt) {
        this.firstBuilt = firstBuilt;
    }

    /**
     * This method makes a player's worker build another time, but not on the same position; it could be used only if the player decide to activate his power.
     * @param objectList contain the model of the actual game (objectList.get(0)).
     * @see Model {@link Model} In the model is contained the increaseLevel Method.
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

    /**
     * @return true if the power has already been used.
     */
    public boolean hasUsedPower() {
        return usedPower;
    }

    /**
     * @param usedPower is a flag that if it's set to true means that the power has already been used.
     */
    public void setUsedPower(boolean usedPower) {
        this.usedPower = usedPower;
    }
}
