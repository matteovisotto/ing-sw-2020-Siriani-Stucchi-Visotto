package it.polimi.ingsw.model.messageModel;

import it.polimi.ingsw.model.Player;

public class GameMessage extends ViewMessage{
    private final Player player;

    public GameMessage(Player player, String message, MessageType messageType){
        super(messageType, message);
        this.player = player;
    }

    public Player getPlayer(){
        return player;
    }
}
