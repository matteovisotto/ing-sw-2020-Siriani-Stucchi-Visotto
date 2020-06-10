package it.polimi.ingsw.model.simplegod;

import it.polimi.ingsw.model.GodCard;
import it.polimi.ingsw.model.Gods;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.Phase;
import it.polimi.ingsw.model.messageModel.MessageType;
import it.polimi.ingsw.utils.PlayerMessage;

import java.util.List;

public class Triton extends GodCard {
    private int usedWorkerID =-1;
    /**
     * {@inheritDoc}
     */
    public Triton() {
        super(Gods.TRITON, Phase.MOVE);
    }

    public int getUsedWorkerID(){
        return this.usedWorkerID;
    }
    public void setUsedWorkerID(int worker){
        this.usedWorkerID =worker;
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public void usePower(List<Object> objectList) {
        Model model = (Model)objectList.get(0);
        model.setNextPhase(getPhase());
        model.setNextPlayerMessage(PlayerMessage.MOVE);
        model.setNextMessageType(MessageType.MOVE);
        model.notifyChanges();
    }
}
