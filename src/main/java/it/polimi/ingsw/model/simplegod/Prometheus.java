
package it.polimi.ingsw.model.simplegod;

import it.polimi.ingsw.controller.GodCardController;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.messageModel.MessageType;
import it.polimi.ingsw.model.messageModel.PlayerBuild;
import it.polimi.ingsw.model.messageModel.PlayerMove;
import it.polimi.ingsw.utils.PlayerMessage;

import java.util.List;

/**
 This class is intended to represent the Prometheus's GodCard
 */
public class Prometheus extends GodCard {
    private boolean built = false; //se ha usato il potere
    private boolean usedPower;
    private int workerID = 0;

    /**
     * {@inheritDoc}
     */
    public Prometheus() {
        super(Gods.PROMETHEUS, Phase.PROMETHEUS_WORKER);
    }

    /**
     * @return a boolean that is true if the player has already used his power (the player has arrived to the second build phase).
     */
    public boolean hasBuilt() {
        return built;
    }

    /**
     * @param built is set to true if the player build using his power.
     */
    public void setBuild(boolean built){
        this.built = built;
    }

    /**
     * This method make move controls for Prometheus, in particular check if the moved worker is the same used to built the step before
     * and also check if player try to move up
     * @param model the play model
     * @param controller the play controller
     * @param move the move message received from the view
     * @return true if an error is reported as described in superclass for a wrong worker selection or a try to move up
     *          return false if all error controls are skipped, so a standard built is done
     */
    @Override
    public boolean handlerMove(Model model, GodCardController controller, PlayerMove move) {
        if(hasUsedPower()){
            if(getWorkerID() != move.getWorkerId()){
                move.getView().reportError("you have to move the same worker");
                return true;
            }
            else if(move.getPlayer().getWorker(move.getWorkerId()).getCell().getLevel().getBlockId() < model.getBoard().getCell(move.getRow(), move.getColumn()).getLevel().getBlockId()){
                move.getView().reportError("you can't move up");
                return true;
            }

        }

        return false;
    }

    /**
     * This method makes controls for the build.
     * If the player has not already used the god power return false to make a standard built, if not check:
     *  - The worker used to built is the same of the first two actions
     *  - If he has already built the first time reset flags and return false for a standard build
     *  - If is the power built action, set model next phase and messages to move, set flags at true and call the controller increase level to make it returning true
     *
     * @param model the play model
     * @param controller the play controller
     * @param build the message received by the view
     * @param buildingCell the cell where the player wants to build
     * @return true if a different control is done else false
     */
    @Override
    public boolean handlerBuild(Model model, GodCardController controller, PlayerBuild build, Cell buildingCell) {
        if(hasUsedPower()){
            if(getWorkerID() != build.getWorkerId()){
                build.getView().reportError("You have to use the same worker.");
                return true;
            }
            if(!hasBuilt()){
                model.setNextMessageType(MessageType.MOVE);
                model.setNextPlayerMessage(PlayerMessage.MOVE);
                model.setNextPhase(Phase.MOVE);
                setBuild(true);
                controller.godIncreaseLevel(buildingCell.getLevel().getBlockId(), buildingCell);
                return true;
            }

                setUsedPower(false);
                setBuild(false);
                return false;

        }
        return false;

    }

    /**
     * This method is called after the first build to check if the worker used for the built is stacked so he can't complete the move and the second build so he loose
     * @param model the play model
     * @param controller the play controller
     * @param playerBuild the building message received by the view
     * @param buildingCell the cell where the player has built
     */
    @Override
    public void afterBuildHandler(Model model, GodCardController controller, PlayerBuild playerBuild, Cell buildingCell) {
        if(controller.canMove(playerBuild.getPlayer().getWorker(playerBuild.getWorkerId()), playerBuild.getPlayer())==0){
            if(hasBuilt()){
                model.loose(playerBuild.getPlayer());
            }

            playerBuild.getView().reportError("This worker can't move anywhere");
        }
    }


    /**
     * This method is used at the beginnign of the turn to set model to sent a god power request to the player
     * @param godCardController the play controller
     * @param blockId the level id of the cell
     * @param cell the cell where the player wants to build
     */
    @Override
    public void turnStartHandler(GodCardController godCardController, int blockId, Cell cell) {
        if(!hasBuilt()) {
            godCardController.getModel().setNextPhase(Phase.WAIT_GOD_ANSWER);
            godCardController.getModel().setNextPlayerMessage(PlayerMessage.USE_POWER);
            godCardController.getModel().setNextMessageType(MessageType.USE_POWER);

        }
    }

    /**
     * This method makes a player build; it could be used only if the player decide to activate his power.
     * @param objectList contain the model of the actual game (objectList.get(0)).
     */
    @Override
    public void usePower(List<Object> objectList) {
        usedPower = true;
        Model model = (Model)objectList.get(0);
        model.setNextPhase(getPhase());
        model.setNextPlayerMessage(PlayerMessage.PROMETHEUS_ASK_WORKER);
        model.setNextMessageType(MessageType.PROMETHEUS);
        model.notifyChanges();
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
     * @return an int that represent the worker selected by the player to make the first built.
     */
    public int getWorkerID() {
        return workerID;
    }

    /**
     * @param workerID is the worker selected by the player to make the first built.
     */
    public void setWorkerID(int workerID) {
        this.workerID = workerID;
    }
}
