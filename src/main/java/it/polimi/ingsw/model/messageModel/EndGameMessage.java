package it.polimi.ingsw.model.messageModel;

import it.polimi.ingsw.model.Phase;
import it.polimi.ingsw.model.Player;

import java.util.HashMap;

public class EndGameMessage extends GameMessage {
    private HashMap<Player, Integer> podium;
    public EndGameMessage(Player player, String message, MessageType messageType, Phase phase, HashMap<Player, Integer> podium) {
        super(player, message, messageType, phase);
        this.podium=podium;
    }
    public HashMap<Player, Integer> getPodium(){
        return this.podium;
    }
}
