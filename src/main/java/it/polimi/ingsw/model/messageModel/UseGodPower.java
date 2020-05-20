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
            switch(player.getGodCard().getCardGod()){
                case ATLAS:
                    playerGodCard.usePower(new ArrayList<Object>(Collections.singletonList(model)));
                case ARTHEMIS:
                    playerGodCard.usePower(new ArrayList<Object>(Collections.singletonList(model)));
                case DEMETER:
                    playerGodCard.usePower(new ArrayList<Object>(Collections.singletonList(model)));
                case HEPHAESTUS:
                    playerGodCard.usePower(new ArrayList<Object>(Collections.singletonList(model)));
                case PROMETHEUS:
                    playerGodCard.usePower(new ArrayList<Object>(Collections.singletonList(model)));
            }

        }
        else{
            model.setNextPhase(Phase.next(playerGodCard.getPhase()));
            switch(playerGodCard.getPhase()){
                case MOVE:
                    model.setNextPlayerMessage(PlayerMessage.BUILD);
                    model.setNextMessageType(MessageType.BUILD);
                    model.notifyChanges();
                case BUILD:
                    model.setNextPlayerMessage(PlayerMessage.MOVE);
                    model.setNextMessageType(MessageType.MOVE);
                    model.updateTurn();
                    model.notifyChanges();
                case PROMETHEUS_WORKER:
                    model.setNextPhase(Phase.MOVE);
                    model.setNextPlayerMessage(PlayerMessage.MOVE);
                    model.setNextMessageType(MessageType.MOVE);
                    model.notifyChanges();
            }
        }
    }
}
