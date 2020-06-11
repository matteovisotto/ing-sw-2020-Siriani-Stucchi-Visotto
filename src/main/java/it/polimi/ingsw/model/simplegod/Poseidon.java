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

    public int getNumOfBuild() {
        return this.counter;
    }

    public void resetBuild() {
        counter = 0;
    }

    public void setUnusedWorker(Worker worker) {
        this.unusedWorker = worker;
    }

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

    public Worker getMovedWorker() {
        return movedWorker;
    }

    public void setMovedWorker(Worker movedWorker) {
        this.movedWorker = movedWorker;
    }

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
