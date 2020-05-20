package it.polimi.ingsw.model.messageModel;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.Phase;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.simplegod.Prometheus;
import it.polimi.ingsw.utils.PlayerMessage;
import it.polimi.ingsw.view.View;

public class SetPrometheus extends Message{
    int usedWorkerId;
    public SetPrometheus(Player player, View view, int usedWorkerId) {
        super(player, view);
        this.usedWorkerId = usedWorkerId;
    }

    @Override
    public void handler(Controller controller) {
        ((Prometheus)player.getGodCard()).setWorkerID(usedWorkerId);
        player.setUsedWorker(usedWorkerId);
        Model model = controller.getModel();
        model.setNextPhase(Phase.BUILD);
        model.setNextPlayerMessage(PlayerMessage.BUILD);
        model.setNextMessageType(MessageType.BUILD);
        model.notifyChanges();
    }
}
