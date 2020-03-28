package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.utils.PlayerMessage;
import it.polimi.ingsw.view.RemoteView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lobby {
    private int numPlayers;
    private String lobbyName;
    private boolean isFull = false;
    private List<ClientConnection> connections = new ArrayList<ClientConnection>();
    private Map<String, ClientConnection> waitingConnection = new HashMap<>();
    private Map<ClientConnection, ClientConnection> playingConnection = new HashMap<>();

    public Lobby(String lobbyName, String playerName, ClientConnection c, int numPlayers){
        this.numPlayers = numPlayers;
        this.lobbyName = lobbyName;
        connections.add(c);
        waitingConnection.put(playerName, c);
    }

    public String getLobbyName(){
        return this.lobbyName;
    }

    public boolean isFull(){
        return this.isFull;
    }

    public void closeLobby() {
        for(int i=connections.size()-1; i>=0; i--){
            ClientConnection clientConnection = connections.get(i);
            clientConnection.closeConnection();
            connections.remove(i);
        }
        waitingConnection.clear();
        playingConnection.clear();
    }

    public void addPlayer(String playerName, ClientConnection c) {
        connections.add(c);
        waitingConnection.put(playerName, c);
        if(waitingConnection.size() == this.numPlayers){    //appena si riempie la stanza
            this.isFull = true;
            ClientConnection c1,c2,c3=null;
            Player p1,p2,p3;
            Player[] playerArray = new Player[this.numPlayers];
            RemoteView rv1,rv2,rv3=null;
            List<String> keys = new ArrayList<>(waitingConnection.keySet());//creo un array di stringhe contenente i nomi dei giocatori
            //se si è in due o in 3
            c1 = waitingConnection.get(keys.get(0));
            c2 = waitingConnection.get(keys.get(1));
            p1 = new Player(keys.get(0));
            p2 = new Player(keys.get(1));
            playerArray[0] = p1;
            playerArray[1] = p2;
            //se si è in 3
            if(this.numPlayers==3){                          //se si è scelto di giocare con 3 giocatori
                c3 = waitingConnection.get(keys.get(2));    //stesse operazioni fatte sopra
                p3 = new Player(keys.get(2));
                playerArray[2] = p3;
                rv1 = new RemoteView(p1, keys.get(1), keys.get(2), c1);
                rv2 = new RemoteView(p2, keys.get(0), keys.get(2), c2);
                rv3 = new RemoteView(p3, keys.get(1), keys.get(0), c3);
            } else {
                rv1 = new RemoteView(p1, keys.get(1), c1);
                rv2 = new RemoteView(p2, keys.get(0), c2);
            }
            Model model = new Model(playerArray);
            Controller controller = new Controller(model);
            model.addObserver(rv1);
            model.addObserver(rv2);
            rv1.addObserver(controller);
            rv2.addObserver(controller);
            if(numPlayers==3){
                model.addObserver(rv3);
                rv3.addObserver(controller);
                playingConnection.put(c1, c2);
                playingConnection.put(c2, c3);
                playingConnection.put(c3, c1);
            }
            else{
                playingConnection.put(c1, c2);
                playingConnection.put(c2, c1);
            }
            waitingConnection.clear();
            //this.numPlayer=0; //When multithread
            //Gestione turni
            c1.asyncSend(PlayerMessage.START_PLAY);
            c2.asyncSend(PlayerMessage.START_PLAY);
            if(this.numPlayers == 3) c3.asyncSend(PlayerMessage.START_PLAY);
        }
    }

    private void twoPlayer(){

    }

    private void threePlayer() {

    }


}
