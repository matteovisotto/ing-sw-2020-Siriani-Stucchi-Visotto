package it.polimi.ingsw.model.messageModel;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.View;

/**
 * This class is used to notify the position where placing a player worker
 */
public class PlayerWorker extends Message{

    private final int x;
    private final int y;

    /**
     * Class constructor
     * {@inheritDoc}
     * @param x the x value of the cell
     * @param y the y value of the cell
     */
    public PlayerWorker(Player player, int x, int y, View view) {
        super(player, view);
        this.x = x;
        this.y = y;

    }

    /**
     *
     * @return the x value of the cell
     */
    public int getX() {
        return x;
    }

    /**
     *
     * @return the y value of the cell
     */
    public int getY() {
        return y;
    }

    /**
     * Call setPlayerWorker controller function for this configuration
     * @param controller thr game controller instance
     */
    @Override
    public void handler(Controller controller) {
        controller.setPlayerWorker(this);
    }
}


