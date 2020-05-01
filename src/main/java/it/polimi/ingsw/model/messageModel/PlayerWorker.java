package it.polimi.ingsw.model.messageModel;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.View;

public class PlayerWorker extends Message{

    private final int x;
    private final int y;

    public PlayerWorker(Player player, int x, int y, View view) {
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
        controller.setPlayerWorker(this);
    }
}


