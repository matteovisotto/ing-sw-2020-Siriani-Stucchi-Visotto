package it.polimi.ingsw.model.simplegod;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.GodCard;
import it.polimi.ingsw.model.Gods;
import it.polimi.ingsw.model.Phase;
import it.polimi.ingsw.model.messageModel.PlayerBuild;

public class Zeus extends GodCard {
    /**
     * {@inheritDoc}
     */
    public Zeus() {
        super(Gods.ZEUS, Phase.BUILD);
    }
    
    @Override
    public boolean checkBuilt(Controller controller, Cell buildingCell, PlayerBuild playerBuild) {
        if(buildingCell.getLevel().getBlockId()==3 && buildingCell.equals(playerBuild.getPlayer().getWorker(playerBuild.getWorkerId()).getCell())){
            return false;
        }
        return Math.abs(buildingCell.getX() - (playerBuild.getPlayer().getWorker(playerBuild.getWorkerId()).getCell().getX())) <= 1 &&
                Math.abs(buildingCell.getY() - (playerBuild.getPlayer().getWorker(playerBuild.getWorkerId()).getCell().getY())) <= 1 &&
                (buildingCell.getX() >= 0 && buildingCell.getX() < 5) &&
                (buildingCell.getY() >= 0 && buildingCell.getY() < 5) &&
                (buildingCell.getLevel().getBlockId() <= 3);
    }

}
