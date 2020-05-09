package it.polimi.ingsw.model.messageModel;

import it.polimi.ingsw.controller.Controller;
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


    @Override
    public void handler(Controller controller) {
        controller.drawedCards(x,y,z);
    }
}
