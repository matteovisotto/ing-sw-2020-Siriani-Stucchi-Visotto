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

/**
 * This class is the main server class
 */
public class Server {
    private static final int PORT = 15986;
    private final ServerSocket serverSocket;
    private final ExecutorService executor = Executors.newCachedThreadPool();
    private final Map<ClientConnection, Lobby> lobbyConnections = new HashMap<>();
    private final List<Lobby> lobbies = new ArrayList<>();
    private final Map<Lobby, ArrayList<ClientConnection>> playerInLobby = new HashMap<>();

    /**
     * Class constructor
     * Create the server socket
     * @throws IOException if socket can't be created
     */
    public Server() throws IOException {
        this.serverSocket = new ServerSocket(PORT);
    }

    /**
     * This method remove and close a client connection
     * @param connection the client socket connection instance
     */
    public synchronized void deregisterConnection(ClientConnection connection){
        Lobby lobby = this.lobbyConnections.get(connection);
        this.lobbyConnections.get(connection).closeLobby();
        ArrayList<ClientConnection> toRemove = playerInLobby.get(lobby);
        for (ClientConnection clientConnection : toRemove) {
            lobbyConnections.remove(clientConnection);
        }
        this.playerInLobby.remove(lobby);
        this.lobbies.remove(lobby);
    }

    /**
     * @return the number of lobbies active in the server
     */
    public synchronized int getLobbiesCount(){
        return this.lobbies.size();
    }

    /**
     *
     * @return a string containing the lobby id, the name, the num of players in it and the play mode
     * @throws NoLobbyException if no lobbies are available
     */
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

    /**
     * This function is used to create a new lobby in the server
     * Also add the player to the playerInLobby maps and lobbyConnections maps
     * @param lobbyName the chosen lobby name
     * @param clientConnection the client socket connection instance
     * @param playerName the name of the player who create the lobby
     * @param numPlayer the chosen number of player for the lobby
     * @param simplePlay a flag that if true indicates that the play is without god cards
     */
    public synchronized void addLobby(String lobbyName, ClientConnection clientConnection, String playerName, int numPlayer, boolean simplePlay){
        Lobby lobby = new Lobby(lobbyName, playerName, clientConnection, numPlayer, simplePlay);
        this.lobbies.add(lobby);
        this.lobbyConnections.put(clientConnection, lobby);
        ArrayList<ClientConnection> arr = new ArrayList<>();
        arr.add(clientConnection);
        this.playerInLobby.put(lobby, arr);
    }

    /**
     * This function in used to add a new player to a lobby that exists
     * Also add the player to the playerInLobby maps and lobbyConnections maps
     * @param lobbyId the id of the lobby
     * @param clientConnection the client socket connection instance
     * @param playerName the name of the player
     * @throws FullLobbyException if the selected lobby is already full
     * @throws InvalidLobbyException if the selected lobby doesn't exist
     */
    public synchronized void joinLobby(int lobbyId, ClientConnection clientConnection, String playerName) throws FullLobbyException, InvalidLobbyException {
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
            lobby.addPlayer(playerName, clientConnection);
            this.lobbyConnections.put(clientConnection, lobby);
            this.playerInLobby.get(lobby).add(clientConnection);
        } else {
            throw new FullLobbyException(LobbyExceptionMessage.FULL_LOBBY);
        }
    }

    /**
     * This function is used to add player to a new lobby after he decided to play an other player without old players
     * If it is a three players game, it adds also the second opponents who chose to play again
     * @param endGameServerMessage the particular message that contins all info for the new play
     * @see EndGameServerMessage
     */
    public synchronized void addLobbyEndGame(EndGameServerMessage endGameServerMessage){
        deleteOldLobby(endGameServerMessage);
        Lobby lobbyNewGame = new Lobby(endGameServerMessage.getLobbyName(), endGameServerMessage.getPlayerNames().get(0), endGameServerMessage.getClientConnections().get(0), endGameServerMessage.getNumPlayer(), endGameServerMessage.isSimplePlay());
        this.lobbies.add(lobbyNewGame);
        this.lobbyConnections.put(endGameServerMessage.getClientConnections().get(0), lobbyNewGame);
        ArrayList<ClientConnection> arr = new ArrayList<>();
        arr.add(endGameServerMessage.getClientConnections().get(0));
        this.playerInLobby.put(lobbyNewGame, arr);
        if (endGameServerMessage.getPlayerNames().size() > 1){
            joinLobby(lobbies.size(),endGameServerMessage.getClientConnections().get(1),endGameServerMessage.getPlayerNames().get(1));
        }
    }

    /**
     * When a new play is performed this method delete the old lobby from the sewrver
     * @param endGameServerMessage endGameServerMessage the particular message that contins all info for the new play
     * @see EndGameServerMessage
     */
    private synchronized void deleteOldLobby(EndGameServerMessage endGameServerMessage) {
        try{
            for (Map.Entry<ClientConnection,Lobby> clientConnections: lobbyConnections.entrySet()) {
                if (clientConnections.getValue() == endGameServerMessage.getLobby())
                    lobbyConnections.remove(clientConnections.getKey());
            }
       }catch (Exception e){
            //e.printStackTrace();
        }


        lobbies.remove(endGameServerMessage.getLobby());
        playerInLobby.remove(endGameServerMessage.getLobby());
    }

    /**
     * {@inheritDoc}
     * Open the socket and submit new accepted connection to the executor
     */
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
