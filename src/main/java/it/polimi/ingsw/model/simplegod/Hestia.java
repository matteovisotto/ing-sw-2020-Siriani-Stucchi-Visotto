package it.polimi.ingsw.model.simplegod;

import it.polimi.ingsw.model.GodCard;
import it.polimi.ingsw.model.Gods;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.Phase;
import it.polimi.ingsw.model.messageModel.MessageType;
import it.polimi.ingsw.utils.PlayerMessage;

import java.util.List;

public class Hestia extends GodCard {
    private boolean hasBuilt=false;
    /**
     * {@inheritDoc}
     */
    public Hestia() {
        super(Gods.HESTIA, Phase.BUILD);
    }

    public void setHasBuilt(boolean hasBuilt){
        this.hasBuilt=hasBuilt;
    }

    public boolean hasBuilt(){
        return this.hasBuilt;
    }

    /**
     * {@inheritDoc}
     * In this case the only parameters is the model,
     * If this function is called, let the player to build the first time and set te model params
     * for letting him to build again
     */
    @Override
    public void usePower(List<Object> objectList) {
        this.hasBuilt=true;
        Model model = (Model)objectList.get(0);
        model.setNextPhase(getPhase());
        model.setNextPlayerMessage(PlayerMessage.BUILD);
        model.setNextMessageType(MessageType.BUILD);
        model.notifyChanges();
    }
}
