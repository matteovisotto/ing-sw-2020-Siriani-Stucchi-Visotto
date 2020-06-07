package it.polimi.ingsw.model;

import java.io.Serializable;

/**
 * This class define a worker in the game.
 */
public class Worker implements Serializable {
    private Cell cell;
    public boolean available = true; //può muoversi

    /**
     * Constructor of the class
     * @param cell the cell where place the worker
     */
    public Worker(Cell cell){
        this.cell = cell;
    }

    /**
     *
     * @return the actual cell of the worker
     */
    public Cell getCell() {
        return cell;
    }

    /**
     * Used to change the worker cell when move
     * @param cell a new cell for the worker
     */
    public void setCell(Cell cell){
        this.cell = cell;
    }

    /**
     *
     * @return return false if the worker is stuck and it can't move
     */
    public boolean getStatus () {
        return available;
    }

    /**
     * Used to set that the worker can't move
     * @param status a flag that indicate if the worker can move
     */
    public void setStatus (boolean status) {
        this.available = status;
    }
}
