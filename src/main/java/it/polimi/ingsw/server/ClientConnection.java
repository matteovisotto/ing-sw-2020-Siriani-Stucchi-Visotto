package it.polimi.ingsw.server;

import it.polimi.ingsw.observer.Observable;

public abstract class ClientConnection extends Observable<String> {

    public abstract void closeConnection();

    public abstract void send(Object message);

    public abstract void asyncSend(Object message);
}
