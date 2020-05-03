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
}

