package it.polimi.ingsw.model.messageModel;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.simplegod.Atlas;
import it.polimi.ingsw.utils.PlayerMessage;
import it.polimi.ingsw.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class UseGodPower extends Message {
    char usePower;
    public UseGodPower(Player player, View view, char usePower) {
        super(player, view);
        this.usePower = usePower;
    }

    @Override
    public void handler(Controller controller) {
        Model model = controller.getModel();
        GodCard playerGodCard = player.getGodCard();
        if(usePower == 'y'){
            playerGodCard.usePower(new ArrayList<Object>(Collections.singletonList(model)));
        }
        else{

            switch(playerGodCard.getPhase()){
                case MOVE:
                    model.setNextPhase(Phase.BUILD);
                    model.setNextPlayerMessage(PlayerMessage.BUILD);
                    model.setNextMessageType(MessageType.BUILD);
                    break;
                case BUILD:
                    if(controller.getModel().getNextPlayerGC().getCardGod()==Gods.PROMETHEUS){
                        model.setNextPhase(Phase.WAIT_GOD_ANSWER);
                        model.setNextPlayerMessage(PlayerMessage.USE_POWER);
                        model.setNextMessageType(MessageType.USE_POWER);
                        model.updateTurn();
                        break;
                    }
                    model.setNextPhase(Phase.MOVE);
                    model.setNextPlayerMessage(PlayerMessage.MOVE);
                    model.setNextMessageType(MessageType.MOVE);
                    model.updateTurn();
                    break;
                case PROMETHEUS_WORKER:
                    model.setNextPhase(Phase.MOVE);
                    model.setNextPlayerMessage(PlayerMessage.MOVE);
                    model.setNextMessageType(MessageType.MOVE);
                    break;
            }
            model.notifyChanges();
        }
    }
}
