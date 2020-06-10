package it.polimi.ingsw.model;

import it.polimi.ingsw.model.messageModel.*;
import it.polimi.ingsw.model.simplegod.Prometheus;
import it.polimi.ingsw.observer.Observable;
import it.polimi.ingsw.utils.PlayerMessage;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

/**
 * This class merge all the data madel of the game
 */

public class Model extends Observable<ViewMessage> {
    private Board board;
    private final Player[] turn;
    private int id = 0;
    private int leftPlayers, playersWhoWon=0, playersWhoLost=0;
    private final boolean simplePlay;
    private Phase phase = Phase.DRAWCARD;
    private final Map<Gods, Player> playerCards = new EnumMap<>(Gods.class);
    private final ArrayList<GodCard> gods = new ArrayList<>();
    private boolean movedUp = false;
    private MessageType messageType = MessageType.DRAW_CARD;
    private String playerMessage = PlayerMessage.DRAW_CARD;
    private final HashMap<Player, Integer> podium=new HashMap<>();

    /**
     * Constructor of the class
     * @param players represent the players of the actual game.
     * @param simplePlay is a boolean that if it's set to true means that the game is played without godCard (SimpleMode mode).
     */
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

    /**
     * This method configure the model and the phases of the actual game. Also this method send to all players the notify of the beginning of the game.
     */
    public void initialize(){
        notifyObservers(new GameMessage(turn[id], PlayerMessage.YOUR_TURN, MessageType.BEGINNING, Phase.BEGINNING));
        notifyObservers(new GameBoardMessage(getBoardClone(), turn[id], this.playerMessage, this.messageType, this.phase));

    }

    /**
     * @return true if Athena has moved up during this turn.
     */
    public boolean isMovedUp() {
        return movedUp;
    }

    /**
     * @param movedUp is set automatically to true by Athena when she use his power (go up one level). This value remain true until begin next athena turn.
     */
    public void setMovedUp(boolean movedUp) {
        this.movedUp = movedUp;
    }

    /**
     * This function resets the board, creating a new different Board. It's used when we want to start another game.
     */
    public void resetBoard(){
        this.board = new Board(turn);
    }

    /**
     * @return true if the mode of the actual game is set to SimpleMode.
     */
    public boolean isSimplePlay() {
        return simplePlay;
    }

    /**
     * @param p is a player of the game.
     * @return true if this turn is the turn of the player p.
     */
    public boolean isPlayerTurn(Player p){
        return this.turn[id] == p;
    }

    /**
     * This method set the next phase of the game.
     * @param phase represent a phase of the game.
     */
    public void setNextPhase(Phase phase) {
        this.phase = phase;
    }

    /**
     * @return the id of the actual player (the id is used in the array turn[]).
     */
    public int getActualPlayerId() {
        return id;
    }

    /**
     * @return an array of players containing all the players of the actual game.
     */
    public Player[] getPlayers(){
        return turn;
    }

    /**
     *
     * @return the number of players who have not yet won or lost
     */
    public int getLeftPlayers(){
        return leftPlayers;
    }

    /**
     * @return the number of the players in the actual game.
     */
    public int getNumOfPlayers(){
        return this.turn.length;
    }

    /**
     * This function is used to set the next phase message type to be notified to clients
     * @param messageType the MessageType enum instance to set
     */
    public void setNextMessageType(MessageType messageType) {
        this.messageType = messageType;
    }

    /**
     *
     * @param playerMessage string representing the next message to be notified to clients
     */
    public void setNextPlayerMessage(String playerMessage) {
        this.playerMessage = playerMessage;
    }

    /**
     * @return the board of the actual game.
     */
    public Board getBoard() {
        return board;
    }

