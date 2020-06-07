package it.polimi.ingsw.model.simplegod;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.messageModel.MessageType;
import it.polimi.ingsw.utils.PlayerMessage;

import java.util.List;

public class Poseidon  extends GodCard {
    private int counter=0;
    private boolean usedPower;
    private Worker usedWorker;
    /**
     * {@inheritDoc}
     */
    public Poseidon() {
        super(Gods.POSEIDON, Phase.BUILD);
        counter=0;
        usedPower=false;
    }
    public boolean hasUsedPower(){
        return this.usedPower;
    }

    public int getNumOfBuild(){
        return this.counter;
    }

    public void setUsedWorker(Worker worker){
        this.usedWorker=worker;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void usePower(List<Object> objectList) {
        this.usedPower=true;
        counter++;
        Model model = (Model)objectList.get(0);
        model.setNextPhase(getPhase());
        model.setNextPlayerMessage(PlayerMessage.BUILD);
        model.setNextMessageType(MessageType.BUILD);
        model.notifyChanges();
    }
}
