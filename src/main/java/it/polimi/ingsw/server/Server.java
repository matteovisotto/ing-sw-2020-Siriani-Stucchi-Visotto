package it.polimi.ingsw.server;

import it.polimi.ingsw.exceptions.FullLobbyException;
import it.polimi.ingsw.exceptions.InvalidLobbyException;
import it.polimi.ingsw.exceptions.UnavailablePlayerNameException;
import it.polimi.ingsw.model.EndGameServerMessage;
import it.polimi.ingsw.utils.LobbyExceptionMessage;
import it.polimi.ingsw.exceptions.NoLobbyException;
import it.polimi.ingsw.utils.ConnectionMessage;
import it.polimi.ingsw.utils.PlayerMessage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static final int PORT= 12345;
    private ServerSocket serverSocket;
    private final ExecutorService executor = Executors.newCachedThreadPool();
    private final Map<ClientConnection, Lobby> lobbyConnections = new HashMap<>();
    private final List<Lobby> lobbies = new ArrayList<>();
    private final Map<Lobby, ArrayList<ClientConnection>> playerInLobby = new HashMap<>();

    public Server() throws IOException {
        this.serverSocket = new ServerSocket(PORT);
    }

    //Deregister connection
    public synchronized void deregisterConnection(ClientConnection c){
        Lobby lobby = this.lobbyConnections.get(c);
        this.lobbyConnections.get(c).closeLobby();
        ArrayList<ClientConnection> toRemove = playerInLobby.get(lobby);
        for (ClientConnection clientConnection : toRemove) {
            lobbyConnections.remove(clientConnection);
        }
        this.playerInLobby.remove(lobby);
        this.lobbies.remove(lobby);
    }

    public synchronized int getLobbiesCount(){
        return this.lobbies.size();
    }

    public synchronized String getLobbiesNames() throws NoLobbyException {
        if(this.lobbies.size() != 0) {
            StringBuilder names = new StringBuilder(PlayerMessage.JOIN_LOBBY + "\n0 - Go Back\n");
            for (int i = 0; i < this.lobbies.size(); i++) {
                Lobby lobby = this.lobbies.get(i);
                String lobbyPlayers;
                int playerInLobby = this.playerInLobby.get(lobby).size();
                if
                    (lobby.isFull()) lobbyPlayers = " [FULL] ";
                else {
                    lobbyPlayers = "\t[" + playerInLobby + "/" + lobby.getNumPlayers() + "]";
                    if(lobby.isSimplePlay()){
                        lobbyPlayers+="\tS";
                    }
                    else
                        lobbyPlayers+="\tH";
                }


                names.append(i + 1).append(" - ").append(lobby.getLobbyName()).append(lobbyPlayers).append("\n");
            }
            return names.toString();
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
        this.playerInLobby.put(lobby, arr);
    }

    public synchronized void joinLobby(int lobbyId, ClientConnection c, String playerName) throws FullLobbyException, InvalidLobbyException {
        Lobby lobby;
        try {
            lobby = this.lobbies.get(lobbyId - 1);
        } catch (Exception e){
            throw new InvalidLobbyException(LobbyExceptionMessage.INVALID_LOBBY);
        }

        if (!lobby.isFull()) {
            if(!lobby.isPlayerNameAvailable(playerName)){
                throw new UnavailablePlayerNameException(LobbyExceptionMessage.UNAVAILABLE_NAME);
            }
            lobby.addPlayer(playerName, c);
            this.lobbyConnections.put(c, lobby);
            this.playerInLobby.get(lobby).add(c);
        } else {
            throw new FullLobbyException(LobbyExceptionMessage.FULL_LOBBY);
        }
    }

    public synchronized void addLobbyEndGame(EndGameServerMessage endGameServerMessage){
        Lobby lobby = new Lobby(endGameServerMessage.getLobbyName(), endGameServerMessage.getPlayerNames().get(0), endGameServerMessage.getClientConnections().get(0), endGameServerMessage.getNumPlayer(), endGameServerMessage.isSimplePlay());
        this.lobbies.add(lobby);
        this.lobbyConnections.put(endGameServerMessage.getClientConnections().get(0), lobby);
        ArrayList<ClientConnection> arr = new ArrayList<>();
        arr.add(endGameServerMessage.getClientConnections().get(0));
        this.playerInLobby.put(lobby, arr);
        if (endGameServerMessage.getPlayerNames().size()>1){
            joinLobby(lobbies.size()-1,endGameServerMessage.getClientConnections().get(1),endGameServerMessage.getPlayerNames().get(1));
        }
    }

    public void run(){
        System.out.println("Server listening on port: " + PORT);
        do{
            try {
                Socket socket = serverSocket.accept();
                SocketClientConnection connection = new SocketClientConnection(socket, this);
                executor.submit(connection);
            } catch (IOException e){
                System.err.println(ConnectionMessage.ERROR);
            }
        }while(true);
    }

}
