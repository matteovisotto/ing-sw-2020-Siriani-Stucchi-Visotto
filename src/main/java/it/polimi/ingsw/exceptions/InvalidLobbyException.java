package it.polimi.ingsw.exceptions;

/**
 * Exception used to notify that doesn't exist the selected lobby
 */
public class InvalidLobbyException extends LobbyException {

    public InvalidLobbyException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }

    public InvalidLobbyException(String errorMessage) {
        super(errorMessage);
    }
}
