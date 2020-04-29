package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.Phase;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.messageModel.Message;
import it.polimi.ingsw.model.messageModel.MessageSinglePlayer;
import it.polimi.ingsw.model.messageModel.PlayerMove;
import it.polimi.ingsw.model.messageModel.PlayerWorker;

public class PhaseManager {
    private final Controller controller;

    public PhaseManager(Controller controller){
        this.controller=controller;
    }

    protected void handlePhase(Message msg){
        if(msg instanceof PlayerMove){
            controller.move((PlayerMove) msg);
        }
        else if (msg instanceof PlayerWorker) {
            controller.setPlayerWorker((PlayerWorker) msg);
        }
    }

    private void godMessageSender(){
        Model model = controller.getModel();
        Player player = model.getActualPlayer();
        Phase phase = player.getGodCard().getPhase();
        if(phase == model.getPhase()){//la fase attuale corrisponde alla fase del godcard
            //devo inviare un messaggio alla view chiedendo se si vuole attivare il potere
            model.notifyObservers(new MessageSinglePlayer(model.getActualPlayer(),"Do you want to use your god's power? (y/n)"));
        }
    }
}