    /**
     * This method return a clone of the board.
     * @return a clone of the board.
     */
    public Board getBoardClone() {
        try{
            return board.clone();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This void function is used to update the turn with the order players join the play.
     * If a player has won or lost he is skipped
     */
    public synchronized void updateTurn(){
        id = (id + 1) % (turn.length);

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

    /**
     *
     * @return the actual phase of the game.
     */
    public Phase getPhase() {
        return phase;
    }

    /**
     * This method update the phase of the actual game.
     * @see Phase {@link Phase} In the phase is contained the next() method.
     */
    public void updatePhase(){
        phase = Phase.next(phase);
    }

    /**
     * This method is used to add a new worker for a player
     * Get the cell in board with the coordinates contained in the message
     * Set the cell as used
     * Add a new worker for the player who ask this event (contained in the message)
     * At the end notify clients of changes
     * @param playerWorker message received from the remote view
     */
    public void setPlayerWorker (PlayerWorker playerWorker){
        Cell chosenCell = this.getBoard().getCell(playerWorker.getX(), playerWorker.getY());
        chosenCell.useCell();
        playerWorker.getPlayer().setWorkers(new Worker(chosenCell));
        notifyChanges();
    }

    /**
     * @return the actual player instance of the game.
     */
    public Player getActualPlayer() {
        return turn[id];
    }

    /**
     * This method add a chosen god card to the play Array
     * @param godCard the choosen god card instance
     */
    public void addGod(GodCard godCard){
        gods.add(godCard);
    }

    /**
     *
     * @return play gods array
     */
    public ArrayList<GodCard> getGods() {
        return gods;
    }

    /**
     *
     * @param i is an int that represent the value of a player in turn[].
     * @return the corresponding player instance in turn[i].
     */
    public Player getPlayer(int i){
        return turn[i];
    }

    /**
     *
     * @return the number of not already chosen card from the drawed ones
     */
    public int getLeftCards(){
        return gods.size();
    }

    /**
     *
     * @param card an integer that represent the card index in the array
     * @return true if the selected index is available in the array
     */
    public boolean isGodAvailable(int card){
        return gods.size() >= card + 1;
    }

    /**
     * This method return the corresponding Player that own a specific GodCard (gods). This method return null if there are no player associated of that specific card.
     * @param gods is the GodCard for which i'm searching the player.
     * @return the searched player.
     */
    public Player getGCPlayer(Gods gods){
        return playerCards.get(gods);
    }

    /**
     * This method is used to get the GodCard of the next turn player
     * @return the GodCard instance of the next turn player
     */
    public GodCard getNextPlayerGC(){
        int next=(id+1)%(turn.length);
        if(turn[next].hasWon() || turn[next].getHasLost()){
            next=(next+1)%(turn.length);
        }
        return turn[next].getGodCard();
    }

    /**
     * This method is used to assign a GodCard to a player
     * @param player the player who pick a card between the drawed ones
     * @param card the drawed card array index chosen by the player
     * @return the GodCard instance of the chosen card
     */
    public GodCard assignCard(Player player, int card){
        GodCard godCard = gods.get(card);
        gods.remove(card);
        gods.trimToSize();
        player.setGodCard(godCard);
        playerCards.put(godCard.getCardGod(), player);
        return godCard;
    }


    /**
     * This method is called by the controller to update data for a move event
     * @param move the message recived by the client and already controlled by the controller
     * @throws ArrayIndexOutOfBoundsException
     */
    public void move(PlayerMove move) throws ArrayIndexOutOfBoundsException {
        Worker movingWorker = move.getPlayer().getWorker(move.getWorkerId());
        movingWorker.getCell().freeCell();
        movingWorker.setCell(this.getBoard().getCell(move.getRow(), move.getColumn()));
        this.getBoard().getCell(move.getRow(), move.getColumn()).useCell();
        getActualPlayer().setUsedWorker(move.getWorkerId());
        notifyChanges();
    }

    /**
     * This function is called by the controller to update data for a build event
     * @see Cell
     * @param cell the cell chosen for the built
     * @param level the level to be assignd at the cell
     */
    public void increaseLevel(Cell cell, Blocks level) {//build
        cell.setLevel(level);
        notifyChanges();
    }

    /**
     * This method add a player who won to the podium,
     * then if only one player is left call lose for him.
     * If more then one player are left, remove player worker from the board and set the player victory flag at true
     * At the end update model phases and message and notify the changes to the clients
     * @param player the player who won
     */
    public void victory(Player player) {
        player.setVictory(true);
        playersWhoWon++;
        ViewMessage win = new GameMessage(turn[id], "Player: " + player.getPlayerName() + " has won!!!!", MessageType.VICTORY, this.phase);
        notifyObservers(win);
        podium.put(player, playersWhoWon);
        if(leftPlayers == 2){
            for (Player value : turn) {
                if (!value.getHasLost() && !value.hasWon() && value != player) {
                    loose(value);
                    return;
                }
            }
            endGame();
        }
        else{
            leftPlayers--;
            player.remove();
            setNextPhase(Phase.MOVE);
            setNextPlayerMessage(PlayerMessage.MOVE);
            setNextMessageType(MessageType.MOVE);
            updateTurn();
            if(getActualPlayer()==getGCPlayer(Gods.PROMETHEUS) && !((Prometheus)getGCPlayer(Gods.PROMETHEUS).getGodCard()).hasBuilt()){
                setNextPhase(Phase.WAIT_GOD_ANSWER);
                setNextPlayerMessage(PlayerMessage.USE_POWER);
                setNextMessageType(MessageType.USE_POWER);
            }
            notifyChanges();
        }

    }

    /**
     * This method add a player who lost to the podium at last position,
     * then if only one player is left call victory for him.
     * If more then one player are left, remove player worker from the board and set the player lost flag at true
     * At the end update model phases and message and notify the changes to the clients
     * @param player the player who lost
     */
    public void loose(Player player){
        player.setHasLost(true);
        if(!isSimplePlay()){
            if(player.getGodCard().getCardGod()==Gods.ATHENA){
                setMovedUp(false);
            }
        }
        ViewMessage loose = new GameMessage(turn[id], "Player: " + player.getPlayerName() + " has lost. Retry, you'll have more luck", MessageType.LOSE, this.phase);
        notifyObservers(loose);
        podium.put(player, getNumOfPlayers()-playersWhoLost);
        playersWhoLost++;
        if(leftPlayers == 3){
            leftPlayers--;
            player.remove();
            setNextPhase(Phase.MOVE);
            setNextPlayerMessage(PlayerMessage.MOVE);
            setNextMessageType(MessageType.MOVE);
            updateTurn();
            if(getActualPlayer()==getGCPlayer(Gods.PROMETHEUS) && !((Prometheus)getGCPlayer(Gods.PROMETHEUS).getGodCard()).hasBuilt()){
                setNextPhase(Phase.WAIT_GOD_ANSWER);
                setNextPlayerMessage(PlayerMessage.USE_POWER);
                setNextMessageType(MessageType.USE_POWER);
            }
            notifyChanges();
        }
        else{
            for (Player value : turn) {
                if (!value.hasWon() && !value.getHasLost() && value != player) {
                    victory(value);
                    return;
                }
            }
            endGame();
        }

    }

    /**
     * Set game phase to END_GAME
     * This function is called when only one player is left
     * Notify clients that the game in ended
     */
    public void endGame(){
        phase = Phase.END_GAME;
        ViewMessage end = new EndGameMessage(turn[id], "The game has ended.\nDo you want to play again?(y/n)\n", MessageType.END_GAME, this.phase,podium);
        notifyObservers(end);
    }

    /**
     * This method reset all the model for starting a new play and call the inizialise function
     * startOver is called only if all player have chosen to play again
     *
     */
    public void startOver(){
        resetBoard();
        podium.clear();
        leftPlayers = turn.length;
        if(simplePlay){
            this.phase = Phase.SETWORKER1;
            this.messageType = MessageType.SET_WORKER_1;
            this.playerMessage = PlayerMessage.PLACE_FIRST_WORKER;
        }
        else{
            playerCards.clear();
            gods.clear();
            this.phase = Phase.DRAWCARD;
            this.messageType = MessageType.DRAW_CARD;
            this.playerMessage = PlayerMessage.DRAW_CARD;
        }
        for (Player player : turn) {
            player.reset();
        }
        this.playersWhoWon=0;
        this.playersWhoLost=0;
        this.id = 0;
        initialize();
    }


    /**
     * This function notify clients a new state of board, phase, turn and message
     */
    public void notifyChanges(){
        notifyObservers(new GameBoardMessage(getBoardClone(), turn[id], this.playerMessage, this.messageType, this.phase));
    }

    /**
     * Similar to notifyChanges, this method is used to notify clients that there is a new phase on the game
     * without changes to the board.
     * @param message string representing the message to be notified to clients
     */
    public void notifyMessage(String message){
        notifyObservers(new GameMessage(turn[id],message, MessageType.VOID_MESSAGE, this.phase));
    }
}