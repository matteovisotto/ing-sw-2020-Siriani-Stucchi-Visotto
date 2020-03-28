package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.utils.ConnectionMessage;
import it.polimi.ingsw.utils.PlayerMessage;
import it.polimi.ingsw.view.RemoteView;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static final int PORT= 12345;
    private ServerSocket serverSocket;

    private ExecutorService executor = Executors.newCachedThreadPool();

    private List<ClientConnection> connections = new ArrayList<>();
    private Map<String, ClientConnection> waitingConnection = new HashMap<>();
    private Map<ClientConnection, ClientConnection> playingConnection = new HashMap<>();
    private Map<ClientConnection, Lobby> lobbyConnections = new HashMap<>();
    private List<Lobby> lobbies = new ArrayList<>();

    //Register connection
    private synchronized void registerConnection(ClientConnection c){
        connections.add(c);
    }

    //Deregister connection
    public synchronized void deregisterConnection(ClientConnection c){
        connections.remove(c);
        ClientConnection opponent = playingConnection.get(c);
        if(opponent != null){
            opponent.closeConnection();
            playingConnection.remove(c);
            playingConnection.remove(opponent);

        }
        Lobby lobby = lobbyConnections.get(c);
        lobbyConnections.get(c).closeLobby();
        //Clear other map
        lobbies.remove(lobby);
    }

    public synchronized int getLobbiesCount(){
        return this.lobbies.size();
    }

    public synchronized String getLobbiesNames() {
        String names = "";
        for(int i=0; i<lobbies.size(); i++){
            String lobbyFull = "";
            if(lobbies.get(i).isFull()) lobbyFull = " [FULL] ";
            names += i + " - " + lobbies.get(i).getLobbyName()+lobbyFull+"\n";
        }
        return names;
    }

    public synchronized void addLobby(String lobbyName, ClientConnection c, String playerName, int numPlayer){
        Lobby lobby = new Lobby(lobbyName, playerName, c, numPlayer);
        this.lobbies.add(lobby);
        this.lobbyConnections.put(c, lobby);
    }

    public synchronized void joinLobby(int lobbyId, ClientConnection c, String playerName){
        Lobby lobby = this.lobbies.get(lobbyId);
        lobby.addPlayer(playerName, c);
        lobbyConnections.put(c, lobby);
    }


    public Server() throws IOException {
        this.serverSocket = new ServerSocket(PORT);
    }

    //non dovrebbe essere @override?
    public void run(){
        System.out.println("Server listening on port: " + PORT);
        while(true){
            try {
                Socket socket = serverSocket.accept();
                SocketClientConnection connection = new SocketClientConnection(socket, this);
                registerConnection(connection);
                executor.submit(connection);
            } catch (IOException e){
                System.err.println(ConnectionMessage.ERROR);
            }
        }
    }

}
