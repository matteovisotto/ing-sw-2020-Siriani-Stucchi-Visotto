package it.polimi.ingsw.server;

import it.polimi.ingsw.observer.Observable;

/**
 * Abract class to manage socket connections from clients
 */
public abstract class ClientConnection extends Observable<String> {

    /**
     * This method close the connection represented by the instnce of the class
     */
    public abstract void closeConnection();

    /**
     * This method is used to send a message to a client
     * @param message Object representing the message to be sent to client
     */
    public abstract void send(Object message);

    /**
     * This method is used to send a message to a client in an other thread
     * @param message Object representing the message to be sent to client
     */
    public abstract void asyncSend(Object message);
}
