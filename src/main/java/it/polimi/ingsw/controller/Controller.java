package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.messageModel.*;
import it.polimi.ingsw.server.ClientConnection;
import it.polimi.ingsw.server.Server;
import it.polimi.ingsw.utils.PlayerMessage;
import it.polimi.ingsw.observer.Observer;

import java.util.*;

public abstract class Controller implements Observer<Message> {
    protected final Model model;
    protected GodCardController godCardController;
    protected SimpleController simpleController;
    protected ArrayList<String> playersName = new ArrayList<>();
    protected ArrayList<ClientConnection> clientConnections = new ArrayList<>();
    protected int counter = 0;
    protected int answers = 0;
    protected Map<Player, ClientConnection> activeClients = new LinkedHashMap<>();
    protected Server server;
    protected Player player;
    protected String lobbyName;

    public Controller(Model model){
        super();
        this.model = model;
    }

    public Model getModel(){
        return model;
    }

    protected abstract boolean canMove(Worker worker);

    public abstract void move(PlayerMove move);

    public abstract void increaseLevel(PlayerBuild playerBuild) throws IllegalArgumentException;

    protected abstract HashMap<Cell, Boolean> checkCellsAround (Worker worker);

    public abstract void setPlayerWorker(PlayerWorker playerWorker);

    public abstract void checkVictory();

    public synchronized void endGame(NewGameMessage newGameMessage){
        int i = 0;
        answers++;
        if(newGameMessage.getChoice() == 'y'){
            this.counter++;
            activeClients.put(newGameMessage.getPlayer(), newGameMessage.getClientConnection());
            if(counter == model.getNumOfPlayers()){
                //TODO model reset
                model.startOver();
            }
            else if(answers == model.getNumOfPlayers()){
                //TODO return to lobby
                //Map.Entry<Player,ClientConnection> entry = activeClients.entrySet().iterator().next();
                /*for (Map.Entry<Player, ClientConnection> names: activeClients.entrySet()) {
                    this.playersName.add(names.getKey().getPlayerName());
                    this.clientConnections.add(names.getValue());
                }
                lobbyName = newGameMessage.getLobby().getLobbyName();*/
                //server.addLobbyEndGame(lobbyName,clientConnections,playersName,answers, model.getGods() == null);

            }
        }
        else{
            newGameMessage.getClientConnection().closeConnection();
        }
    }
}
