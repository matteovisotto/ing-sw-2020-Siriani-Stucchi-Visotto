package it.polimi.ingsw.model.messageModel;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.GodCardController;
import it.polimi.ingsw.model.GodCard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.View;

public class DrawedCards extends Message {
    int x;
    int y;
    int z;
    public DrawedCards(Player player, int x, int y,View view) {
        super(player, view);
        this.x=x;
        this.y=y;
        this.z=-1;
    }
    public DrawedCards(Player player, int x, int y,int z, View view) {
        super(player, view);
        this.x=x;
        this.y=y;
        this.z=z;
    }

    public int getFirst(){
        return x;
    }
    public int getSecond(){
        return y;
    }
    public int getThird(){
        return z;
    }


    @Override
    public void handler(Controller controller) {
        ((GodCardController)controller).drawedCards(this);
    }
}
