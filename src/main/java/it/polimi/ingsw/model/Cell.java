package it.polimi.ingsw.model;

public class Cell {
    private Blocks level;

    private int x;
    private int y;

    public Cell(int x, int y){
        this.level=Blocks.EMPTY;
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
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