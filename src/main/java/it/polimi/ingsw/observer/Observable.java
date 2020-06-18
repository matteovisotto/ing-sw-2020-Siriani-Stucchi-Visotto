package it.polimi.ingsw.observer;

import java.util.ArrayList;
import java.util.List;

public class Observable<T> {
    private final List<Observer<T>> observers = new ArrayList<>();

    /**
     * This method adds a new Observer
     * @param observer is a class that implements Observer
     */
    public void addObserver(Observer<T> observer){
        synchronized (observers){
            observers.add(observer);
        }
    }

    /**
     * This method deletes an observer
     * @param observer is the observer that needs to be removed
     */
    public void removeObserver(Observer<T> observer){
        synchronized (observers) {
            observers.remove(observer);
        }
    }

    /**
     * This method removes every observer except the one given
     * @param observer is the observer that doesn't have to be
     */
    public void removeExcept(Observer<T> observer){
        synchronized (observers){
            for(Observer<T> observer1 : observers){
                if(observer1!=observer){
                    observers.remove(observer1);
                }
            }
        }
    }

    /**
     * This method notifies every observer
     * @param msg is the message that needs to be notified to every observer
     */
    public void notifyObservers(T msg){
        synchronized (observers){
            for(Observer<T> observer : observers){
                observer.update(msg);
            }
        }
    }

}
