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
        switch(level.getBlockId()) {
            case 0:
                level = Blocks.LEVEL1;
            case 1:
                level = Blocks.LEVEL2;
            case 2:
                level = Blocks.LEVEL3;
            case 3:
                level = Blocks.DOME;
            default:
                throw new IllegalArgumentException();
        }
    }
}