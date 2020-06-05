package it.polimi.ingsw.observer;

import java.util.ArrayList;
import java.util.List;

public class Observable<T> {
    private final List<Observer<T>> observers = new ArrayList<>();

    public void addObserver(Observer<T> observer){
        synchronized (observers){
            observers.add(observer);
        }
    }

    public void removeObserver(Observer<T> observer){
        synchronized (observers) {
            observers.remove(observer);
        }
    }

    public void removeExcept(Observer<T> observer){
        synchronized (observer){
            for(Observer<T> observer1 : observers){
                if(observer1!=observer){
                    observers.remove(observer1);
                }
            }
        }
    }

    public void notifyObservers(T msg){
        synchronized (observers){
            for(Observer<T> observer : observers){
                observer.update(msg);
            }
        }
    }

}
