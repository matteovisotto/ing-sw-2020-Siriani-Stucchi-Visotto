package it.polimi.ingsw.model.messageModel;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.View;

/**
 * This class is used to notify the controller a boilt action
 */
public class PlayerBuild extends Message {

    private final int x;
    private final int y;
    private final int workerId;

    /**
     * Class constructor
     * {@inheritDoc}
     * @param workerId the selected worker id for the move
     * @param x the new x value of the cell
     * @param y the new y value of the cell
     */
    public PlayerBuild(Player player, int workerId, int x, int y, View view) {
        super(player, view);
        this.x = x;
        this.y = y;
        this.workerId = workerId;
    }

    /**
     *
     * @return the selected x value of the cell
     */
    public int getX() {
        return x;
    }

    /**
     *
     * @return the selected y value of the cell
     */
    public int getY() {
        return y;
    }

    /**
     *
     * @return the selected worker id
     */
    public int getWorkerId(){
        return workerId;
    }

    /**
     * Call the controller build function
     * If it is not permitted by checkers, the IllegalArgumentException is catch by
     * sending an error to the client who asked this action
     * @param controller thr game controller instance
     */
    @Override
    public void handler(Controller controller) {

        try{
            controller.build(this);
        }catch(IllegalArgumentException e){
            this.getView().reportError("Can't build there");
        }
    }
}