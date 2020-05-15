package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.messageModel.Message;
import it.polimi.ingsw.model.messageModel.PlayerMove;
import it.polimi.ingsw.model.messageModel.PlayerWorker;
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

    void doMove(int row, int column, int workerId) {
        System.out.println(row + " " + column);
        notifyObservers(new PlayerMove(player, workerId, row, column, this));
    }

    protected void placeWorker(int x, int y){
        notifyObservers(new PlayerWorker(player,x,y,this));
    }

    protected void drawCard(int cardId){
        //notify observers with a message containing the card
    }

    public void reportError(String message){
        showMessage(message);
    }


}