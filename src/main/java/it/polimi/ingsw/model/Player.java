package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.FullWorkerException;

import java.util.ArrayList;
import java.util.Random;

public class Player {
    private final ArrayList<Worker> workers;
    private final String playerName;
    private GodCard godCard = null;
    private boolean lost = false;

    public Player(String playerName) {
        this.playerName = playerName;
        workers = new ArrayList<>();
    }

    public String getPlayerName() {
        return playerName;
    }
    public GodCard getCard() {
        return godCard;
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

    public void drawCard() {
        Random random = new Random();
        try {
            this.godCard = SimpleGods.getGod(random.nextInt(8) + 1);
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }

    public void setStatus(boolean status) {
        this.lost = status;
    }

    public boolean getStatus(){
        return lost;
    }
}

