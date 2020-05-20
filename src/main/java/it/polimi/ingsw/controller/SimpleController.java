package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.messageModel.*;
import it.polimi.ingsw.server.ClientConnection;
import it.polimi.ingsw.utils.PlayerMessage;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SimpleController extends Controller {

    public SimpleController(Model model){
        super(model);
    }


    @Override
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
    protected synchronized boolean canMove(Worker actualWorker){
        Cell actualCell= actualWorker.getCell();
        for (int x = actualCell.getX() - 1; x <= actualCell.getX() + 1; x++) {
            for (int y = actualCell.getY() - 1; y <= actualCell.getY() + 1; y++) {
                if(x >= 0 && y >= 0 && x < 5 && y < 5){
                    Cell nextCell = model.getBoard().getCell(x,y);
                    if(nextCell.isFree() && !nextCell.equals(actualCell) && (nextCell.getLevel().getBlockId() -  actualCell.getLevel().getBlockId()< 2) && nextCell.getLevel().getBlockId() != 4){
                        return true;
                    }
                }

            }
        }
        return false;
    }
    @Override
    public synchronized void move(PlayerMove move) {
        boolean canMove;
        if(!model.isPlayerTurn(move.getPlayer())){//se non è il turno del giocatore
            move.getView().reportError(PlayerMessage.TURN_ERROR);
            return;
        }
        //qua fa la mossa

        canMove = move.getPlayer().getWorker(move.getWorkerId()).getStatus();
        if(!canMove){
            move.getView().reportError("This worker can't move anywhere");
            return;
        }
        HashMap<Cell, Boolean> availableCells = checkCellsAround(move.getPlayer().getWorker(move.getWorkerId()));

        try{
            if (availableCells.get(model.getBoard().getCell(move.getRow(), move.getColumn())) != null && availableCells.get(model.getBoard().getCell(move.getRow(), move.getColumn()))) {
                try {
                    model.setNextMessageType(MessageType.BUILD);
                    model.setNextPlayerMessage(PlayerMessage.BUILD);
                    model.updatePhase();
                    model.move(move);
                    if(model.getBoard().getCell(move.getRow(), move.getColumn()).getLevel().getBlockId()==3){
                        model.victory(move.getPlayer());
                        if(model.getNumOfPlayers() == 2){
                            model.endGame();
                        }
                    }
                    checkVictory();
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
    @Override
    public synchronized void increaseLevel(PlayerBuild playerBuild) throws IllegalArgumentException {
        if(!model.isPlayerTurn(playerBuild.getPlayer())){//se non è il turno del giocatore
            playerBuild.getView().reportError(PlayerMessage.TURN_ERROR);
            return;
        }

        Cell buildingCell = this.model.getBoard().getCell(playerBuild.getX(), playerBuild.getY()); //ottengo la cella sulla quale costruire
        Blocks level = buildingCell.getLevel();//ottengo l'altezza della cella

        //qui devo fare i controlli
        if(     Math.abs(buildingCell.getX() - (playerBuild.getPlayer().getWorker(playerBuild.getWorkerId()).getCell().getX())) <= 1 &&
                Math.abs(buildingCell.getY() - (playerBuild.getPlayer().getWorker(playerBuild.getWorkerId()).getCell().getY())) <= 1 &&
                (playerBuild.getPlayer().getWorker(playerBuild.getWorkerId()).getCell() != buildingCell) &&
                (buildingCell.getX() >= 0 && buildingCell.getX() < 5) &&
                (buildingCell.getY() >= 0 && buildingCell.getY() < 5) &&
                (buildingCell.getLevel().getBlockId() <= 3) &&
                (buildingCell.isFree())
        ){
            model.setNextMessageType(MessageType.MOVE);
            model.setNextPlayerMessage(PlayerMessage.MOVE);
            model.updatePhase();
            model.updateTurn();
            switch(level.getBlockId()) {
                case 0:
                    model.increaseLevel(buildingCell, Blocks.LEVEL1);
                    break;
                case 1:
                    model.increaseLevel(buildingCell, Blocks.LEVEL2);break;
                case 2:
                    model.increaseLevel(buildingCell, Blocks.LEVEL3);break;
                case 3:
                    model.increaseLevel(buildingCell, Blocks.DOME);break;
                default:
                    throw new IllegalArgumentException();
            }
        }
        else{
            throw new IllegalArgumentException();
        }
        checkVictory();

    }
    @Override
    protected synchronized HashMap<Cell, Boolean> checkCellsAround (Worker actualWorker){
        HashMap<Cell, Boolean> availableCells = new HashMap<>();
        Cell actualWorkerCell = actualWorker.getCell();
        Board board = model.getBoard();
        for (int x = actualWorkerCell.getX() - 1; x <= actualWorkerCell.getX() + 1; x++) {
            for (int y = actualWorkerCell.getY() - 1; y <= actualWorkerCell.getY() + 1; y++) {
                try{
                    availableCells.put(board.getCell(x,y), board.checkCell(x,y,actualWorker));
                }
                catch (IllegalArgumentException e){
                    Cell c= new Cell(x,y);
                    availableCells.put(c, false);
                }
            }
        }
        return availableCells;
    }
    @Override
    public synchronized void checkVictory(){
        int playerCantMove = 0;
        Player[] players = model.getPlayers();
        boolean[] playersBool = new boolean[model.getNumOfPlayers()];
        Arrays.fill(playersBool, false); //do per scontato che tutti i worker si possano muovere
        for(int i=0; i<model.getNumOfPlayers(); i++){
            players[i].getWorker(0).setStatus(canMove(players[i].getWorker(0)));
            players[i].getWorker(1).setStatus(canMove(players[i].getWorker(1)));
            if(!players[i].getWorker(0).getStatus() && !players[i].getWorker(1).getStatus()){// controllo se nessun worker si può muovere
                playerCantMove++;
                playersBool[i] = true;
            }
        }
        if(playerCantMove == model.getNumOfPlayers() -1){
            for(int i = 0; i < players.length; i++){
                if(!playersBool[i]){
                    model.victory(players[i]);
                    if(model.getLeftPlayers() == 2){
                        model.endGame();
                    }
                }
            }
        }
    }
    @Override
    public void update(Message msg) {//la update gestisce i messaggi
        msg.handler(this);
    }
}
