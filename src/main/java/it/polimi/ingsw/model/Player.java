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
    public Player(String playerName) {
        this.playerName = playerName;
        workers = new ArrayList<>();
    }

    public boolean hasWon() {
        return victory;
    }

    public void setVictory(boolean victory) {
        this.victory = victory;
    }

    public boolean getUsePower() {
        return usePower;
    }

    public void resetUsePower(){
        usePower = false;
    }

    public void setUsePower(boolean usePower){
        this.usePower = usePower;
    }

    public void setHasLost(boolean status){
        hasLost = status;
    }

    public boolean getHasLost(){
        return hasLost;
    }

    public String getPlayerName() {
        return playerName;
    }

    public GodCard getGodCard() {
        return this.godCard;
    }

    public void setGodCard(GodCard godCard){
        this.godCard = godCard;
    }

    public Worker getWorker(int number) throws IndexOutOfBoundsException {
        return (workers.get(number));
    }

    public void setWorkers(Worker worker) throws FullWorkerException{
        if(workers.size() == 2){
            throw new FullWorkerException("Went over the number of workers allowed");
        }
        this.workers.add(worker);
    }

    public int getUsedWorker() {
        return usedWorker;
    }

    public void setUsedWorker(int usedWorker) {
        this.usedWorker = usedWorker;
    }

    public void reset(){
        this.hasLost = false;
        this.victory = false;
        godCard = null;
        workers.remove(1);
        workers.remove(0);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((playerName == null) ? 0 : playerName.hashCode());
        return result;
    }

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
            if (other.playerName != null) {
                return false;
            }
        } else return playerName.equals(other.playerName);

        return true;
    }
}

