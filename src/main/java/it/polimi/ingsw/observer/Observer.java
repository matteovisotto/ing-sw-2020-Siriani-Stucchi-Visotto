package it.polimi.ingsw.observer;

public interface Observer<T> {
    /**
     * This method when implemented in a class recive notify updates from a Observable class
     * @param msg the message which has been sent
     */
    void update(T msg);
}
