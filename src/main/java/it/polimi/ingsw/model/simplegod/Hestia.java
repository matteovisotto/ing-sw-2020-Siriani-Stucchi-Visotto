package it.polimi.ingsw.model.simplegod;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.GodCardController;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.messageModel.MessageType;
import it.polimi.ingsw.model.messageModel.PlayerBuild;
import it.polimi.ingsw.utils.PlayerMessage;

import java.util.List;

public class Hestia extends GodCard {
    private boolean hasBuilt=false;
    /**
     * {@inheritDoc}
     */
    public Hestia() {
        super(Gods.HESTIA, Phase.BUILD);
    }

    public void setHasBuilt(boolean hasBuilt){
        this.hasBuilt=hasBuilt;
    }

    public boolean hasBuilt(){
        return this.hasBuilt;
    }

    /**
     * {@inheritDoc}
     * In this case the only parameters is the model,
     * If this function is called, let the player to build the first time and set te model params
     * for letting him to build again
     */
    @Override
    public void usePower(List<Object> objectList) {
        this.hasBuilt=true;
        Model model = (Model)objectList.get(0);
        model.setNextPhase(getPhase());
        model.setNextPlayerMessage(PlayerMessage.BUILD);
        model.setNextMessageType(MessageType.BUILD);
        model.notifyChanges();
    }

    @Override
    public boolean checkBuilt(Controller controller, Cell buildingCell, PlayerBuild playerBuild) {
        if(hasBuilt()){
            return Math.abs(buildingCell.getX() - (playerBuild.getPlayer().getWorker(playerBuild.getWorkerId()).getCell().getX())) <= 1 &&
                    Math.abs(buildingCell.getY() - (playerBuild.getPlayer().getWorker(playerBuild.getWorkerId()).getCell().getY())) <= 1 &&
                    (playerBuild.getPlayer().getWorker(playerBuild.getWorkerId()).getCell() != buildingCell) &&
                    (buildingCell.getX() > 0 && buildingCell.getX() < 4) &&
                    (buildingCell.getY() > 0 && buildingCell.getY() < 4) &&
                    (buildingCell.getLevel().getBlockId() <= 3) &&
                    (buildingCell.isFree());
        }
        return super.checkBuilt(controller,buildingCell, playerBuild);
    }

    @Override
    public boolean handlerBuild(Model model, GodCardController controller, PlayerBuild build, Cell buildingCell) {
        if(!hasBuilt() && checkHestiaCells(model, build)){
            model.setNextPhase(Phase.WAIT_GOD_ANSWER);
            model.setNextPlayerMessage(PlayerMessage.USE_POWER);
            model.setNextMessageType(MessageType.USE_POWER);
            controller.godIncreaseLevel(buildingCell.getLevel().getBlockId(), buildingCell);
            return true;
        }

        setHasBuilt(false);
        return false;
    }

    private boolean checkHestiaCells(Model model, PlayerBuild playerBuild){
        int x=playerBuild.getPlayer().getWorker(playerBuild.getWorkerId()).getCell().getX();
        int y=playerBuild.getPlayer().getWorker(playerBuild.getWorkerId()).getCell().getY();
        Board board=model.getBoard();
        for(int i=x-1; i<=x+1; i++){
            for(int j=y-1; j<=y+1; j++){
                try{
                    if(board.getCell(i,j).getLevel().getBlockId() < 4 && i!=0 && i!=4 && j!=0 && j!=4 && board.getCell(i,j).isFree()){
                        return true;
                    }
                }catch(Exception e){
                    //ignore
                }
            }
        }
        return false;
    }
}
