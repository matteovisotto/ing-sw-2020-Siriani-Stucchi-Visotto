package it.polimi.ingsw.model.messageModel;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.simplegod.Atlas;
import it.polimi.ingsw.utils.PlayerMessage;
import it.polimi.ingsw.view.View;

public class UseGodPower extends Message {
    char c;
    public UseGodPower(Player player, View view, char c) {
        super(player, view);
        this.c=c;
    }

    @Override
    public void handler(Controller controller) {
        Model model=controller.getModel();
        GodCard godCard= player.getGodCard();
        if(c=='y'){
            switch(player.getGodCard().getCardGod()){
                case ATLAS:
                    godCard.usePower(null);
            }

        }
        else{
            model.setNextPhase(Phase.next(godCard.getPhase()));
            switch(godCard.getPhase()){
                case MOVE:
                    model.setNextPlayerMessage(PlayerMessage.BUILD);
                    model.setNextMessageType(MessageType.BUILD);
                    model.notifyChanges();
                case BUILD:
                    model.setNextPlayerMessage(PlayerMessage.MOVE);
                    model.setNextMessageType(MessageType.MOVE);
                    model.updateTurn();
                    model.notifyChanges();
            }
        }
    }
}
