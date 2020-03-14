package it.polimi.ingsw.model;

import java.util.Random;

public class Player {
    private Worker[] workers;
    private String playerName;
    private GodCard godCard;

    public String getName(){
        return playerName;
    }
    public GodCard getCard(){
        return godCard;
    }

    public Worker getWorker(int i){
        return (workers[i]);
    }

    public void drawCard(){
        Random random = new Random();
        try {
            this.godCard = new SimpleGod(SimpleGods.getGod(random.nextInt(8) + 1));
        } catch (IllegalArgumentException e) {

        }

    }

}
