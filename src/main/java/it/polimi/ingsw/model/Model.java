package it.polimi.ingsw.model;

import it.polimi.ingsw.model.messageModel.*;
import it.polimi.ingsw.model.simplegod.Athena;
import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.utils.PlayerMessage;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;

public class Model extends Observable<ViewMessage> {
    private Board board;
    private final Player[] turn;
    private int id = 0;
    private int leftPlayers;
    private final boolean simplePlay;
    private Phase phase = Phase.DRAWCARD;
    private Map<Gods, Player> playerCards = new EnumMap<>(Gods.class);
    private ArrayList<GodCard> gods = new ArrayList<>();
    public static int athenaId = -2;     //-2->valore inizializzato, -1-> non c'é athena in partita
    private boolean movedUp = false;
    private MessageType messageType = MessageType.DRAW_CARD;
    private String playerMessage = PlayerMessage.DRAW_CARD;

    public Model(Player[] players, boolean simplePlay){
        this.turn = players;
        this.simplePlay = simplePlay;
        this.board= new Board(this.turn);
        leftPlayers = players.length;
        if(simplePlay){
            this.phase = Phase.SETWORKER1;
            this.messageType = MessageType.SET_WORKER_1;
            this.playerMessage = PlayerMessage.PLACE_FIRST_WORKER;
        }
    }

    public void initialize(){
        notifyObservers(new GameMessage(turn[id], PlayerMessage.YOUR_TURN, MessageType.BEGINNING, Phase.BEGINNING));
        notifyObservers(new GameBoardMessage(getBoardClone(), turn[id], this.playerMessage, this.messageType, this.phase));
    }

    public boolean isMovedUp() {
        return movedUp;
    }

    public void setMovedUp(boolean movedUp) {
        this.movedUp = movedUp;
    }

    public void resetBoard(){
        this.board = new Board(turn);
    }

    public boolean isSimplePlay() {
        return simplePlay;
    }

    public boolean isPlayerTurn(Player p){
        return this.turn[id] == p;
    }

    public void setNextPhase(Phase phase) {
        this.phase = phase;
    }

    public int getActualPlayerId() {
        return id;
    }

    public Player[] getPlayers(){
        return turn;
    }

    public int getLeftPlayers(){
        return leftPlayers;
    }

    public int getNumOfPlayers(){
        return this.turn.length;
    }

    public void setNextMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    public void setNextPlayerMessage(String playerMessage) {
        this.playerMessage = playerMessage;
    }

    public Board getBoard() {
        return board;
    }

