package it.polimi.ingsw.model.messageModel;

import it.polimi.ingsw.model.Player;

public class MessageSinglePlayer extends ViewMessage{

    public MessageSinglePlayer(Player player, String message) {//questo invia un messaggio al singolo
        super(player, message);
    }
}
