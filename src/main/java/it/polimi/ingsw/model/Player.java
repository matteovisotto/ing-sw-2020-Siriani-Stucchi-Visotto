package it.polimi.ingsw.model;

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

    }

}
