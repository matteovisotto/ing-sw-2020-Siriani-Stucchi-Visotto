package it.polimi.ingsw.model;
import java.util.Observable;

public class Model extends Observable {
    private Board board = new Board();
    private Player turn[];
    private int id=0;

    public Model(Player[] players){
        this.turn = players;

    }

    public boolean isPlayerTurn(Player p){
        return this.turn[id]==p;
    }

    public Board getBoard() {
        return board;
    }

    public Board getBoardClone() throws CloneNotSupportedException {
        return board.clone();
    }
    public void setChanges(Object o){
        setChanged();
        notifyObservers(o);
    }

    public void hasMoved(Player player, int workerId) {
        if(player.getWorker(workerId).getCell().getLevel().getBlockId()==3){
            vittoria(player, workerId);
        } else {
            setChanged();
            notifyObservers();
        }

    }

    public void vittoria(Player player, int worker) {
        setChanged();
        notifyObservers();

    }

    public void updateTurn(){
        id=(id+1)%(turn.length);
        if(turn[id].getStatus()){
            updateTurn();
        }
    }
}

