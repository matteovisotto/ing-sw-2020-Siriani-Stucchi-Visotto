package it.polimi.ingsw.exceptions;

public class InvalidLobbyException extends LobbyException {

    public InvalidLobbyException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }

    public InvalidLobbyException(String errorMessage) {
        super(errorMessage);
    }
}
