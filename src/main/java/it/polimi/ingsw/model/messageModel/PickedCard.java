package it.polimi.ingsw.model.messageModel;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.GodCardController;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.View;

public class PickedCard extends Message {
    private final int card;
    public PickedCard(Player player, View view, int card) {
        super(player, view);
        this.card = card;
    }

    @Override
    public void handler(Controller controller) {
        ((GodCardController)controller).pickACard(this);
    }

    public int getCardId() {
        return card;
    }
}
