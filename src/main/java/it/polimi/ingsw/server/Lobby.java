package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.utils.PlayerMessage;
import it.polimi.ingsw.view.RemoteView;

import java.util.*;

public class Lobby {
    private final int numPlayers;
    private final String lobbyName;
    private boolean isFull = false;
    private final List<ClientConnection> connections = new ArrayList<>();
    private final Map<String, ClientConnection> waitingConnection = new LinkedHashMap<>();
    private final boolean simplePlay;

    public Lobby(String lobbyName, String playerName, ClientConnection c, int numPlayers, boolean simplePlay){
        this.numPlayers = numPlayers;
        this.lobbyName = lobbyName;
        this.simplePlay = simplePlay;
        connections.add(c);
        waitingConnection.put(playerName, c);
        c.asyncSend(PlayerMessage.WAIT_PLAYERS);
    }

    public String getLobbyName(){
        return this.lobbyName;
    }

    public int getNumPlayers(){
        return this.numPlayers;
    }

    public boolean isFull(){
        return this.isFull;
    }

    public boolean isSimplePlay(){
        return this.simplePlay;
    }


    public void closeLobby() {
        for(int i = connections.size() - 1; i >= 0; i--){
            ClientConnection clientConnection = connections.get(i);
            clientConnection.closeConnection();
            connections.remove(i);
        }
        waitingConnection.clear();
    }

    public void addPlayer(String playerName, ClientConnection c) {
        connections.add(c);
        waitingConnection.put(playerName, c);

        if(waitingConnection.size() == this.numPlayers){    //appena si riempie la stanza
            this.isFull = true;
            List<String> players = new ArrayList<>(waitingConnection.keySet());
            waitingConnection.clear();
            if(this.numPlayers == 2){
                twoPlayer(players);
            } else {
                threePlayer(players);
            }

        } else {
            c.asyncSend(PlayerMessage.WAIT_PLAYERS);
        }
    }

    private void sendAllPlayer(Object o){
        for (ClientConnection connection : connections) {
            connection.send(o);
        }
    }

    private void sendAllPlayerAsync(Object o){
        for (ClientConnection connection : connections) {
            connection.asyncSend(o);
        }
    }

    public boolean isPlayerNameAvailable (String name) {
        return waitingConnection.get(name) == null;
    }

    private void twoPlayer(List<String> players){
        ClientConnection c1,c2;
        Player player1,player2;
        Player[] playerArray = new Player[this.numPlayers];
        RemoteView remoteView1,remoteView2;

        c1 = connections.get(0);
        c2 = connections.get(1);
        player1 = new Player(players.get(0));
        player2 = new Player(players.get(1));
        playerArray[0] = player1;
        playerArray[1] = player2;

        if(!simplePlay){
            //player has to draw a card
        }

        remoteView1 = new RemoteView(player1, players.get(1), c1);
        remoteView2 = new RemoteView(player2, players.get(0), c2);

        Model model = new Model(playerArray, simplePlay);
        Controller controller = new Controller(model);
        model.addObserver(remoteView1);
        model.addObserver(remoteView2);
        remoteView1.addObserver(controller);
        remoteView2.addObserver(controller);

        sendAllPlayer(PlayerMessage.START_PLAY);
        try {
            sendAllPlayer(model.getBoardClone());
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        if(model.isPlayerTurn(playerArray[0])){
            c1.asyncSend(PlayerMessage.YOUR_TURN);
            c1.asyncSend(PlayerMessage.PLACE_FIRST_WORKER);
            c2.asyncSend(playerArray[0].getPlayerName() + PlayerMessage.NOT_YOUR_TURN);
        }
        else{
            c2.asyncSend(PlayerMessage.YOUR_TURN);
            c2.asyncSend(PlayerMessage.PLACE_FIRST_WORKER);
            c1.asyncSend(playerArray[1].getPlayerName() + PlayerMessage.NOT_YOUR_TURN);
        }
    }

    private void threePlayer(List<String> players) {
        ClientConnection c1,c2,c3;
        Player player1,player2,player3;
        Player[] playerArray = new Player[this.numPlayers];
        RemoteView remoteView1,remoteView2,remoteView3;

        c1 = connections.get(0);
        c2 = connections.get(1);
        c3 = connections.get(2);
        player1 = new Player(players.get(0));
        player2 = new Player(players.get(1));
        player3 = new Player(players.get(2));
        playerArray[0] = player1;
        playerArray[1] = player2;
        playerArray[2] = player3;

        if(!simplePlay){
            //player have to draw a card
        }

        remoteView1 = new RemoteView(player1, players.get(1), players.get(2), c1);
        remoteView2 = new RemoteView(player2, players.get(0), players.get(2), c2);
        remoteView3 = new RemoteView(player3, players.get(1), players.get(0), c3);


        Model model = new Model(playerArray, simplePlay);
        Controller controller = new Controller(model);
        model.addObserver(remoteView1);
        model.addObserver(remoteView2);
        model.addObserver(remoteView3);

        remoteView1.addObserver(controller);
        remoteView2.addObserver(controller);
        remoteView3.addObserver(controller);

        sendAllPlayer(PlayerMessage.START_PLAY);
        try {
            sendAllPlayer(model.getBoardClone());
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        if(model.isPlayerTurn(playerArray[0])){
            c1.asyncSend(PlayerMessage.YOUR_TURN);
            c1.asyncSend(PlayerMessage.PLACE_FIRST_WORKER);
            c2.asyncSend(playerArray[0].getPlayerName() + PlayerMessage.NOT_YOUR_TURN);
            c3.asyncSend(playerArray[0].getPlayerName() + PlayerMessage.NOT_YOUR_TURN);
        }
        else if(model.isPlayerTurn(playerArray[1])){
            c2.asyncSend(PlayerMessage.YOUR_TURN);
            c2.asyncSend(PlayerMessage.PLACE_FIRST_WORKER);
            c1.asyncSend(playerArray[1].getPlayerName() + PlayerMessage.NOT_YOUR_TURN);
            c3.asyncSend(playerArray[1].getPlayerName() + PlayerMessage.NOT_YOUR_TURN);
        }
        else{
            c3.asyncSend(PlayerMessage.YOUR_TURN);
            c3.asyncSend(PlayerMessage.PLACE_FIRST_WORKER);
            c1.asyncSend(playerArray[2].getPlayerName() + PlayerMessage.NOT_YOUR_TURN);
            c2.asyncSend(playerArray[2].getPlayerName() + PlayerMessage.NOT_YOUR_TURN);
        }
    }

}
