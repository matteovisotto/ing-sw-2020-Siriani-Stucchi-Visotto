package it.polimi.ingsw.model.messageModel;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.GodCardController;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.View;

/**
 * This class is used to notify the controller of the selected cards for the play by the first player
 */
public class DrawedCards extends Message {
    final int firstCard;
    final int secondCard;
    final int thirdCard;

    /**
     * Class constructor for a two players play
     * {@inheritDoc}
     * @param firstCard first card selected
     * @param secondCard second card selected
     */
    public DrawedCards(Player player, int firstCard, int secondCard,View view) {
        super(player, view);
        this.firstCard = firstCard;
        this.secondCard = secondCard;
        this.thirdCard = -1;
    }

    /**
     * Class constructor for a three players play
     * {@inheritDoc}
     * @param firstCard first card selected
     * @param secondCard second card selected
     * @param  thirdCard third card selected
     */
    public DrawedCards(Player player, int firstCard, int secondCard, int thirdCard, View view) {
        super(player, view);
        this.firstCard = firstCard;
        this.secondCard = secondCard;
        this.thirdCard = thirdCard;
    }

    /**
     *
     * @return the first selected card id
     */
    public int getFirst(){
        return firstCard;
    }

    /**
     *
     * @return the second selected card id
     */
    public int getSecond(){
        return secondCard;
    }

    /**
     *
     * @return the third selected card id
     */
    public int getThird(){
        return thirdCard;
    }

    /**
     * Call the controller drawedCards function for this configuration
     * @param controller thr game controller instance
     */
    @Override
    public void handler(Controller controller) {
        ((GodCardController)controller).drawedCards(this);
    }
}
