package it.polimi.ingsw.model.messageModel;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.server.ClientConnection;
import it.polimi.ingsw.view.View;

public class NewGameMessage extends Message {
    private char c;
    private ClientConnection clientConnection;
    public NewGameMessage(Player player, View view, char c, ClientConnection clientConnection) {
        super(player, view);
        this.c=c;
        this.clientConnection = clientConnection;
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
