package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.PlayerMove;

import java.util.Observable;
import java.util.Observer;


public abstract class View extends Observable implements Observer {
    private Player player;

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

    public void reportError(String message){
        showMessage(message);
    }


}