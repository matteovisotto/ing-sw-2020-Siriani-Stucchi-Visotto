package it.polimi.ingsw.model;

import java.util.Random;

public class Player {
    private final Worker[] workers;
    private final String playerName;
    private GodCard godCard = null;
    private boolean lost = false;

    public Player(String playerName) {
        this.playerName = playerName;
        workers = new Worker[2];
    }

    public String getPlayerName() {
        return playerName;
    }
    public GodCard getCard() {
        return godCard;
    }

    public Worker getWorker(int number) throws ArrayIndexOutOfBoundsException {
        return (workers[number]);
    }

    public void setWorkers(Worker worker1, Worker worker2) {
        this.workers[0] = worker1;
        this.workers[1] = worker2;
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

