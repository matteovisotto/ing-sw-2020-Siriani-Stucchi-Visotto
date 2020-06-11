package it.polimi.ingsw.model.simplegod;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.GodCardController;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.messageModel.MessageType;
import it.polimi.ingsw.model.messageModel.PlayerBuild;
import it.polimi.ingsw.utils.PlayerMessage;

import java.util.List;

public class Poseidon  extends GodCard {
    private int counter;
    private Worker unusedWorker = null;
    private Worker movedWorker = null;

    /**
     * {@inheritDoc}
     */
    public Poseidon() {
        super(Gods.POSEIDON, Phase.BUILD);
        counter = 0;
    }

    /**
     *
     * @return the number of times the player has built
     */
    public int getNumOfBuild() {
        return this.counter;
    }

    /**
     * Reset the build counter
     */
    public void resetBuild() {
        counter = 0;
    }

    /**
     * This method set a variable containing the worker not used in the turn
     * @param worker the worker to set
     */
    public void setUnusedWorker(Worker worker) {
        this.unusedWorker = worker;
    }

    /**
     *
     * @return the worker not used in the turn, if exist
     */
    public Worker getUnusedWorker() {
        return this.unusedWorker;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void usePower(List<Object> objectList) {
        counter++;
        Model model = (Model) objectList.get(0);
        model.setNextPhase(getPhase());
        model.setNextPlayerMessage(PlayerMessage.BUILD);
        model.setNextMessageType(MessageType.BUILD);
        model.notifyChanges();
    }

    /**
     *
     * @return the worker the player has used in the turn
     */
    public Worker getMovedWorker() {
        return movedWorker;
    }

    /**
     * Set the worker the player has used in the turn
     * @param movedWorker the used worker
     */
    public void setMovedWorker(Worker movedWorker) {
        this.movedWorker = movedWorker;
    }

    /**
     * This method modify the normal build control.
     * Poseidon can build three more time with the worker he hasn't used if it is in a level 0 cell
     * If this is the first build the used worker is set, and the unused one is computed and set (the same paramenter is set in player instance)
     * After the first build, if the unused worker is in a level 0 cell and it hasn't alresy built three times, ask if using god power
     * When one of the condition are false, the normal game flow is set and the flag reset
     * @param model the play model
     * @param controller the play controller
     * @param build the message recived by the view
     * @param buildingCell the cell where the player wants to build
     * @return true id the unused worker is in a 0 cell level and it hasn't built three times, else false
     */
    @Override
    public boolean handlerBuild(Model model, GodCardController controller, PlayerBuild build, Cell buildingCell) {
        if(getMovedWorker()==null){
            setMovedWorker(build.getPlayer().getWorker(build.getWorkerId()));
            setUnusedWorker(build.getPlayer().getWorker((build.getWorkerId()+1)%2));
            build.getPlayer().setUsedWorker((build.getWorkerId()+1)%2);
        }
        //se non ho costruito già 3 volte e se il worker inutilizzato e' al livello 0 e se il worker inutilizzato puo costruire
        if(getNumOfBuild()<3 && getUnusedWorker().getCell().getLevel().getBlockId()==0 && controller.checkCanBuild(getUnusedWorker())) {
            model.setNextPhase(Phase.WAIT_GOD_ANSWER);
            model.setNextPlayerMessage(PlayerMessage.USE_POWER);
            model.setNextMessageType(MessageType.USE_POWER);
            controller.godIncreaseLevel(buildingCell.getLevel().getBlockId(), buildingCell);
            return true;
        }

        setUnusedWorker(null);
        setMovedWorker(null);
        resetBuild();
        return false;


    }

    /**
     * When the player doesn't want to use the power, the model is set in normal flow, updated the turn and rest the flags
     * @param phase the god card phase gicen by the controller
     * @param controller the play controller
     * Always notify clients changes
     *
     */
    @Override
    public void performGodMessageForPhaseWithNegativeAnswer(Phase phase, Controller controller) {
        Model model = controller.getModel();
            setMovedWorker(null);
            setUnusedWorker(null);
            resetBuild();
            model.setNextPhase(Phase.MOVE);
            model.setNextPlayerMessage(PlayerMessage.MOVE);
            model.setNextMessageType(MessageType.MOVE);
            model.updateTurn();
            model.notifyChanges();
    }
}
