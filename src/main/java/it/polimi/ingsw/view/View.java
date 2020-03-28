package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Player;

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

    void handleMove(int row, int column) {
        System.out.println(row + " " + column);

    }

    public void reportError(String message){
        showMessage(message);
    }

}
