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
     * Class' constructor
     * @param card is the God's enum instance to create the model's cards
     * @param phase is the phase in which the gods power should be activated
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
     * It has to be overridden in subclasses to define the behavior
     * @param objectList is a generic list of Objects
     */
    public void usePower(List<Object> objectList){}

    /**
     * This method returns the default controller check cell
     * @param controller is the game's controller
     * @param x is the x value of the cell
     * @param y is the y value of the cell
     * @param actualWorker is the worker selected by the player
     * @param maxUpDifference is the max difference between move cell levels allowed for the action
     * @return the result of controller check cell
     * @throws IllegalArgumentException if check cell throws it
     *
     * OVERRIDE this method to change the control
     */
    public boolean checkCell(GodCardController controller, int x, int y, Worker actualWorker, int maxUpDifference) throws IllegalArgumentException{
        return controller.checkCell(x,y, actualWorker, maxUpDifference);
    }

    /**
     * This method has to be overridden if a god card has to configure something before the turn starts, making controls for the move action
     * @param model is the game's model
     * @param controller is the game's controller
     * @param move is the move message received from the view
     */
    public void beforeMoveHandler(Model model, GodCardController controller, PlayerMove move){}

    /**
     * This method can be used (by override) to perform controls for the move
     * @param model is the game's model
     * @param controller is the game's controller
     * @param move is the move message received from the view
     * @return always false if not overridden
     *
     * This has to return false if you want to use the standard move action and controls
     * This has to return true if a different control has been done
     */
    public boolean handlerMove(Model model, GodCardController controller, PlayerMove move){return false;}

    /**
     * This method could be used to modify parameters before calling the default model.move
     * @param model the play model
     * @param controller the play controller
     * @param move the move message received from the view
     */
    public void normalMoveModifier(Model model, GodCardController controller, PlayerMove move){}

    /**
     * This returns the default controller's check build
     * @param controller is the game's controller
     * @param buildingCell the cell where the player wants to build
     * @param playerBuild the message received from the view
     * @return the controls results
     *
     * OVERRIDE this method to change the controls
     */
    public boolean checkBuilt(Controller controller, Cell buildingCell, PlayerBuild playerBuild) {
        return controller.checkBuild(buildingCell, playerBuild);
    }

    /**
     * This method has to be overridden to make controls in a build action for a god power
     * @param model is the game's model
     * @param controller is the game's controller
     * @param build is the message received from the view
     * @param buildingCell is the cell where the player wants to build
     * @return always false if not overridden
     *
     * This has to return false if you want to use the standard move action and controls
     * This has to return true if a different set of controls has been done (also report error have to return true in order to skip the move action)
     */
    public boolean handlerBuild(Model model, GodCardController controller, PlayerBuild build, Cell buildingCell){return false;}

    /**
     * This method has to be overridden if a god card needs some action at the player's turn beginning
     * @param godCardController is the game's controller
     * @param blockId is the game's id of the cell
     * @param cell is the cell where the player wants to build
     */
    public void turnStartHandler(GodCardController godCardController, int blockId, Cell cell){
    }

    /**
     * This method has to be overridden if a god card needs to perform an action after the build in done (even if the turn has changed)
     * @param model is the game's model
     * @param controller is the game's controller
     * @param playerBuild is the building message received from the view
     * @param buildingCell is the cell where the player has built
     */
    public void afterBuildHandler(Model model, GodCardController controller, PlayerBuild playerBuild, Cell buildingCell){}

    /**
     * This function has to be overridden if a god card has a particular winning condition
     * In particular, it can be used to check if he has won when is not his turn
     * @param model is the game's model
     * @param controller is the game's controller
     */
    public void checkVictory(Model model, GodCardController controller){}

    /**
     * This method is called when a player answers 'no' when a "use god power" request is asked
     * It by default sets the model's phase and messages to the default next value, based on the god card's actual phase given as param
     * @param phase is the god card phase given by the controller
     * @param controller is the game's controller
     * Always notify clients changes
     *
     * A particular control is made for Prometheus because his power has to be activated during the turn change
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
     * @return the phase in which the god power can be used
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
     * @param active is a flag representing if the god card is active in the turn
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
