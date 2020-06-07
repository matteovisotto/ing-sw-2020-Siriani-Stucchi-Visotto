package it.polimi.ingsw.model.messageModel;

import it.polimi.ingsw.model.Phase;
import it.polimi.ingsw.model.Player;

/**
 * This class represent messages send in a player turn
 */
public class GameMessage extends ViewMessage{
    private final Player player;

    /**
     * Class contructor
     * {@inheritDoc}
     * @param player the player in turn
     */
    public GameMessage(Player player, String message, MessageType messageType, Phase phase){
        super(messageType, message, phase);
        this.player = player;
    }

    /**
     *
     * @return the player instance
     */
    public Player getPlayer(){
        return player;
    }
}
