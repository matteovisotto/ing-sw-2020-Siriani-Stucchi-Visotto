package it.polimi.ingsw.model.messageModel;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.View;

/**
 * This class is used to notify to the controller a move action
 */
public class PlayerMove extends Message{
    private final int x;
    private final int y;
    private final int workerId;

    /**
     * Class constructor
     * {@inheritDoc}
     * @param workerId the selected worker for the move action
     * @param x the new x cell value
     * @param y the new y cell value
     */
    public PlayerMove(Player player, int workerId, int x, int y, View view) {
        super(player, view);
        this.x = x;
        this.y = y;
        this.workerId = workerId;
    }

    /**
     *
     * @return selected worker id
     */
    public int getWorkerId(){
        return workerId;
    }

    /**
     *
     * @return return the selected c value
     */
    public int getRow() {
        return x;
    }

    /**
     *
     * @return the selected y value
     */
    public int getColumn() {
        return y;
    }


    /**
     * Call controller move for this configuration
     * @param controller thr game controller instance
     */
    @Override
    public void handler(Controller controller) {
        controller.move(this);
    }
}