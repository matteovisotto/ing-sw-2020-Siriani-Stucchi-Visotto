package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.FullWorkerException;

import java.util.ArrayList;

public class Player {
    private final ArrayList<Worker> workers;
    private final String playerName;
    private GodCard godCard = null;
    private boolean canMove = true;
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

    public void setCanMove(boolean status){
        canMove = status;
    }

    public boolean getCanMove(){
        return canMove;
    }

    public String getPlayerName() {
        return playerName;
    }

    public GodCard getGodCard() {
        return this.godCard;
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

    /*public void drawCard() {
        Random random = new Random();
        try {
            this.godCard = SimpleGods.getGod(random.nextInt(8) + 1);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }*/

    public void setGodCard(GodCard godCard){
        this.godCard = godCard;
    }
}

