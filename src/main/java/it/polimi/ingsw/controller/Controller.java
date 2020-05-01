package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.messageModel.*;
import it.polimi.ingsw.utils.PlayerMessage;
import it.polimi.ingsw.observer.Observer;

import java.util.*;

public class Controller implements Observer<Message> {
    private final Model model;
    private GodCardController godCardController;

    public Controller(Model model){
        super();
        this.model = model;
    }

    protected Model getModel(){
        return model;
    }

    private synchronized boolean checkPhase(){//deve controllare che la fase attuale sia la stessa del godpower
        Player p= model.getActualPlayer();
        Phase ph=p.getGodCard().getPhase();
        return ph==model.getPhase();
    }

    public synchronized void move(PlayerMove move) {
        if(!model.isPlayerTurn(move.getPlayer())){
            move.getView().reportError(PlayerMessage.TURN_ERROR);
            return;
        }
        //qua fa la mossa
        HashMap<Cell, Boolean> availableCells=checkCellsAround(move.getPlayer().getWorker(move.getWorkerId()));
        if (availableCells.get(model.getBoard().getCell(move.getRow(), move.getColumn()))) {
            try {
                model.setNextMessageType(MessageType.BUILD);
                model.setNextPlayerMessage(PlayerMessage.BUILD);
                model.updatePhase();
                model.move(move);
            } catch (ArrayIndexOutOfBoundsException e) {
                System.err.println(e.getMessage());
            }
        } else {
            move.getView().reportError("Unable to move in selected position");
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
    }

    public synchronized void setPlayerWorker(PlayerWorker playerWorker){
        //Check for right turn
        if(!model.isPlayerTurn(playerWorker.getPlayer())){
            playerWorker.getView().reportError(PlayerMessage.TURN_ERROR);
            return;
        }

        if(model.getBoard().getCell(playerWorker.getX(), playerWorker.getY()).isFree()){
            if(model.getPhase()==Phase.SETWORKER2){
                if(model.getActualPlayerId()!=model.getNumOfPlayers()-1){
                    model.updateTurn();
                    model.setNextPhase(Phase.SETWORKER1);
                    model.setNextMessageType(MessageType.SET_WORKER_1);
                    model.setNextPlayerMessage(PlayerMessage.PLACE_FIRST_WORKER);
                }
                else{
                    model.updateTurn();
                    model.updatePhase();
                    model.setNextMessageType(MessageType.MOVE);
                    model.setNextPlayerMessage(PlayerMessage.MOVE);
                }
            }
            else{
                model.updatePhase();
                model.setNextMessageType(MessageType.SET_WORKER_2);
                model.setNextPlayerMessage(PlayerMessage.PLACE_SECOND_WORKER);
            }
            model.setPlayerWorker(playerWorker);
        }
        else{
            playerWorker.getView().reportError("The cell is busy.");
        }
    }

    @Override
    public void update(Message msg) {//la update gestisce i messaggi
        msg.handler(this);
    }

}
