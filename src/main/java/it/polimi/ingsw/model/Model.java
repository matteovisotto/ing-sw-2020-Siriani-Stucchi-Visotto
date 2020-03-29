package it.polimi.ingsw.model;
import it.polimi.ingsw.utils.PlayerMessage;

import java.util.Observable;

public class Model extends Observable {
    private Board board = new Board();
    private Player turn[];
    private int id=0;
    private boolean simplePlay;

    public Model(Player[] players, boolean simplePlay){
        this.turn = players;
        this.simplePlay = simplePlay;
    }

    public boolean isPlayerTurn(Player p){
        return this.turn[id] == p;
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

    public void vittoria(Player player, int worker) {
        setChanged();
        notifyObservers();
    }

    public void updateTurn(){
        id = (id + 1) % (turn.length);
        if(turn[id].getStatus()){
            updateTurn();
        }

    }

    public void setPlayerWorker (PlayerWorker playerWorker){
        playerWorker.getPlayer().setWorkers(new Worker(this.getBoard().getCell(playerWorker.getX1(), playerWorker.getY1())), new Worker(this.getBoard().getCell(playerWorker.getX2(), playerWorker.getY2())));
    }

    public void move(PlayerMove move) throws ArrayIndexOutOfBoundsException {
        Worker worker = move.getPlayer().getWorker(move.getWorkerId());
        worker.setCell(this.getBoard().getCell(move.getRow(), move.getColumn()));
        worker.getCell().freeCell();
        this.getBoard().getCell(move.getRow(), move.getColumn()).useCell();
    }
}