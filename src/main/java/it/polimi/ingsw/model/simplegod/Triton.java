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

    public int getUsedWorkerID(){
        return this.usedWorkerID;
    }
    public void setUsedWorkerID(int worker){
        this.usedWorkerID =worker;
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public void usePower(List<Object> objectList) {
        Model model = (Model)objectList.get(0);
        model.setNextPhase(getPhase());
        model.setNextPlayerMessage(PlayerMessage.MOVE);
        model.setNextMessageType(MessageType.MOVE);
        model.notifyChanges();
    }

    @Override
    public void beforeMoveHandler(Model model, GodCardController controller, PlayerMove move) {
        if(controller.canMove(move.getPlayer().getWorker(move.getWorkerId()),move.getPlayer())==0){
            if(getUsedWorkerID()!=-1)
                model.loose(move.getPlayer());
        }
    }

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
