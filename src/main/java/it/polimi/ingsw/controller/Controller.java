package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;

import java.util.*;

public class Controller implements Observer {

    private Model model;


    public Controller(Model model){
        super();
        this.model = model;
    }

    public void move(int playerId, int workerId, Cell newCell) {
        try {
            Worker worker = model.getPlayer(playerId).getWorker(workerId);
            worker.setCell(newCell);
            worker.getCell().freeCell();
            newCell.useCell();
            model.hasMoved(playerId, workerId);

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
