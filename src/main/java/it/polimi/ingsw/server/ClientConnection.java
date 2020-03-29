package it.polimi.ingsw.server;

import java.util.Observable;

public abstract class ClientConnection extends Observable {

    public abstract void closeConnection();

    public abstract void send(Object message);

    public abstract void asyncSend(Object message);
}
