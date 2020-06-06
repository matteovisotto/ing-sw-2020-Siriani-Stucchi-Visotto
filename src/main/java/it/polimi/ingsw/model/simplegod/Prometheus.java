
package it.polimi.ingsw.model.simplegod;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.messageModel.MessageType;
import it.polimi.ingsw.utils.PlayerMessage;

import java.util.List;
/**
 This class is intended to represent the Prometheus's GodCard
 */
public class Prometheus extends GodCard {
    private boolean built = false; //se ha usato il potere
    private boolean usedPower;
    private int workerID = 0;

    /**
     * {@inheritDoc}
     */
    public Prometheus() {
        super(Gods.PROMETHEUS, Phase.PROMETHEUS_WORKER);
    }

    /**
     * @return a boolean that is true if the player has already used his power (the player has arrived to the second build phase).
     */
    public boolean hasBuilt() {
        return built;
    }

    /**
     * @param built is set to true if the player build using his power.
     */
    public void setBuild(boolean built){
        this.built = built;
    }

    /**
     * This method makes a player build; it could be used only if the player decide to activate his power.
     * @param objectList contain the model of the actual game (objectList.get(0)).
     */
    @Override
    public void usePower(List<Object> objectList) {
        usedPower = true;
        Model model = (Model)objectList.get(0);
        model.setNextPhase(getPhase());
        model.setNextPlayerMessage(PlayerMessage.PROMETHEUS_ASK_WORKER);
        model.setNextMessageType(MessageType.PROMETHEUS);
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

    /**
     * @return an int that represent the worker selected by the player to make the first built.
     */
    public int getWorkerID() {
        return workerID;
    }

    /**
     * @param workerID is the worker selected by the player to make the first built.
     */
    public void setWorkerID(int workerID) {
        this.workerID = workerID;
    }
}
