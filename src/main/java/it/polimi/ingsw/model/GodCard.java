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

    public boolean checkCell(GodCardController controller, int x, int y, Worker actualWorker, int maxUpDifference) throws IllegalArgumentException{
        return controller.checkCell(x,y, actualWorker, maxUpDifference);
    }

    public void beforeMoveHandler(Model model, GodCardController controller, PlayerMove move){}

    public boolean handlerMove(Model model, GodCardController controller, PlayerMove move){return false;}

    public void normalMoveModifier(Model model, GodCardController controller, PlayerMove move){}

    public boolean checkBuilt(Controller controller, Cell buildingCell, PlayerBuild playerBuild) {
        return controller.checkBuild(buildingCell, playerBuild);
    }
    public boolean handlerBuild(Model model, GodCardController controller, PlayerBuild build, Cell buildingCell){return false;}

    public void turnStartHandler(GodCardController godCardController, int blockId, Cell buildingCell){
    }

    public void afterBuildHandler(Model model, GodCardController controller, PlayerBuild playerBuild, Cell buildingCell){}

    public void checkVictory(Model model, GodCardController controller){}

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