    public Board getBoardClone() {
        try{
            return board.clone();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public synchronized void updateTurn(){
        id = (id + 1) % (turn.length);
        /*if(!isSimplePlay()){
            if(turn[id].getGodCard().getCardGod()==Gods.ATHENA){
                setMovedUp(false);
            }
        }*/
        if(turn[id].hasWon() && turn.length == 3){
            updateTurn();
        }
        if(turn[id].getHasLost() && turn.length == 3){
            updateTurn();
        }
        try{
            if(!turn[id].getWorker(0).getStatus() && !turn[id].getWorker(1).getStatus()){
                loose(turn[id]);
            }
        }catch(IndexOutOfBoundsException e){
            //e.printStackTrace();
        }

        notifyObservers(new GameMessage(turn[id], PlayerMessage.YOUR_TURN, MessageType.BEGINNING, Phase.BEGINNING));
    }

    public Phase getPhase() {
        return phase;
    }

    public void updatePhase(){
        phase = Phase.next(phase);
    }

    public void setPlayerWorker (PlayerWorker playerWorker){
        Cell chosenCell = this.getBoard().getCell(playerWorker.getX(), playerWorker.getY());
        chosenCell.useCell();
        playerWorker.getPlayer().setWorkers(new Worker(chosenCell));
        notifyChanges();
    }

    public Player getActualPlayer() {
        return turn[id];
    }
    //La seguente funzione va chiamata solo una volta, in seguito alla distribuzione delle carte, per vedere dov'é athena.
    public int getAthenaPlayer(){    //da mettere subito dopo la scelta delle carte dai giocatori ed in seguito assegnare il valore alla variabile athenaId
        GodCard godCard = new Athena();
        for (int i = 0; i < turn.length; i++)
        {
            if (turn[i].getGodCard().getName().equals(godCard.getName())){
                return i;
            }
        }
        return -1;
    }

    public void addGod(GodCard godCard){
        gods.add(godCard);
    }

    public ArrayList<GodCard> getGods() {
        return gods;
    }

    public Player getPlayer(int i){
        return turn[i];
    }

    public int getLeftCards(){
        return gods.size();
    }

    public boolean isGodAvailable(int card){
        return gods.size() >= card + 1;
    }

    public Player getGCPlayer(Gods gods){//returna null se non c'é un giocatore assegnato alla carta
        return playerCards.get(gods);
    }

    public GodCard getNextPlayerGC(){
        int next=(id+1)%(turn.length);
        if(turn[next].hasWon() || turn[next].getHasLost()){
            next=(next+1)%(turn.length);
        }
        return turn[next].getGodCard();
    }

    public GodCard assignCard(Player player, int card){
        GodCard godCard = gods.get(card);
        gods.remove(card);
        gods.trimToSize();
        player.setGodCard(godCard);
        playerCards.put(godCard.getCardGod(), player);
        return godCard;
    }


    public void move(PlayerMove move) throws ArrayIndexOutOfBoundsException {
        Worker movingWorker = move.getPlayer().getWorker(move.getWorkerId());
        movingWorker.getCell().freeCell();
        movingWorker.setCell(this.getBoard().getCell(move.getRow(), move.getColumn()));
        this.getBoard().getCell(move.getRow(), move.getColumn()).useCell();
        getActualPlayer().setUsedWorker(move.getWorkerId());
        notifyChanges();
    }

    public void increaseLevel(Cell cell, Blocks level) {//build
        cell.setLevel(level);
        notifyChanges();
    }

    public void victory(Player player) {
        player.setVictory(true);
        ViewMessage win = new GameMessage(turn[id], "Player: " + player.getPlayerName() + " has won!!!!", MessageType.VICTORY, this.phase);
        notifyObservers(win);
        if(leftPlayers == 2){
            endGame();
        }
        else{
            leftPlayers--;
            player.remove();
            setNextPhase(Phase.MOVE);
            setNextPlayerMessage(PlayerMessage.MOVE);
            setNextMessageType(MessageType.MOVE);
            updateTurn();
            notifyChanges();
        }

    }

    public void loose(Player player){
        player.setHasLost(true);
        if(player.getGodCard().getCardGod()==Gods.ATHENA){
            setMovedUp(false);
        }
        ViewMessage loose = new GameMessage(turn[id], "Player: " + player.getPlayerName() + " has lost. Retry, you'll have more luck", MessageType.LOSE, this.phase);
        if(leftPlayers == 3){
            leftPlayers--;
        }
        else{
            for (Player value : turn) {
                if (!value.getHasLost() && value != player) {
                    victory(value);
                }
            }
        }
        notifyObservers(loose);
    }

    public void endGame(){
        phase = Phase.END_GAME;
        ViewMessage end = new GameMessage(turn[id], "The game has ended.\nDo you want to play again?(y/n)\n", MessageType.END_GAME, this.phase);
        notifyObservers(end);
    }

    public void close(){

    }

    public void startOver(){
        resetBoard();
        leftPlayers = turn.length;
        if(simplePlay){
            this.phase = Phase.SETWORKER1;
            this.messageType = MessageType.SET_WORKER_1;
            this.playerMessage = PlayerMessage.PLACE_FIRST_WORKER;
        }
        else{
            this.phase = Phase.DRAWCARD;
            this.messageType = MessageType.DRAW_CARD;
            this.playerMessage = PlayerMessage.DRAW_CARD;
        }
        for (Player player : turn) {
            player.reset();
        }
        initialize();
    }
    //Before call set all params -> MessageType, PlayerMessage, Phase, Turn
    public void notifyChanges(){
        notifyObservers(new GameBoardMessage(getBoardClone(), turn[id], this.playerMessage, this.messageType, this.phase));
    }

    public void notifyMessage(String message){
        notifyObservers(new GameMessage(turn[id],message, MessageType.VOID_MESSAGE, this.phase));
    }
}