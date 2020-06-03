package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.messageModel.Message;
import it.polimi.ingsw.model.messageModel.ViewMessage;
import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.observer.Observer;



public abstract class View extends Observable<Message> implements Observer<ViewMessage> {
    private final Player player;

    protected View(Player player){
        this.player = player;
    }

    protected Player getPlayer(){
        return player;
    }

    protected abstract void showMessage(Object message);

    void doAction(Message message){
        notifyObservers(message);
    }

    public void reportError(String message){
        showMessage("ERROR: " + message);
    }


}