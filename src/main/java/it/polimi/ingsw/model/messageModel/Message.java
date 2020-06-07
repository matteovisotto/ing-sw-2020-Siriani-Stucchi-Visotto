package it.polimi.ingsw.model.messageModel;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.View;

import java.io.Serializable;

/**
 * This class in the genric abstract class for messages from remote view to controller
 */
public abstract class Message implements Serializable {
    protected final Player player;
    protected final View view;

    /**
     *
     * @param player the player in tunr who in sending the message
     * @param view the remote view instance representing the client on the server
     */
    public Message(Player player, View view){
        this.player = player;
        this.view = view;
    }

    /**
     *
     * @return the player instance
     */
    public Player getPlayer() {
        return player;
    }

    /**
     *
     * @return the view instance
     */
    public View getView() {
        return view;
    }

    /**
     * Abstract metod that have to be overridden by subclasses
     * Is used to handler actions in base of the message subclass type
     * @param controller thr game controller instance
     */
    public abstract void handler(Controller controller);
}
