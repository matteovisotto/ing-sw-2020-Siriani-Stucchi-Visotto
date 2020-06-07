package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.FullWorkerException;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class define a player in the game.
 */
public class Player implements Serializable {
    private final ArrayList<Worker> workers;
    private final String playerName;
    private GodCard godCard = null;
    private boolean hasLost = false;
    private boolean usePower = false;
    private boolean victory = false;
    private int usedWorker = 0;

    /**
     * Constructor of the class
     * @param playerName a string that represent the selected name by the player for hinself
     */
    public Player(String playerName) {
        this.playerName = playerName;
        workers = new ArrayList<>();
    }

    /**
     *
     * @return true if this player has won in the game
     */
    public boolean hasWon() {
        return victory;
    }

    /**
     * @param victory for set if the player has won
     */
    public void setVictory(boolean victory) {
        this.victory = victory;
    }

    /**
     *
     * @return true of the player has used his god power in his turn
     */
    public boolean getUsePower() {
        return usePower;
    }

    /**
     * Set the usePower to default value (False)
     */
    public void resetUsePower(){
        usePower = false;
    }

    /**
     * @param usePower set the value for usePower flag
     */
    public void setUsePower(boolean usePower){
        this.usePower = usePower;
    }

    /**
     *
     * @param status set the hasLost flag
     */
    public void setHasLost(boolean status){
        hasLost = status;
    }

    /**
     *
     * @return true if the player has lost
     */
    public boolean getHasLost(){
        return hasLost;
    }

    /**
     *
     * @return a string containing the name of the Player
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     *
     * @return the GodCard instance if player has it, else return null
     */
    public GodCard getGodCard() {
        return this.godCard;
    }

    /**
     *
     * @param godCard set player GodCard
     */
    public void setGodCard(GodCard godCard){
        this.godCard = godCard;
    }

    /**
     *
     * @param number the number of the worker we want, between 0 and 1
     * @return the Worker instance corresponding ad the number
     * @throws IndexOutOfBoundsException if number is less then 0 or higher then 1
     */
    public Worker getWorker(int number) throws IndexOutOfBoundsException {
        return (workers.get(number));
    }


    /**
     *
     * @param worker a worker class instance
     * @throws FullWorkerException if the player already heave two workers
     */
    public void setWorkers(Worker worker) throws FullWorkerException{
        if(workers.size() == 2){
            throw new FullWorkerException("Went over the number of workers allowed");
        }
        this.workers.add(worker);
    }

    /**
     *
     * @return the selected worker used in the turn
     */
    public int getUsedWorker() {
        return usedWorker;
    }

    public int getUnusedWorker(){
        return  (usedWorker+1)%2;
    }

    /**
     *
     * @param usedWorker the worker the player has decided to use in his turn
     */
    public void setUsedWorker(int usedWorker) {
        this.usedWorker = usedWorker;
    }

    /**
     * Reset all the data except the name for a new play
     */
    public void reset(){
        this.hasLost = false;
        this.victory = false;
        godCard = null;
        workers.remove(1);
        workers.remove(0);
    }

    /**
     * Remove player's workers
     */
    public void remove(){
        for (Worker w : workers){
            w.getCell().freeCell();
            //w.setCell(null);
        }


    }

    /**
     *{@inheritDoc}
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((playerName == null) ? 0 : playerName.hashCode());
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Player other = (Player) obj;
        if (playerName == null) {
            return other.playerName == null;
        } else return playerName.equals(other.playerName);
    }
}

