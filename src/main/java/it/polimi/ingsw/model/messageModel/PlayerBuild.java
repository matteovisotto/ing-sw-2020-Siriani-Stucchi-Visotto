package it.polimi.ingsw.model.messageModel;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.View;

public class PlayerBuild extends Message {

    private final int x;
    private final int y;
    private final int workerId;

    public PlayerBuild(Player player, int workerId, int x, int y, View view) {
        super(player, view);
        this.x = x;
        this.y = y;
        this.workerId = workerId;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWorkerId(){
        return workerId;
    }

    @Override
    public void handler(Controller controller) {

        try{
            controller.build(this);
        }catch(IllegalArgumentException e){
            this.getView().reportError("Can't build there");
        }
    }
}