package it.polimi.ingsw.server;

import it.polimi.ingsw.exceptions.FullLobbyException;
import it.polimi.ingsw.exceptions.InvalidLobbyException;
import it.polimi.ingsw.utils.LobbyExceptionMessage;
import it.polimi.ingsw.exceptions.NoLobbyException;
import it.polimi.ingsw.utils.ConnectionMessage;

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
    private Map<ClientConnection, Lobby> lobbyConnections = new HashMap<>();
    private List<Lobby> lobbies = new ArrayList<>();
    private Map<Lobby, ArrayList<ClientConnection>> playerInLobby = new HashMap<>();
    //Register connection
    private synchronized void registerConnection(ClientConnection c){
        connections.add(c);
    }

    //Deregister connection
    public synchronized void deregisterConnection(ClientConnection c){
        connections.remove(c);
        Lobby lobby = lobbyConnections.get(c);
        lobbyConnections.get(c).closeLobby();
        ArrayList<ClientConnection> toRemove = playerInLobby.get(lobby);
        for(int i=0; i<toRemove.size(); i++){
            lobbyConnections.remove(toRemove.get(i));
        }
        playerInLobby.remove(lobby);
        lobbies.remove(lobby);
    }

    public synchronized int getLobbiesCount(){
        return this.lobbies.size();
    }

    public synchronized String getLobbiesNames() throws NoLobbyException {
        if(lobbies.size()!=0) {
            String names = "0 - Go Back\n";
            for (int i = 0; i < lobbies.size(); i++) {
                Lobby lobby = lobbies.get(i);
                String lobbyPlayers = "";
                int playerInLobby = this.playerInLobby.get(lobby).size();
                if (lobby.isFull()) lobbyPlayers = " [FULL] ";
                else lobbyPlayers = " ["+playerInLobby+"/"+lobby.getNumPlayers()+"] ";
                names += i+1 + " - " + lobby.getLobbyName() + lobbyPlayers + "\n";
            }
            return names;
        } else {
            throw new NoLobbyException(LobbyExceptionMessage.NO_LOBBY);
        }
    }

    public synchronized void addLobby(String lobbyName, ClientConnection c, String playerName, int numPlayer, boolean simplePlay){
        Lobby lobby = new Lobby(lobbyName, playerName, c, numPlayer, simplePlay);
        this.lobbies.add(lobby);
        this.lobbyConnections.put(c, lobby);
        ArrayList<ClientConnection> arr = new ArrayList<>();
        arr.add(c);
        playerInLobby.put(lobby, arr);
    }

    public synchronized void joinLobby(int lobbyId, ClientConnection c, String playerName) throws FullLobbyException, InvalidLobbyException {
        Lobby lobby;
        try {
            lobby = this.lobbies.get(lobbyId-1);
        } catch (Exception e){
            throw new InvalidLobbyException(LobbyExceptionMessage.INVALID_LOBBY);
        }

        if (!lobby.isFull()) {
            lobby.addPlayer(playerName, c);
            lobbyConnections.put(c, lobby);
            playerInLobby.get(lobby).add(c);
        } else {
            throw new FullLobbyException(LobbyExceptionMessage.FULL_LOBBY);
        }

    }


    public Server() throws IOException {
        this.serverSocket = new ServerSocket(PORT);
    }

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
