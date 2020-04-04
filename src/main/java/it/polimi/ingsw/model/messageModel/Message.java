package it.polimi.ingsw.model.messageModel;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.View;

public class Message {
    private final Player player;
    private final View view;

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
}