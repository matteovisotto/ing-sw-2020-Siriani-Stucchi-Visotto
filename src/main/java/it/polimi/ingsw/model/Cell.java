package it.polimi.ingsw.model;

public class Cell {
    private Blocks level;
    private boolean isFree = true;

    private int x;
    private int y;

    public Cell(int x, int y){
        this.level = Blocks.EMPTY;
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Cell) {
            Cell otherCell = (Cell) obj;
            if(otherCell.getX() == this.x && otherCell.getY() == this.y) return true;
            else return false;
        } else return false;
    }

    @Override
    public String toString() {
        int free = 0;
        if(this.isFree()) free = 1;
        return this.getLevel().getBlockId() + " " + free;
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

    public void setLevel (Blocks level) {
        this.level = level;
    }

    public boolean isFree () {
        return isFree;
    }

    public void freeCell () {
        this.isFree = true;
    }

    public void useCell () {
        this.isFree = false;
    }

}