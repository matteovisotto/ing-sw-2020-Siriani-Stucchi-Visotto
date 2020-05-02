package it.polimi.ingsw.model.messageModel;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.View;

public class PlayerBuild extends Message {

    private final int x;
    private final int y;

    public PlayerBuild(Player player, int x, int y, View view) {
        super(player, view);
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public void handler(Controller controller) {

        try{
            controller.increaseLevel(this);
        }catch(IllegalArgumentException e){
            this.getView().reportError("Can't build there");
        }
    }
}