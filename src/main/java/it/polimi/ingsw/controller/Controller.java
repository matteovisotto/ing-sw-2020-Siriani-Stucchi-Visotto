package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.messageModel.Message;
import it.polimi.ingsw.model.messageModel.PlayerMove;
import it.polimi.ingsw.model.messageModel.PlayerWorker;
import it.polimi.ingsw.utils.PlayerMessage;
import it.polimi.ingsw.observer.Observer;

import java.util.*;

public class Controller implements Observer<Message> {

    private final Model model;
    private GodCardController GCC;
    public Controller(Model model){
        super();
        this.model = model;
    }

    private synchronized void move(PlayerMove move) {
        if(!model.isPlayerTurn(move.getPlayer())){
            move.getView().reportError(PlayerMessage.TURN_ERROR);
            return;
        }
        //qua fa la mossa
        HashMap<Cell, Boolean> availableCells=checkCellsAround(move.getPlayer().getWorker(move.getWorkerId()));
        if (availableCells.get(model.getBoard().getCell(move.getRow(), move.getColumn()))) {
            try {
                model.move(move);
                //model.updateTurn();
            } catch (ArrayIndexOutOfBoundsException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    private synchronized void increaseLevel(Cell cell) throws IllegalArgumentException {
        Blocks level = this.model.getBoard().getCell(cell.getX(),cell.getY()).getLevel();
        switch(level.getBlockId()) {
            case 0:
                model.increaseLevel(cell, Blocks.LEVEL1);
            case 1:
                model.increaseLevel(cell, Blocks.LEVEL2);
            case 2:
                model.increaseLevel(cell, Blocks.LEVEL3);
            case 3:
                model.increaseLevel(cell, Blocks.DOME);
            default:
                throw new IllegalArgumentException();
        }
    }

    private synchronized HashMap<Cell, Boolean> checkCellsAround (Worker worker){
        HashMap<Cell, Boolean> availableCells = new HashMap<>();
        Cell cell= worker.getCell();
        Board board = model.getBoard();
        for (int x = cell.getX() - 1; x <= cell.getX() + 1; x++) {
            for (int y = cell.getY() - 1; y <= cell.getY() + 1; y++) {
                try{
                    availableCells.put(board.getCell(x,y), board.checkCell(x,y,worker));
                }
                catch (IllegalArgumentException e){
                    Cell c= new Cell(x,y);
                    availableCells.put(c, false);
                }
            }
        }
        return availableCells;
        //model.setChanges(availableCells);
    }

    private synchronized void setPlayerWorker (PlayerWorker playerWorker){
        model.setPlayerWorker(playerWorker);
    }

    @Override
    public void update(Message msg) {
        /*if(msg.getPlayer()==model.getActualPlayer() && !model.getActualPlayer().getGodCard().equals(SimpleGods.ATHENA) ){ && player decided to use power
            switch(model.getActualPlayer().getGodCard().getCardGod().getSimpleGodId()){
                case 1://APOLLO

            }
        }*/
        if(msg instanceof PlayerMove){
            move((PlayerMove) msg);
        }
        else if (msg instanceof PlayerWorker) {
            setPlayerWorker((PlayerWorker) msg);
        }

    }

}
