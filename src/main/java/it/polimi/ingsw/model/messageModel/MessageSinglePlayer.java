package it.polimi.ingsw.model.messageModel;

import it.polimi.ingsw.model.Player;

public class MessageSinglePlayer extends ViewMessage{
    private final Player player;
    public MessageSinglePlayer(Player player, String message, MessageType msg) {//questo invia un messaggio al singolo
        super(msg, message);
        this.player=player;
    }
}
