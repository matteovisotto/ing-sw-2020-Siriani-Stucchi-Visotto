package it.polimi.ingsw.model;

import java.io.Serializable;

/**
 * This class define a cell on the board.
 */
public class Cell implements Serializable, Cloneable{
    private Blocks level;
    private boolean isFree = true;

    private final int x;
    private final int y;

    public Cell(int x, int y){
        this.level = Blocks.EMPTY;
        this.x = x;
        this.y = y;
    }

    private Cell(int x, int y, Blocks level, boolean isFree){
        this.x = x;
        this.y = y;
        this.isFree = isFree;
        this.level = level;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Cell) {
            Cell otherCell = (Cell) obj;
            return otherCell.getX() == this.x && otherCell.getY() == this.y;
        } else return false;
    }

    @Override
    public String toString() {
        int free = 0;
        if(this.isFree()) free = 1;
        return "L:" + this.getLevel().getBlockId() + " F:" + free;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        super.clone();
        return new Cell(x, y, level, isFree);
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