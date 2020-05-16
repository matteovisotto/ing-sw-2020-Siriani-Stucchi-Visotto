package it.polimi.ingsw.model.messageModel;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.View;

import java.io.Serializable;

public abstract class Message implements Serializable {
    protected final Player player;
    protected final View view;

    public Message(Player player, View view){
        this.player = player;
        this.view = view;
    }

    public Player getPlayer() {
        return player;
    }

    public View getView() {
        return view;
    }

    public abstract void handler(Controller controller);
}
