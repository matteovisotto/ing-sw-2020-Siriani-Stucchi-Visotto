package it.polimi.ingsw.model;

public class Cell {
    private Blocks level;


    public Cell(){
        this.level=Blocks.EMPTY;
    }

    public Blocks getLevel () {
        return level;
    }

    public void increaseLevel() {

    }
}