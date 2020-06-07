package it.polimi.ingsw.model.messageModel;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.simplegod.Poseidon;
import it.polimi.ingsw.model.simplegod.Triton;
import it.polimi.ingsw.utils.PlayerMessage;
import it.polimi.ingsw.view.View;

import java.util.ArrayList;
import java.util.Collections;

/**
 * This class is used to notify at the controller the player choice of using a god power
 */
public class UseGodPower extends Message {
    char usePower;

    /**
     * Class constructor
     * {@inheritDoc}
     * @param usePower char representing player choice
     */
    public UseGodPower(Player player, View view, char usePower) {
        super(player, view);
        this.usePower = usePower;
    }

    /**
     * If player choose to use the god power with y answer call the god card instance userPower function
     * else set the next default model configuration for each case of phase in base of the point of the game
     * @param controller thr game controller instance
     */
    @Override
    public void handler(Controller controller) {
        Model model = controller.getModel();
        GodCard playerGodCard = player.getGodCard();
        if(usePower == 'y'){
            playerGodCard.usePower(new ArrayList<Object>(Collections.singletonList(model)));
        }
        else{//se io dico di  no

            switch(playerGodCard.getPhase()){
                case MOVE:
                    if(controller.getModel().getActualPlayer().getGodCard().getCardGod()==Gods.TRITON){
                        ((Triton)playerGodCard).setUsedWorkerID(-1);
                    }
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
                    if(controller.getModel().getActualPlayer().getGodCard().getCardGod()==Gods.POSEIDON){
                        ((Poseidon)playerGodCard).setMovedWorker(null);
                        ((Poseidon)playerGodCard).setUnusedWorker(null);
                        ((Poseidon)playerGodCard).resetBuild();
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
