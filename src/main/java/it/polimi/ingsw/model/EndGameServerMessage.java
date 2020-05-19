package it.polimi.ingsw.model;

import it.polimi.ingsw.server.ClientConnection;
import it.polimi.ingsw.server.Lobby;

import java.util.ArrayList;

public class EndGameServerMessage {
    private final Lobby lobby;
    private final ArrayList<ClientConnection> clientConnections;
    private final ArrayList<String> playerNames;
    private final int numPlayer;
    private final boolean simplePlay;

    public EndGameServerMessage(Lobby lobby, ArrayList<ClientConnection> clientConnections, ArrayList<String> playerNames, int numPlayer, boolean simplePlay){
     this.clientConnections = clientConnections;
     this.lobby = lobby;
     this.playerNames = playerNames;
     this.numPlayer = numPlayer;
     this.simplePlay = simplePlay;

    }

    public String getLobbyName() {
        return lobby.getLobbyName();
    }

    public Lobby getLobby(){
        return lobby;
    }

    public ArrayList<ClientConnection> getClientConnections() {
        return clientConnections;
    }

    public ArrayList<String> getPlayerNames() {
        return playerNames;
    }

    public int getNumPlayer() {
        return numPlayer;
    }

    public boolean isSimplePlay() {
        return simplePlay;
    }
}
