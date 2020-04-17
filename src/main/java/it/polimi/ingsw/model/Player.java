package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.FullWorkerException;

import java.util.ArrayList;

public class Player {
    private final ArrayList<Worker> workers;
    private final String playerName;
    private GodCard godCard = null;
    private boolean status = false;
    private boolean canMove = true;

    public Player(String playerName) {
        this.playerName = playerName;
        workers = new ArrayList<>();
    }

    public void setCanMove(boolean b){
        canMove=b;
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

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean getStatus(){
        return status;
    }
}

