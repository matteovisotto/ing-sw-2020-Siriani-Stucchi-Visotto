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

    /**
     * This method override standard check built
     * As documented Zeus can build also in the same cell where the used worker is.
     * It controls if the cell level is not the third one and the cell in the one of the selected worker
     * @param controller the play controller
     * @param buildingCell the cell where player wants to build
     * @param playerBuild the message received by the view
     * @return true if
     */
    @Override
    public boolean checkBuilt(Controller controller, Cell buildingCell, PlayerBuild playerBuild) {

        if(buildingCell.getLevel().getBlockId()==3 && buildingCell.equals(playerBuild.getPlayer().getWorker(playerBuild.getWorkerId()).getCell())){
            return false;
        }

        return Math.abs(buildingCell.getX() - (playerBuild.getPlayer().getWorker(playerBuild.getWorkerId()).getCell().getX())) <= 1 &&
                Math.abs(buildingCell.getY() - (playerBuild.getPlayer().getWorker(playerBuild.getWorkerId()).getCell().getY())) <= 1 &&
                (playerBuild.getPlayer().getWorker((playerBuild.getWorkerId()+1)%2).getCell() != buildingCell) &&
                (buildingCell.getX() >= 0 && buildingCell.getX() < 5) &&
                (buildingCell.getY() >= 0 && buildingCell.getY() < 5) &&
                (buildingCell.getLevel().getBlockId() <= 3);
    }

}
