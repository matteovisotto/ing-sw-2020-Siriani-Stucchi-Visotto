package it.polimi.ingsw.model.messageModel;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.GodCard;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.View;

import java.util.ArrayList;
import java.util.Collections;

/**
 * This class is used to notify at the controller the player choice of using a god power
 */
public class UseGodPower extends Message {
    final char usePower;

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
            playerGodCard.performGodMessageForPhaseWithNegativeAnswer(playerGodCard.getPhase(), controller);
        }
    }
}
