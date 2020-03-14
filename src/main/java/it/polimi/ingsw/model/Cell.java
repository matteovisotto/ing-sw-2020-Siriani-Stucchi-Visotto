package it.polimi.ingsw.model;

public class Cell {
    private Blocks level;

    public Cell(){
        this.level=Blocks.EMPTY;
    }

    public Blocks getLevel () {
        return level;
    }

    public void increaseLevel() throws IllegalArgumentException {
        switch(level.toString()) {
            case "EMPTY":
                level = Blocks.LEVEL1;
            case "LEVEL1":
                level = Blocks.LEVEL2;
            case "LEVEL2":
                level = Blocks.LEVEL3;
            case "LEVEL3":
                level = Blocks.DOME;
            default:
                throw new IllegalArgumentException();
        }
    }
}