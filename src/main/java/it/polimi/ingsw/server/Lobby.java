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
    private List<ClientConnection> connections = new ArrayList<>();
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
            List<String> players = new ArrayList<>(waitingConnection.keySet());
            waitingConnection.clear();
            if(this.numPlayers==2){
                twoPlayer(players);
            } else {
                threePlayer(players);
            }

        }
    }

    private void sendAllPlayer(Object o){
        for(int i=0; i<connections.size(); i++){
            connections.get(i).asyncSend(o);
        }
    }

    private void twoPlayer(List<String> players){
        ClientConnection c1,c2;
        Player p1,p2;
        Player[] playerArray = new Player[this.numPlayers];
        RemoteView rv1,rv2;

        c1 = connections.get(0);
        c2 = connections.get(1);
        p1 = new Player(players.get(0));
        p2 = new Player(players.get(1));
        playerArray[0] = p1;
        playerArray[1] = p2;

        rv1 = new RemoteView(p1, players.get(1), c1);
        rv2 = new RemoteView(p2, players.get(0), c2);

        Model model = new Model(playerArray);
        Controller controller = new Controller(model);
        model.addObserver(rv1);
        model.addObserver(rv2);
        rv1.addObserver(controller);
        rv2.addObserver(controller);

        playingConnection.put(c1, c2);
        playingConnection.put(c2, c1);

        sendAllPlayer(PlayerMessage.START_PLAY);

    }

    private void threePlayer(List<String> players) {
        ClientConnection c1,c2,c3;
        Player p1,p2,p3;
        Player[] playerArray = new Player[this.numPlayers];
        RemoteView rv1,rv2,rv3;

        c1 = connections.get(0);
        c2 = connections.get(1);
        c3 = connections.get(3);
        p1 = new Player(players.get(0));
        p2 = new Player(players.get(1));
        p3 = new Player(players.get(2));
        playerArray[0] = p1;
        playerArray[1] = p2;
        playerArray[2] = p3;

        rv1 = new RemoteView(p1, players.get(1), players.get(2), c1);
        rv2 = new RemoteView(p2, players.get(0), players.get(2), c2);
        rv3 = new RemoteView(p3, players.get(1), players.get(0), c3);


        Model model = new Model(playerArray);
        Controller controller = new Controller(model);
        model.addObserver(rv1);
        model.addObserver(rv2);
        model.addObserver(rv3);

        rv1.addObserver(controller);
        rv2.addObserver(controller);
        rv3.addObserver(controller);

        playingConnection.put(c1, c2);
        playingConnection.put(c2, c3);
        playingConnection.put(c3, c1);


        sendAllPlayer(PlayerMessage.START_PLAY);
    }


}
