package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.utils.PlayerMessage;

import java.util.*;

public class Controller implements Observer {

    private Model model;


    public Controller(Model model){
        super();
        this.model = model;
    }

    public void move(PlayerMove move) {
        if(!model.isPlayerTurn(move.getPlayer())){
            move.getView().reportError(PlayerMessage.TURN_ERROR);
            return;
        }
        try {
            Worker worker = move.getPlayer().getWorker(move.getWorkerId());
            worker.setCell(model.getBoard().getCell(move.getRow(), move.getColumn()));
            worker.getCell().freeCell();
            model.getBoard().getCell(move.getRow(), move.getColumn()).useCell();
            model.hasMoved(move.getPlayer(), move.getWorkerId());
            model.updateTurn();
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println(e.getMessage());
        }
    }

    public void increaseLevel(Cell cell) throws IllegalArgumentException {
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

    public void checkCellsAround (Cell cell, Worker worker){
        HashMap<Cell, Boolean> availableCells = new HashMap<>();
        Board board = model.getBoard();
        for (int x = cell.getX()-1; x<=cell.getX()+1; x++) {
            for (int y = cell.getY()-1; y<=cell.getY()+1; y++) {
                try{
                    availableCells.put(board.getCell(x,y), board.checkCell(x,y,worker));
                }
                catch (IllegalArgumentException e){
                }
            }
        }
        model.setChanges(availableCells);
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
