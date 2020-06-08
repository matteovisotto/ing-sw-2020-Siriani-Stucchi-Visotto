package it.polimi.ingsw.model;

import java.io.Serializable;

/**
 * This class define a cell on the board.
 */
public class Cell implements Serializable, Cloneable{
    private Blocks level;
    private boolean isFree = true;
    private boolean isFull=false;

    private final int x;
    private final int y;

    /**
     * Constructor of the class
     * @param x x value of the new cell
     * @param y y value of the new cell
     */
    public Cell(int x, int y){
        this.level = Blocks.EMPTY;
        this.x = x;
        this.y = y;
    }

    /**
     * Private constructor to create a new cell in clone method
     * @param x x value of cloneable cell
     * @param y y value of cloneable cell
     * @param level level of cloneable cell
     * @param isFree actual usage of cloneable cell
     */
    private Cell(int x, int y, Blocks level, boolean isFree, boolean isFull){
        this.x = x;
        this.y = y;
        this.isFree = isFree;
        this.level = level;
        this.isFull=isFull;
    }

    public boolean isFull(){
        return this.isFull;
    }

    /**
     *{@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Cell) {
            Cell otherCell = (Cell) obj;
            return otherCell.getX() == this.x && otherCell.getY() == this.y;
        } else return false;
    }

    /**
     * @return a string that represent the level of the cell and a flag if it is free
     */
    @Override
    public String toString() {
        int free = 0;
        if(this.isFree()) free = 1;
        return "L:" + this.getLevel().getBlockId() + " F:" + free;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Object clone() throws CloneNotSupportedException {
        super.clone();
        return new Cell(x, y, level, isFree, isFull);
    }

    /**
     * @return the x value of the cell
     */
    public int getX() {
        return x;
    }

    /**
     * @return the y value of the cell
     */
    public int getY() {
        return y;
    }

    /**
     * @return the level of the cell as a Blocks instance
     */
    public Blocks getLevel () {
        return level;
    }

    /**
     * @param level the Block enum instance to assign at the cell
     */
    public void setLevel (Blocks level) {
        if(level==Blocks.DOME){
            this.isFull=true;
        }
        this.level = level;

    }

    /**
     * @return true if the cell is not used
     */
    public boolean isFree () {
        return isFree;
    }

    /**
     * Set the cell ad free
     */
    public void freeCell () {
        this.isFree = true;
    }

    /**
     * Set the cell as used
     */
    public void useCell () {
        this.isFree = false;
    }

}