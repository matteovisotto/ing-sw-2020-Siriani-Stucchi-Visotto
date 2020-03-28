package it.polimi.ingsw.model;

import sun.text.normalizer.NormalizerBase;

import java.util.Observable;

public class Model extends Observable {
    private Player[] players;
    private Board board = new Board();

    public Model(Player[] players){
        this.players = players;
    }

    public Player getPlayer(int id) throws ArrayIndexOutOfBoundsException{
        return this.players[id];
    }

    public Board getBoard() {
        return board;
    }

    public void setChanges(Object o){
        setChanged();
        notifyObservers(o);
    }

    public void hasMoved(int player, int workerId) {
        if(getPlayer(player).getWorker(workerId).getCell().getLevel().getBlockId()==3){
            vittoria(player, workerId);
        } else {
            setChanged();
            notifyObservers();
        }

    }
    public void vittoria(int player, int worker) {
        setChanged();
        notifyObservers();

    }
}
