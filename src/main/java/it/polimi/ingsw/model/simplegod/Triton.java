package it.polimi.ingsw.model.simplegod;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.GodCardController;
import it.polimi.ingsw.model.GodCard;
import it.polimi.ingsw.model.Gods;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.Phase;
import it.polimi.ingsw.model.messageModel.MessageType;
import it.polimi.ingsw.model.messageModel.PlayerMove;
import it.polimi.ingsw.utils.PlayerMessage;

import java.util.List;

public class Triton extends GodCard {
    private int usedWorkerID =-1;
    /**
     * {@inheritDoc}
     */
    public Triton() {
        super(Gods.TRITON, Phase.MOVE);
    }

    /**
     * @return the used worker id
     */
    public int getUsedWorkerID(){
        return this.usedWorkerID;
    }

    /**
     *
     * @param worker the id of the used worker at the first action
     */
    public void setUsedWorkerID(int worker){
        this.usedWorkerID =worker;
    }



    /**
     * Set model next messages and phase to MOVE again
     */
    @Override
    public void usePower(List<Object> objectList) {
        Model model = (Model)objectList.get(0);
        model.setNextPhase(getPhase());
        model.setNextPlayerMessage(PlayerMessage.MOVE);
        model.setNextMessageType(MessageType.MOVE);
        model.notifyChanges();
    }

    /**
     * This method ad a control before a move action is performed
     * If after confirmed the usages of the god power he can't move, he lose
     * @param model the play model
     * @param controller the play controller
     * @param move the move message received from the view
     */
    @Override
    public void beforeMoveHandler(Model model, GodCardController controller, PlayerMove move) {
        if(controller.canMove(move.getPlayer().getWorker(move.getWorkerId()),move.getPlayer())==0){
            if(getUsedWorkerID()!=-1)
                model.loose(move.getPlayer());
        }
    }

    /**
     * This method change the flow of the play asking the player to use the god power after a move action is done
     * In particular this god can move all the times he want around the perimeter of the board. It check if the selected worker is in the perimeter and if
     * true ask to use the power until he get stuck, he moved away from the perimeter or he answered no
     * @param model the play model
     * @param controller the play controller
     * @param move the move message received from the view
     * @return true if an error is reported or the flow has been modified, false if not
     * If the power has been used the worker flag is reset
     */
    @Override
    public boolean handlerMove(Model model, GodCardController controller, PlayerMove move) {
        if(getUsedWorkerID()==-1){
            setUsedWorkerID(move.getWorkerId());
        }
        if(getUsedWorkerID() != move.getWorkerId()){
            move.getView().reportError("you have to move the same worker");
        }
        else if(move.getRow()==0 || move.getRow()==4 || move.getColumn()==0 || move.getColumn()==4 ){
            model.setNextPhase(Phase.WAIT_GOD_ANSWER);
            model.setNextPlayerMessage(PlayerMessage.USE_POWER);
            model.setNextMessageType(MessageType.USE_POWER);
            model.move(move);
            model.notifyChanges();
        }
        else{
            setUsedWorkerID(-1);
            return false;
        }

        return true;
    }

    /**
     * When a no answer is given for the power the flow is changed to built as default
     * @param phase the god card phase gicen by the controller
     * @param controller the play controller
     * Always notify clients changes
     *
     */
    @Override
    public void performGodMessageForPhaseWithNegativeAnswer(Phase phase, Controller controller) {
        Model model = controller.getModel();
        setUsedWorkerID(-1);
        model.setNextPhase(Phase.BUILD);
        model.setNextPlayerMessage(PlayerMessage.BUILD);
        model.setNextMessageType(MessageType.BUILD);
        model.notifyChanges();
    }
}
