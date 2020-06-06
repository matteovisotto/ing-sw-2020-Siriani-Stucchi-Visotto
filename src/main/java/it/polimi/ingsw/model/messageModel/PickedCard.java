package it.polimi.ingsw.model.messageModel;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.GodCardController;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.View;

/**
 * This class is uset to notify the controller of a card selected by the player
 */
public class PickedCard extends Message {
    private final int card;

    /**
     * Class constructor
     * {@inheritDoc}
     * @param card an int value that represent the card selected by the player.
     */
    public PickedCard(Player player, View view, int card) {
        super(player, view);
        this.card = card;
    }

    /**
     * Call controller pickACard function for this configuration
     * @param controller thr game controller instance
     */
    @Override
    public void handler(Controller controller) {
        ((GodCardController)controller).pickACard(this);
    }

    /**
     *
     * @return the id of the selected card
     */
    public int getCardId() {
        return card;
    }
}
