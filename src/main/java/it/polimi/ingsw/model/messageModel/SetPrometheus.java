package it.polimi.ingsw.model.messageModel;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Gods;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.Phase;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.simplegod.Prometheus;
import it.polimi.ingsw.utils.PlayerMessage;
import it.polimi.ingsw.view.View;

/**
 * This class is used to perform choice with the player who have prometheus god card
 */
public class SetPrometheus extends Message{
    final int usedWorkerId;

    /**
     * Class constructor
     * {@inheritDoc}
     * @param usedWorkerId the worker identifier selected by the player
     */
    public SetPrometheus(Player player, View view, int usedWorkerId) {
        super(player, view);
        this.usedWorkerId = usedWorkerId;
    }

    /**
     * Set the selected worker id on player gos card instance
     * Set model next phase before update
     * Set model message type and string message
     * Ask model to notify changes to views
     * @param controller thr game controller instance
     */
    @Override
    public void handler(Controller controller) {
        ((Prometheus)player.getGodCard()).setWorkerID(usedWorkerId);
        player.setUsedWorker(usedWorkerId);
        Model model = controller.getModel();
        if(model.getGCPlayer(Gods.PROMETHEUS).getWorker(usedWorkerId).getStatus()){
            model.setNextPhase(Phase.BUILD);
            model.setNextPlayerMessage(PlayerMessage.BUILD);
            model.setNextMessageType(MessageType.BUILD);
            model.notifyChanges();
        }
        else{
            model.setNextPhase(Phase.PROMETHEUS_WORKER);
            model.setNextPlayerMessage(PlayerMessage.PROMETHEUS_ASK_WORKER);
            model.setNextMessageType(MessageType.PROMETHEUS);
            this.view.reportError("The selected worker can't move");
            model.notifyChanges();
        }

    }
}
