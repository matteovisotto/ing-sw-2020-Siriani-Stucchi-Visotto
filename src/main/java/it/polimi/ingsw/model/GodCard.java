package it.polimi.ingsw.model;

public abstract class GodCard {

    protected SimpleGods card;


    public String getName() {
        return card.toString();
    }

    public abstract void usePower();

}
