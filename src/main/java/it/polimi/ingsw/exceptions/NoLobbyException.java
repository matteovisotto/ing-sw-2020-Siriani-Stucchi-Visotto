package it.polimi.ingsw.exceptions;

public class NoLobbyException extends LobbyException {

    public NoLobbyException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
