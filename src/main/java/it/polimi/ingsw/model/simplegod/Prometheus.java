
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
    private boolean moved = false;
    private Cell cell;
    private boolean usedPower;
    private int workerID = 0;
    public Prometheus() {
        super(Gods.PROMETHEUS, Phase.PROMETHEUS_WORKER);
    }

    public boolean hasBuilt() {
        return built;
    }

    public void setBuild(boolean built){
        this.built = built;
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

    public Phase getPhase() {
        return phase;
    }

    /**
     * This method makes a player build; it could be used only if the player decide to activate his power.
     * @param objectList contain the model of the actual game (objectList.get(0)) and the cell in which it will be built (objectList.get(1)).
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


    public boolean hasUsedPower() {
        return usedPower;
    }

    public void setUsedPower(boolean usedPower) {
        this.usedPower = usedPower;
    }

    /**
     * This method set to false the built variable; is used to reset the turn. It represent that a worker build before moving (so Prometheus power has been activated) and the player's connot moved up anymore in this turn.
     */
    @Override
    public void reset() {
        built = false;
    }

    public int getWorkerID() {
        return workerID;
    }

    public void setWorkerID(int workerID) {
        this.workerID = workerID;
    }
}
