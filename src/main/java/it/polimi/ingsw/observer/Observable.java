package it.polimi.ingsw.observer;

import java.util.ArrayList;
import java.util.List;

public class Observable<T> {
    private final List<Observer<T>> observers = new ArrayList<>();

    /**
     * This method add a new Observer
     * @param observer a class which implement Observer
     */
    public void addObserver(Observer<T> observer){
        synchronized (observers){
            observers.add(observer);
        }
    }

    /**
     * This method delete an observer
     * @param observer the observer to remove
     */
    public void removeObserver(Observer<T> observer){
        synchronized (observers) {
            observers.remove(observer);
        }
    }

    /**
     * This method remove all the observes except the one which in given throw the param
     * @param observer the observe not to remove
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
     * This method notify all observers
     * @param msg the message to notify all observers
     */
    public void notifyObservers(T msg){
        synchronized (observers){
            for(Observer<T> observer : observers){
                observer.update(msg);
            }
        }
    }

}
