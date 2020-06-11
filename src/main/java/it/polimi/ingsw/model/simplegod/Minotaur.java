
package it.polimi.ingsw.model.simplegod;

import it.polimi.ingsw.controller.GodCardController;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.messageModel.PlayerMove;

import java.util.ArrayList;
import java.util.List;

/**
 This class is intended to represent the Minotaur's GodCard
 */
public class Minotaur extends GodCard {

    /**
     * {@inheritDoc}
     */
    public Minotaur() {
        super(Gods.MINOTAUR, Phase.MOVE);
    }

    /**
     * This method makes a player's worker move into an enemy worker's cell, forcing him to move one space straight backwards to an unoccupied space at any level; it could be used only if the player decide to activate his power.
     * @param objectList contain the worker of the Minotaur's player (objectList.get(0)), the enemy's worker (objectList.get(1)) and the cell straight backwards of the enemy's worker (objectList.get(2)).
     */
    @Override
    public void usePower(List<Object> objectList) {
        Worker minotaurWorker = (Worker)objectList.get(0);
        Worker enemyWorker = (Worker)objectList.get(1);
        Cell behindCell = (Cell)objectList.get(2);
        Cell c, c2;
        //scambia le celle dei worker
        behindCell.useCell();
        c = enemyWorker.getCell();
        enemyWorker.setCell(behindCell);
        c2 = minotaurWorker.getCell();
        minotaurWorker.setCell(c);
        c2.freeCell();


    }

    /**
     * Modifed control for Minotaur
     * @param x the x value of the cell
     * @param y the y value of the cell
     * @param actualWorker the worker who is performing the move
     * @param maxUpDifference the max step the worker can move up, normal 1, with some gods could be heigher
     * @return true if it can move in the selected cell
     * @throws IllegalArgumentException if cell values are not between 0 and 4
     */
    @Override
    public boolean checkCell (GodCardController controller, int x, int y, Worker actualWorker, int maxUpDifference) throws IllegalArgumentException{
        Cell nextCell = controller.getModel().getBoard().getCell(x,y);
        Cell actualCell = actualWorker.getCell();
        Cell behindCell;
        boolean isEnemyPushable = false;
        for (Player player : controller.getModel().getBoard().getPlayers()) {
            //controllo che il player non sia quello del turno
            if (!player.getGodCard().getCardGod().equals(Gods.MINOTAUR)) {
                if (!nextCell.isFree() && (player.getWorker(0).getCell() == nextCell || player.getWorker(1).getCell() == nextCell)) {
                    behindCell = controller.getModel().getBoard().getCell((nextCell.getX() - actualCell.getX()) + nextCell.getX(), (nextCell.getY() - actualCell.getY()) + nextCell.getY());
                    if (behindCell.getLevel().getBlockId() != 4 && behindCell.isFree()) {
                        isEnemyPushable = true;
                    }
                }
            }
        }
        return (nextCell.isFree() || isEnemyPushable) && !nextCell.equals(actualCell) && (nextCell.getLevel().getBlockId() - actualCell.getLevel().getBlockId() < maxUpDifference) && nextCell.getLevel().getBlockId() != 4;
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

            objectList.add(model.getBoard().getCell((((Worker)objectList.get(1)).getCell().getX() - ((Worker)objectList.get(0)).getCell().getX()) + ((Worker)objectList.get(1)).getCell().getX(), (((Worker)objectList.get(1)).getCell().getY() - ((Worker)objectList.get(0)).getCell().getY()) + ((Worker)objectList.get(1)).getCell().getY()));
            move.getPlayer().getGodCard().usePower(objectList);
            model.getActualPlayer().setUsedWorker(move.getWorkerId());
            model.notifyChanges();
            return true;
        }
        return false;
    }
}
