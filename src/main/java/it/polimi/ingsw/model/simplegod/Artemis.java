
package it.polimi.ingsw.model.simplegod;

import it.polimi.ingsw.controller.GodCardController;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.messageModel.MessageType;
import it.polimi.ingsw.model.messageModel.PlayerMove;
import it.polimi.ingsw.utils.PlayerMessage;

import java.util.List;

/**
 This class is intended to represent the Artemis's GodCard
 */
public class Artemis extends GodCard {
    private Cell firstMove;
    private boolean usedPower;
    private Worker previousWorker;

    /**
     * {@inheritDoc}
     */
    public Artemis() {
        super(Gods.ARTEMIS, Phase.MOVE);
    }

    /**
     * @return the cell corresponding to the first move make by the player.
     */
    public Cell getFirstMove(){
        return firstMove;
    }

    /**
     * @param firstBuilt is the cell corresponding to the first move make by the player.
     */
    public void setFirstMove(Cell firstBuilt){
        this.firstMove = firstBuilt;
    }

    /**
     * This method moves the player; it could be used only if the player decide to activate his power.
     * @param objectList contain the model of the actual game (objectList.get(0)).
     * @see PlayerMove {@link PlayerMove}
     */
    @Override
    public void usePower(List<Object> objectList) {
        usedPower = true;
        Model model = (Model)objectList.get(0);
        model.setNextPhase(getPhase());
        model.setNextPlayerMessage(PlayerMessage.MOVE);
        model.setNextMessageType(MessageType.MOVE);
        model.notifyChanges();
    }

    /**
     * Artemis can move two times but not in the cell from which starts the turn
     * This method control if is the first time it move and so ask for using god power,
     * If this is the second move, it controls if the used worker is the same, the selected cell is not the one from which it moved
     * and then reset flag and make a normal move.
     * @param model the play model
     * @param controller the play controller
     * @param move the move message received from the view
     * @return false only if this is the second move and all error controls are negative
     */
    @Override
    public boolean handlerMove(Model model, GodCardController controller, PlayerMove move) {
        if(hasUsedPower()){
            if(getPreviousWorker() != move.getPlayer().getWorker(move.getWorkerId())){
                move.getView().reportError("You have to move the same worker");
            }
            else if(getFirstMove() == model.getBoard().getCell(move.getRow(), move.getColumn())){
                move.getView().reportError("You can't move into the previous cell");
            }
            else{
                setUsedPower(false);
                return false;
            }
        }
        else{
            setFirstMove(model.getActualPlayer().getWorker(move.getWorkerId()).getCell());
            setPreviousWorker(model.getActualPlayer().getWorker(move.getWorkerId()));
            model.setNextPhase(Phase.WAIT_GOD_ANSWER);
            model.setNextPlayerMessage(PlayerMessage.USE_POWER);
            model.setNextMessageType(MessageType.USE_POWER);
            model.move(move);
            if(controller.canMove(move.getPlayer().getWorker(move.getWorkerId()), move.getPlayer())==1){
                model.setNextPhase(Phase.BUILD);
                model.setNextPlayerMessage(PlayerMessage.BUILD);
                model.setNextMessageType(MessageType.BUILD);
            }
            model.notifyChanges();
        }
        return true;
    }
    /**
     * @return true if the power has already been used.
     */
    public boolean hasUsedPower() {
        return usedPower;
    }

    /**
     * @param usedPower is a flag that if it's set to true means that the power has already been used.
     */
    public void setUsedPower(boolean usedPower) {
        this.usedPower = usedPower;
    }

    /**
     * @return the worker that was used in the previous move (before using the power).
     */
    public Worker getPreviousWorker() {
        return previousWorker;
    }

    /**
     * @param previousWorker is worker that represent the previous move make by the player (before using the power).
     */
    public void setPreviousWorker(Worker previousWorker) {
        this.previousWorker = previousWorker;
    }
}
