package it.polimi.ingsw.model.messageModel;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;

public class ViewMessage {

    private final Player player;
    private final String message;

    public ViewMessage(Player player, String message){
        this.player=player;
        this.message=message;
    }

    public String getMessage() {
        return message;
    }

    public Player getPlayer(){
        return player;
    }
}
