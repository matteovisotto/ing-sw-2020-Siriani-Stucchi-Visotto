package it.polimi.ingsw.exceptions;

public class FullLobbyException extends LobbyException {
    public FullLobbyException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
