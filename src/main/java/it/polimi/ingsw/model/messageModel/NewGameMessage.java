package it.polimi.ingsw.model.messageModel;


import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.server.*;
import it.polimi.ingsw.view.View;

/**
 * This class represent the message containing the choice for starting a new play
 */
public class NewGameMessage extends Message {
    private final char choice;
    private final ClientConnection clientConnection;
    private final Lobby lobby;

    /**
     * {@inheritDoc}
     * @param choice the selected
     * @param clientConnection the socket connection of the player who make this choice
     * @param lobby the old lobby instance of the previews play
     */
    public NewGameMessage(Player player, View view, char choice, ClientConnection clientConnection, Lobby lobby) {
        super(player, view);
        this.choice = choice;
        this.clientConnection = clientConnection;
        this.lobby = lobby;
    }

    /**
     *
     * @return the lobby instance
     */
    public Lobby getLobby() {
        return lobby;
    }

    /**
     *
     * @return a char representing the player choice
     */
    public char getChoice(){
        return this.choice;
    }

    /**
     *
     * @return player client socket connection
     */
    public ClientConnection getClientConnection() {
        return clientConnection;
    }

    /**
     * Perform the controller end game actions calling controller function
     * @param controller thr game controller instance
     */
    @Override
    public void handler(Controller controller) {
        controller.endGame(this);
    }
}
