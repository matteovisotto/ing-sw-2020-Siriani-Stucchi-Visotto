package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.messageModel.*;
import it.polimi.ingsw.server.ClientConnection;
import it.polimi.ingsw.utils.PlayerMessage;
import it.polimi.ingsw.observer.Observer;

import java.util.*;

public abstract class Controller implements Observer<Message> {
    protected final Model model;
    protected GodCardController godCardController;
    protected int counter=0;
    protected int answers=0;
    protected Map<Player, ClientConnection> activeClients = new HashMap<>();

    public Controller(Model model){
        super();
        this.model = model;
    }

    protected Model getModel(){
        return model;
    }

    protected abstract  boolean canMove(Worker worker);

    public abstract void move(PlayerMove move);

    public abstract void increaseLevel(PlayerBuild playerBuild) throws IllegalArgumentException;

    protected abstract HashMap<Cell, Boolean> checkCellsAround (Worker worker);

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

    public abstract void checkVictory();
    public synchronized void endGame(NewGameMessage newGameMessage){
        answers++;
        if(newGameMessage.getChoice()=='y'){
            this.counter++;
            activeClients.put(newGameMessage.getPlayer(), newGameMessage.getClientConnection());
            if(counter==model.getNumOfPlayers()){
                //TODO model reset
                model.startOver();
            }
            else if(answers==model.getNumOfPlayers()){
                //TODO return to lobby
            }
        }
        else{
            newGameMessage.getClientConnection().closeConnection();
        }
    }
}
