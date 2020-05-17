package it.polimi.ingsw.model.messageModel;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.Phase;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.simplegod.Prometheus;
import it.polimi.ingsw.utils.PlayerMessage;
import it.polimi.ingsw.view.View;

public class SetPrometheus extends Message{
    int x;
    public SetPrometheus(Player player, View view, int x) {
        super(player, view);
        this.x=x;
    }

    @Override
    public void handler(Controller controller) {
        ((Prometheus)player.getGodCard()).setWorkerID(x);
        player.setUsedWorker(x);
        Model model= controller.getModel();
        model.setNextPhase(Phase.BUILD);
        model.setNextPlayerMessage(PlayerMessage.BUILD);
        model.setNextMessageType(MessageType.BUILD);
        model.notifyChanges();
    }
}
