package it.polimi.ingsw.model;

import it.polimi.ingsw.server.ClientConnection;

import java.util.ArrayList;

public class EndGameServerMessage {
    private final String lobbyName;
    private final ArrayList<ClientConnection> clientConnections;
    private final ArrayList<String> playerNames;
    private final int numPlayer;
    private final boolean simplePlay;

    public EndGameServerMessage(String lobbyName, ArrayList<ClientConnection> clientConnections, ArrayList<String> playerNames, int numPlayer, boolean simplePlay){
     this.clientConnections = clientConnections;
     this.lobbyName = lobbyName;
     this.playerNames = playerNames;
     this.numPlayer = numPlayer;
     this.simplePlay = simplePlay;

    }

    public String getLobbyName() {
        return lobbyName;
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
