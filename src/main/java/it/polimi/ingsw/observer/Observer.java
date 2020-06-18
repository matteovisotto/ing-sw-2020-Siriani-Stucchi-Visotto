package it.polimi.ingsw.observer;

public interface Observer<T> {
    /**
     * This method receives updates from a Observable class
     * @param msg is the message sent
     */
    void update(T msg);
}
