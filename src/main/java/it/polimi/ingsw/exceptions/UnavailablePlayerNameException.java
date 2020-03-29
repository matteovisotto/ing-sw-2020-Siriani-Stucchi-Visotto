package it.polimi.ingsw.exceptions;

public class UnavailablePlayerNameException extends LobbyException {
    public UnavailablePlayerNameException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }

    public UnavailablePlayerNameException(String errorMessage) {
        super(errorMessage);
    }
}
