package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.messageModel.*;
import it.polimi.ingsw.server.ClientConnection;
import it.polimi.ingsw.utils.PlayerMessage;
import it.polimi.ingsw.observer.Observer;

import java.util.*;

public abstract class Controller implements Observer<Message> {
    protected final Model model;
    protected final ArrayList<String> playersName = new ArrayList<>();
    protected final ArrayList<ClientConnection> clientConnections = new ArrayList<>();
    protected int counter = 0;
    protected int answers = 0;
    protected final Map<Player, ClientConnection> activeClients = new LinkedHashMap<>();

    public Controller(Model model){
        super();
        this.model = model;
    }

    public Model getModel(){
        return model;
    }

    public synchronized boolean turnCheck(Message message){
        if(!model.isPlayerTurn(message.getPlayer())){
            message.getView().reportError(PlayerMessage.TURN_ERROR);
            return false;
        }
        return true;
    }

    public synchronized void increaseLevel(int blockId, Cell buildingCell){
        switch(blockId) {
            case 0:
                model.increaseLevel(buildingCell, Blocks.LEVEL1);
                break;
            case 1:
                model.increaseLevel(buildingCell, Blocks.LEVEL2);
                break;
            case 2:
                model.increaseLevel(buildingCell, Blocks.LEVEL3);
                break;
            case 3:
                model.increaseLevel(buildingCell, Blocks.DOME);
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    protected synchronized boolean checkBuild(Cell buildingCell, PlayerBuild playerBuild){
        return Math.abs(buildingCell.getX() - (playerBuild.getPlayer().getWorker(playerBuild.getWorkerId()).getCell().getX())) <= 1 &&
                Math.abs(buildingCell.getY() - (playerBuild.getPlayer().getWorker(playerBuild.getWorkerId()).getCell().getY())) <= 1 &&
                (playerBuild.getPlayer().getWorker(playerBuild.getWorkerId()).getCell() != buildingCell) &&
                (buildingCell.getX() >= 0 && buildingCell.getX() < 5) &&
                (buildingCell.getY() >= 0 && buildingCell.getY() < 5) &&
                (buildingCell.getLevel().getBlockId() <= 3) &&
                (buildingCell.isFree());
    }

    protected synchronized void checkVictory(){
        Player[] players = model.getPlayers();
        //in questo for controllo
        try{
            for(int i = 0; i < model.getNumOfPlayers(); i++){
                players[i].getWorker(0).setStatus(canMove(players[i].getWorker(0), players[i]));
                players[i].getWorker(1).setStatus(canMove(players[i].getWorker(1), players[i]));
                //se tutti e due non si possono muovere
                if(!players[i].getWorker(0).getStatus() && !players[i].getWorker(1).getStatus()){// controllo se nessun worker si può muovere
                    model.loose(players[i]);
                }
            }
        }catch(IndexOutOfBoundsException e){
            //ignore
        }

    }

    public synchronized void endGame(NewGameMessage newGameMessage){
        answers++;
        if(newGameMessage.getChoice() == 'y'){
            this.counter++;
            activeClients.put(newGameMessage.getPlayer(), newGameMessage.getClientConnection());
            if(counter == model.getNumOfPlayers()){
                counter=0;
                answers=0;
                activeClients.clear();
                playersName.clear();
                model.startOver();
            } else if(answers == model.getNumOfPlayers()){
                for (Map.Entry<Player, ClientConnection> names: activeClients.entrySet()) {
                    this.playersName.add(names.getKey().getPlayerName());
                    this.clientConnections.add(names.getValue());
                }
                EndGameServerMessage endGameServerMessage = new EndGameServerMessage(newGameMessage.getLobby(),clientConnections,playersName,answers, model.isSimplePlay());
                newGameMessage.getClientConnection().send(endGameServerMessage);
            }

        } else {
            if(answers == model.getNumOfPlayers() && counter != 0){
                for (Map.Entry<Player, ClientConnection> names: activeClients.entrySet()) {
                    this.playersName.add(names.getKey().getPlayerName());
                    this.clientConnections.add(names.getValue());
                }
                EndGameServerMessage endGameServerMessage = new EndGameServerMessage(newGameMessage.getLobby(),clientConnections,playersName,answers, model.isSimplePlay());
                newGameMessage.getClientConnection().send(endGameServerMessage);
            }

            newGameMessage.getClientConnection().closeConnection();
        }

    }

    protected synchronized boolean canMove(Worker worker, Player player){
        Cell actualCell = worker.getCell();
        for (int x = actualCell.getX() - 1; x <= actualCell.getX() + 1; x++) {
            for (int y = actualCell.getY() - 1; y <= actualCell.getY() + 1; y++) {
                if(x >= 0 && y >= 0 && x < 5 && y < 5){
                    Cell nextCell = model.getBoard().getCell(x,y);
                    if(nextCell.isFree() && !nextCell.equals(actualCell) && (nextCell.getLevel().getBlockId() -  actualCell.getLevel().getBlockId() < 2) && nextCell.getLevel().getBlockId() != 4){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    protected abstract HashMap<Cell, Boolean> checkCellsAround (Worker worker);

    public abstract void setPlayerWorker(PlayerWorker playerWorker);

    public abstract void move(PlayerMove move);

    public abstract void build(PlayerBuild playerBuild) throws IllegalArgumentException;


}
