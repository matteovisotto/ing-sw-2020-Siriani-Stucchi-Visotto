package it.polimi.ingsw.model;

import it.polimi.ingsw.model.messageModel.PlayerMove;
import it.polimi.ingsw.model.messageModel.PlayerWorker;
import it.polimi.ingsw.model.messageModel.ViewMessage;
import it.polimi.ingsw.observer.Observable;

import java.util.EnumMap;
import java.util.Map;

public class Model extends Observable<ViewMessage> {
    private Board board = new Board();
    private final Player[] turn;
    private int id = 0;
    private final boolean simplePlay;
    private Phase phase;
    private Map<SimpleGods, Player> playerCards = new EnumMap<SimpleGods, Player>(SimpleGods.class);

    public Model(Player[] players, boolean simplePlay){
        this.turn = players;
        this.simplePlay = simplePlay;
    }

    public void resetBoard(){
        this.board = new Board();
    }

    public boolean isSimplePlay() {
        return simplePlay;
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
        notifyObservers((ViewMessage) o);
    }

    public void vittoria(Player player, int worker) {
        //notifyObservers();
    }

    public void updateTurn(){
        id = (id + 1) % (turn.length);
        if(turn[id].getStatus()){
            updateTurn();
        }

        try {
            notifyObservers(new ViewMessage(getBoardClone(), turn[id]));
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    public void setPlayerWorker (PlayerWorker playerWorker){
        playerWorker.getPlayer().setWorkers(new Worker(this.getBoard().getCell(playerWorker.getX(), playerWorker.getY())));
    }

    public void move(PlayerMove move) throws ArrayIndexOutOfBoundsException {
        Worker worker = move.getPlayer().getWorker(move.getWorkerId());
        worker.setCell(this.getBoard().getCell(move.getRow(), move.getColumn()));
        worker.getCell().freeCell();
        this.getBoard().getCell(move.getRow(), move.getColumn()).useCell();
    }
    public Player getActualPlayer() {
        return turn[id];
    }
}