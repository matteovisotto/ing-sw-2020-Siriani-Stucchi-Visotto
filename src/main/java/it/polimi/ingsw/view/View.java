package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.messageModel.PlayerMove;

import java.util.Observable;
import java.util.Observer;


public abstract class View extends Observable implements Observer {
    private final Player player;

    protected View(Player player){
        this.player = player;
    }

    protected Player getPlayer(){
        return player;
    }

    protected abstract void showMessage(Object message);

    void doMove(int row, int column, int workerId) {
        System.out.println(row + " " + column);
        notifyObservers(new PlayerMove(player, workerId, row, column, this));
    }

    protected void drawCard(int cardId){
        //notify observers with a message containing the card 
    }

    public void reportError(String message){
        showMessage(message);
    }


}