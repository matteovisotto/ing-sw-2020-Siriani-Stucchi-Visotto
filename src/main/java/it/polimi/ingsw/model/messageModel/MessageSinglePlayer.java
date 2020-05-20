package it.polimi.ingsw.model.messageModel;

import it.polimi.ingsw.model.Phase;
import it.polimi.ingsw.model.Player;

public class MessageSinglePlayer extends ViewMessage{
    private final Player player;
    public MessageSinglePlayer(Player player, String message, MessageType msg, Phase phase) {//questo invia un messaggio al singolo
        super(msg, message, phase);
        this.player = player;
    }
}
