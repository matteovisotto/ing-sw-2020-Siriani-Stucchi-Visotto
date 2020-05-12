package it.polimi.ingsw.model.messageModel;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.GodCardController;
import it.polimi.ingsw.model.GodCard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.View;

public class DrawedCards extends Message {
    int firstCard;
    int secondCard;
    int thirdCard;
    public DrawedCards(Player player, int firstCard, int secondCard,View view) {
        super(player, view);
        this.firstCard = firstCard;
        this.secondCard = secondCard;
        this.thirdCard = -1;
    }
    public DrawedCards(Player player, int firstCard, int secondCard, int thirdCard, View view) {
        super(player, view);
        this.firstCard = firstCard;
        this.secondCard = secondCard;
        this.thirdCard = thirdCard;
    }

    public int getFirst(){
        return firstCard;
    }
    public int getSecond(){
        return secondCard;
    }
    public int getThird(){
        return thirdCard;
    }


    @Override
    public void handler(Controller controller) {
        ((GodCardController)controller).drawedCards(this);
    }
}
