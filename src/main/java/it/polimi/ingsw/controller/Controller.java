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
        Player p = model.getActualPlayer();
        Phase ph = p.getGodCard().getPhase();
        return ph == model.getPhase();
    }

    public synchronized void move(PlayerMove move) {
        boolean test = false;
        if(!model.isPlayerTurn(move.getPlayer())){//se non è il turno del giocatore
            move.getView().reportError(PlayerMessage.TURN_ERROR);
            return;
        }
        //qua fa la mossa
        if(!move.getPlayer().getWorker(move.getWorkerId()).getStatus()){
            move.getView().reportError("This worker can't move anywhere");
            return;
        }
        HashMap<Cell, Boolean> availableCells=checkCellsAround(move.getPlayer().getWorker(move.getWorkerId()));
        for(Cell c:availableCells.keySet()){
            if(availableCells.get(c)){
                test = true;
            }
        }
        try{
            if(!test){
                move.getPlayer().getWorker(move.getWorkerId()).setStatus(false);
                move.getView().reportError("This worker can't move anywhere");
            } else if (availableCells.get(model.getBoard().getCell(move.getRow(), move.getColumn())) != null && availableCells.get(model.getBoard().getCell(move.getRow(), move.getColumn()))) {
                try {
                    model.setNextMessageType(MessageType.BUILD);
                    model.setNextPlayerMessage(PlayerMessage.BUILD);
                    model.updatePhase();
                    model.move(move);
                    resetWorkerStatus(move);

                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println(e.getMessage());
                }
            } else {
                move.getView().reportError("Unable to move in selected position");
            }
        }catch(IllegalArgumentException e){
            move.getView().reportError("Cell index must be between 0 and 4 (included)");


        }

    }

    private synchronized void resetWorkerStatus(PlayerMove move){
        Cell cell = model.getBoard().getCell(move.getRow(), move.getColumn());
        Player[] players = model.getPlayers();
        for(int i = 0; i < players.length; i++){
            if(Math.abs(players[i].getWorker(0).getCell().getX() - cell.getX()) < 2 && Math.abs(players[i].getWorker(0).getCell().getY() - cell.getY()) < 2){
                model.resetWorkerStatus(players[i].getWorker(0));
            }
            if(Math.abs(players[i].getWorker(1).getCell().getX() - cell.getX()) < 2 && Math.abs(players[i].getWorker(1).getCell().getY() - cell.getY()) < 2){
                model.resetWorkerStatus(players[i].getWorker(1));
            }
        }
    }

    public synchronized void increaseLevel(PlayerBuild playerBuild) throws IllegalArgumentException {
        if(!model.isPlayerTurn(playerBuild.getPlayer())){//se non è il turno del giocatore
            playerBuild.getView().reportError(PlayerMessage.TURN_ERROR);
            return;
        }
        Cell cell=this.model.getBoard().getCell(playerBuild.getX(), playerBuild.getY());
        Blocks level = cell.getLevel();
        model.setNextMessageType(MessageType.MOVE);
        model.setNextPlayerMessage(PlayerMessage.MOVE);
        model.updatePhase();
        model.updateTurn();
        switch(level.getBlockId()) {
            case 0:
                model.increaseLevel(cell, Blocks.LEVEL1);
                break;
            case 1:
                model.increaseLevel(cell, Blocks.LEVEL2);break;
            case 2:
                model.increaseLevel(cell, Blocks.LEVEL3);break;
            case 3:
                model.increaseLevel(cell, Blocks.DOME);break;
            default:
                throw new IllegalArgumentException();
        }

    }

    private synchronized HashMap<Cell, Boolean> checkCellsAround (Worker worker){
        HashMap<Cell, Boolean> availableCells = new HashMap<>();
        Cell cell = worker.getCell();
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
        try{
            if(model.getBoard().getCell(playerWorker.getX(), playerWorker.getY()).isFree()){
                if(model.getPhase() == Phase.SETWORKER2){
                    if(model.getActualPlayerId() != model.getNumOfPlayers() - 1){
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
        }catch (IllegalArgumentException e){
            playerWorker.getView().reportError("Cell index must be between 0 and 4 (included)");
        }

    }

    @Override
    public void update(Message msg) {//la update gestisce i messaggi
        msg.handler(this);
    }

}
