package it.polimi.ingsw.model.simplegod;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.messageModel.MessageType;
import it.polimi.ingsw.utils.PlayerMessage;

import java.util.List;

public class Poseidon  extends GodCard {
    private int counter=0;
    private Worker unusedWorker=null;
    private Worker movedWorker=null;
    /**
     * {@inheritDoc}
     */
    public Poseidon() {
        super(Gods.POSEIDON, Phase.BUILD);
        counter=0;
    }

    public int getNumOfBuild(){
        return this.counter;
    }

    public void resetBuild(){
        counter=0;
    }

    public void setUnusedWorker(Worker worker){
        this.unusedWorker =worker;
    }

    public Worker getUnusedWorker(){
        return this.unusedWorker;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void usePower(List<Object> objectList) {
        counter++;
        Model model = (Model)objectList.get(0);
        model.setNextPhase(getPhase());
        model.setNextPlayerMessage(PlayerMessage.BUILD);
        model.setNextMessageType(MessageType.BUILD);
        model.notifyChanges();
    }

    public Worker getMovedWorker() {
        return movedWorker;
    }

    public void setMovedWorker(Worker movedWorker) {
        this.movedWorker = movedWorker;
    }
}
