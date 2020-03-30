package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.utils.PlayerMessage;

import java.util.*;

public class Controller implements Observer {

    private final Model model;

    public Controller(Model model){
        super();
        this.model = model;
    }

    private synchronized void move(PlayerMove move) {
        if(!model.isPlayerTurn(move.getPlayer())){
            move.getView().reportError(PlayerMessage.TURN_ERROR);
            return;
        }
        try {
            model.move(move);
            model.updateTurn();
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println(e.getMessage());
        }
    }

    private synchronized void increaseLevel(Cell cell) throws IllegalArgumentException {
        Blocks level = this.model.getBoard().getCell(cell.getX(),cell.getY()).getLevel();
        switch(level.getBlockId()) {
            case 0:
                cell.setLevel(Blocks.LEVEL1);
            case 1:
                cell.setLevel(Blocks.LEVEL2);
            case 2:
                cell.setLevel(Blocks.LEVEL3);
            case 3:
                cell.setLevel(Blocks.DOME);
            default:
                throw new IllegalArgumentException();
        }
    }

    private synchronized void checkCellsAround (Cell cell, Worker worker){
        HashMap<Cell, Boolean> availableCells = new HashMap<>();
        Board board = model.getBoard();
        for (int x = cell.getX() - 1; x <= cell.getX() + 1; x++) {
            for (int y = cell.getY() - 1; y <= cell.getY() + 1; y++) {
                try{
                    availableCells.put(board.getCell(x,y), board.checkCell(x,y,worker));
                }
                catch (IllegalArgumentException e){
                    System.err.println(e.getMessage());
                }
            }
        }
        model.setChanges(availableCells);
    }

    private synchronized void setPlayerWorker (PlayerWorker playerWorker){
        model.setPlayerWorker(playerWorker);
    }

    @Override
    public void update(Observable o, Object arg) {
        if(arg instanceof PlayerMove){
            move((PlayerMove) arg);
        }
        else if (arg instanceof PlayerWorker) {
            setPlayerWorker((PlayerWorker) arg);
        }
    }
}
