package it.polimi.ingsw.exceptions;

/**
 * Exception used to notify that there aren't lobbies in the server
 */
public class NoLobbyException extends LobbyException {

    public NoLobbyException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }

    public NoLobbyException(String errorMessage){
        super(errorMessage);
    }
}
