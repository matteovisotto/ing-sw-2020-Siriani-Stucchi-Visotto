
package it.polimi.ingsw.model.simplegod;

import it.polimi.ingsw.controller.GodCardController;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.messageModel.PlayerMove;

import java.util.ArrayList;
import java.util.List;

/**
 This class is intended to represent the Apollo's GodCard
 */
public class Apollo extends GodCard {

    /**
     * {@inheritDoc}
     */
    public Apollo(){
        super(Gods.APOLLO, Phase.MOVE);
    }

    /**
     * This method switches the position of two different workers; it could be used only if the player decide to activate his power
     * @param objectList contain the positions of the two different workers; objectList.get(0) = Apollo's worker; objectList.get(1) = enemy's worker
     */
    @Override
    public void usePower(List<Object> objectList) {
        Worker apolloWorker = (Worker)objectList.get(0);
        Worker enemyWorker = (Worker)objectList.get(1);
        Cell cell;
        //scambia le celle dei worker
        cell = enemyWorker.getCell();
        enemyWorker.setCell(apolloWorker.getCell());
        apolloWorker.setCell(cell);
    }

    /**
     * Modifed control for Apollo
     * @param x the x value of the cell
     * @param y the y value of the cell
     * @param actualWorker the worker who is performing the move
     * @param maxUpDifference the max step the worker can move up, normal 1, with some gods could be heigher
     * @return true if it can move in the selected cell
     * @throws IllegalArgumentException if cell values are not between 0 and 4
     */
    @Override
    public boolean checkCell(GodCardController controller, int x, int y, Worker actualWorker, int maxUpDifference) throws IllegalArgumentException{
        Cell nextCell = controller.getModel().getBoard().getCell(x,y);
        Cell actualCell = actualWorker.getCell();
        boolean isPlayerSwitchable = false;
        for (Player player : controller.getModel().getBoard().getPlayers()) {
            //controllo che il player non sia quello del turno
            if (!player.getGodCard().getCardGod().equals(Gods.APOLLO)) {
                if ((player.getWorker(0).getCell() == nextCell || player.getWorker(1).getCell() == nextCell)) {
                    isPlayerSwitchable = true;
                }
            }
        }
        return (nextCell.isFree() || isPlayerSwitchable) && !nextCell.equals(actualCell) && (nextCell.getLevel().getBlockId() -  actualCell.getLevel().getBlockId()< maxUpDifference) && nextCell.getLevel().getBlockId() != 4;
    }

    @Override
    public boolean handlerMove(Model model, GodCardController controller, PlayerMove move) {
        if(!model.getBoard().getCell(move.getRow(), move.getColumn()).isFree()){
            List<Object> objectList = new ArrayList<>();
            //primo worker di quello che vuole muovere
            objectList.add(move.getPlayer().getWorker(move.getWorkerId()));
            for(int i = 0; i < model.getNumOfPlayers(); i++){
                if(model.getPlayer(i).getGodCard().getCardGod() != move.getPlayer().getGodCard().getCardGod()){
                    if(model.getPlayer(i).getWorker(0).getCell() == model.getBoard().getCell(move.getRow(), move.getColumn())){
                        objectList.add(model.getPlayer(i).getWorker(0));
                    }
                    else if(model.getPlayer(i).getWorker(1).getCell() == model.getBoard().getCell(move.getRow(), move.getColumn())){
                        objectList.add(model.getPlayer(i).getWorker(1));
                    }
                }
            }
            move.getPlayer().getGodCard().usePower(objectList);
            model.getActualPlayer().setUsedWorker(move.getWorkerId());
            model.notifyChanges();
            return true;
        }
        return false;
    }
}
