package it.polimi.ingsw.model.messageModel;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.View;

public class PickedCard extends Message {
    private final int x;
    public PickedCard(Player player, View view, int x) {
        super(player, view);
        this.x=x;
    }

    @Override
    public void handler(Controller controller) {
        controller.pickACard(this);
    }

    public int getCardId() {
        return x;
    }
}
