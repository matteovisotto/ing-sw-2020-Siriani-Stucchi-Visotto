package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.GodCardController;
import it.polimi.ingsw.model.messageModel.MessageType;
import it.polimi.ingsw.model.messageModel.PlayerBuild;
import it.polimi.ingsw.model.messageModel.PlayerMove;
import it.polimi.ingsw.utils.PlayerMessage;

import java.io.Serializable;
import java.util.List;

public class GodCard implements Serializable {

    protected final Gods card;
    private boolean active = false;
    protected final Phase phase;

    /**
     * Constructor of the class
     * @param card the Gods enum instance for creating the model card
     * @param phase the phase in which the gods power should be activated
     */
    public GodCard(Gods card, Phase phase){
        this.card = card;
        this.phase = phase;
    }

    /**
     *
     * @return the Gods enum instance of the card created in this class
     */
    public Gods getCardGod() {
        return this.card;
    }

    /**
     *
     * @return the name of the god
     */
    public String getName() {
        return card.toString();
    }

    /**
     * This function is used to perform actions with the god power
     * Have to be overridden in subclasses to define the behavior
     * @param objectList a generic list of Objects
     */
    public void usePower(List<Object> objectList){}

    /**
     * This method return the default controller check cell
     * @param controller the play controller
     * @param x the x value of the cell
     * @param y the y value of the cell
     * @param actualWorker the selected worker by the player
     * @param maxUpDifference the max difference between move cell levels permitted for the action
     * @return the result of controller check cell
     * @throws IllegalArgumentException if check cell throws it
     *
     * OVERRIDE this method to chenge the control
     */
    public boolean checkCell(GodCardController controller, int x, int y, Worker actualWorker, int maxUpDifference) throws IllegalArgumentException{
        return controller.checkCell(x,y, actualWorker, maxUpDifference);
    }

    /**
     * This method have to be overridden if a god card have to configure something before start making controls for the move action
     * @param model the play model
     * @param controller the play controller
     * @param move the move message received from the view
     */
    public void beforeMoveHandler(Model model, GodCardController controller, PlayerMove move){}

    /**
     * This method can be used (by override) to perform controls for the move
     * @param model the play model
     * @param controller the play controller
     * @param move the move message received from the view
     * @return always false if not overridden
     *
     * This have to return false if you want to use the standard move action and controls
     * This have to return true if a different control has been done (also report error have to return true in order to skip the move action)
     */
    public boolean handlerMove(Model model, GodCardController controller, PlayerMove move){return false;}

    /**
     * This method could by used to modify parameters before colling standard model.move
     * @param model the play model
     * @param controller the play controller
     * @param move the move message received from the view
     */
    public void normalMoveModifier(Model model, GodCardController controller, PlayerMove move){}

    /**
     * This return the default controller check build
     * @param controller the play controller
     * @param buildingCell the cell where player wants to build
     * @param playerBuild the message received by the view
     * @return the given result from the controller
     *
     * OVERRIDE this method to change the control
     */
    public boolean checkBuilt(Controller controller, Cell buildingCell, PlayerBuild playerBuild) {
        return controller.checkBuild(buildingCell, playerBuild);
    }

    /**
     * This method have to be overridden to make controls in a build action for a god power
     * @param model the play model
     * @param controller the play controller
     * @param build the message recived by the view
     * @param buildingCell the cell where the player wants to build
     * @return always false if not overridden
     *
     * This have to return false if you want to use the standard move action and controls
     * This have to return true if a different control has been done (also report error have to return true in order to skip the move action)
     */
    public boolean handlerBuild(Model model, GodCardController controller, PlayerBuild build, Cell buildingCell){return false;}

    /**
     * This method have to by overridden if a god card need some action at the player turn start
     * @param godCardController the play controller
     * @param blockId the level id of the cell
     * @param cell the cell where the player wants to buold
     */
    public void turnStartHandler(GodCardController godCardController, int blockId, Cell cell){
    }

    /**
     * This method have to be overridden if a god card needs to perform action after the build action in done (even id the turn is changed)
     * @param model the play model
     * @param controller the play controller
     * @param playerBuild the building message received by the view
     * @param buildingCell the cell where the player has built
     */
    public void afterBuildHandler(Model model, GodCardController controller, PlayerBuild playerBuild, Cell buildingCell){}

    /**
     * This function have to be overridden if a god card have a particular winning condition
     * In particular can be used to check if he has won when in not his turn
     * @param model the play model
     * @param controller the play controller
     */
    public void checkVictory(Model model, GodCardController controller){}

    /**
     * This method is called when a player ask 'no' when a use god power request is done
     * By default set model phase and messages to the natural next value in base of the god card actual phase given as param
     * @param phase the god card phase gicen by the controller
     * @param controller the play controller
     * Always notify clients changes
     *
     * A particular control is made for Prometheus because his power have to be activated in the turn change
     */
    public void performGodMessageForPhaseWithNegativeAnswer(Phase phase, Controller controller){
        Model model = controller.getModel();
        switch(phase){
            case MOVE:
                model.setNextPhase(Phase.BUILD);
                model.setNextPlayerMessage(PlayerMessage.BUILD);
                model.setNextMessageType(MessageType.BUILD);
                break;
            case BUILD:
                if(controller.getModel().getNextPlayerGC().getCardGod()== Gods.PROMETHEUS){
                    model.setNextPhase(Phase.WAIT_GOD_ANSWER);
                    model.setNextPlayerMessage(PlayerMessage.USE_POWER);
                    model.setNextMessageType(MessageType.USE_POWER);
                    model.updateTurn();
                    break;
                }
                model.setNextPhase(Phase.MOVE);
                model.setNextPlayerMessage(PlayerMessage.MOVE);
                model.setNextMessageType(MessageType.MOVE);
                model.updateTurn();
                break;
            case PROMETHEUS_WORKER:
                model.setNextPhase(Phase.MOVE);
                model.setNextPlayerMessage(PlayerMessage.MOVE);
                model.setNextMessageType(MessageType.MOVE);
                break;
        }
        model.notifyChanges();
    }

    /**
     *
     * @return the phase in which control if god power can be used
     */
    public Phase getPhase(){
        return phase;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof GodCard){
            return ((GodCard) obj).card.getSimpleGodId() == this.card.getSimpleGodId();
        }
        return false;
    }

    /**
     *
     * @param active a flag that rappresent if the god card is active in the turn
     */
    public void setActive(boolean active){
        this.active = active;
    }

    /**
     *
     * @return true if the god card has been activated in the turn
     */
    public boolean isActive() {
        return active;
    }


}
