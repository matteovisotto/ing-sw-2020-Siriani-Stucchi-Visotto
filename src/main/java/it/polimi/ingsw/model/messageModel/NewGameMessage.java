package it.polimi.ingsw.model.messageModel;


import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.server.ClientConnection;
import it.polimi.ingsw.server.*;
import it.polimi.ingsw.view.View;

public class NewGameMessage extends Message {
    private char c;
    private ClientConnection clientConnection;
    private Lobby lobby;

    public NewGameMessage(Player player, View view, char c, ClientConnection clientConnection, Lobby lobby) {
        super(player, view);
        this.c=c;
        this.clientConnection = clientConnection;
        this.lobby = lobby;
    }

    public Lobby getLobby() {
        return lobby;
    }

    public char getChoice(){
        return this.c;
    }


    public ClientConnection getClientConnection() {
        return clientConnection;
    }

    @Override
    public void handler(Controller controller) {
        controller.endGame(this);
    }
}
