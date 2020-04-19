package it.polimi.ingsw.model;

import it.polimi.ingsw.model.messageModel.MessageEveryPlayer;
import it.polimi.ingsw.model.messageModel.PlayerMove;
import it.polimi.ingsw.model.messageModel.PlayerWorker;
import it.polimi.ingsw.model.messageModel.ViewMessage;
import it.polimi.ingsw.model.simplegod.Athena;
import it.polimi.ingsw.observer.Observable;

import java.util.EnumMap;
import java.util.Map;
import java.util.Random;

public class Model extends Observable<ViewMessage> {
    private Board board = new Board();
    private final Player[] turn;
    private int id = 0;
    private final boolean simplePlay;
    private Phase phase = Phase.DRAWCARD;
    private Map<SimpleGods, Player> playerCards = new EnumMap<>(SimpleGods.class);//questo serve per athena
    public static int athenaId = -2;     //-2->valore inizializzato, -1-> non c'é athena in partita
    private static boolean movedUp = false;

    public Model(Player[] players, boolean simplePlay){
        this.turn = players;
        this.simplePlay = simplePlay;
    }

    public static boolean isMovedUp() {
        return movedUp;
    }

    public static void setMovedUp(boolean movedUp) {
        Model.movedUp = movedUp;
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

    public void updateTurn(){
        id = (id + 1) % (turn.length);
        if (athenaId != -1 && athenaId != -2 && turn[athenaId] == turn[id]) {
            setMovedUp(false);//questo serve per dire che il potere di Athena é terminato perché é reiniziato il suo turno.
        }
        if(turn[id].hasWon()){
            updateTurn();
        }
        try {
            notifyObservers(new MessageEveryPlayer(getBoardClone(), turn[id]));
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
    }

    public Phase getPhase() {
        return phase;
    }

    public void updatePhase(){
        phase = Phase.next(phase);
    }

    public void setPlayerWorker (PlayerWorker playerWorker){
        playerWorker.getPlayer().setWorkers(new Worker(this.getBoard().getCell(playerWorker.getX(), playerWorker.getY())));
    }

    public Player getActualPlayer() {
        return turn[id];
    }
    //La seguente funzione va chiamata solo una volta, in seguito alla distribuzione delle carte, per vedere dov'é athena.
    public int getAthenaPlayer(){    //da mettere subito dopo la scelta delle carte dai giocatori ed in seguito assegnare il valore alla variabile athenaId
        GodCard godCard = new Athena();
        for (int i = 0; i<turn.length; i++)
        {
            if (turn[i].getGodCard().getName().equals(godCard.getName())){
                return i;
            }
        }
        return -1;
    }

    public GodCard[] chooseCards(){
        Random random = new Random();
        GodCard[] godCards= new GodCard[turn.length];
        for(int i=0; i<turn.length; i++){
            godCards[i]=SimpleGods.getGod(random.nextInt(8) + 1);
            for(int j=i;j>0; j--){
                if(godCards[i].equals(godCards[j-1])) {
                    i--;
                }
            }
        }
        return godCards;
    }

    public void assignCard(Player player, GodCard godCard){
        player.setGodCard(godCard);
        playerCards.put(godCard.getCardGod(), player);
    }

    public void move(PlayerMove move) throws ArrayIndexOutOfBoundsException {
        Worker worker = move.getPlayer().getWorker(move.getWorkerId());
        worker.setCell(this.getBoard().getCell(move.getRow(), move.getColumn()));
        worker.getCell().freeCell();
        this.getBoard().getCell(move.getRow(), move.getColumn()).useCell();
    }

    public void increaseLevel(Cell cell, Blocks level) {//build
        cell.setLevel(level);
    }

    public void victory(Player player) {
        player.setVictory(true);
        try{
            ViewMessage win = new MessageEveryPlayer(getBoardClone(),turn[id],"Player: "+player.getPlayerName()+" has won!!!!");
            notifyObservers(win);
        }
        catch(CloneNotSupportedException e){
            e.printStackTrace();
        }
    }
}