package it.polimi.ingsw.model;

import java.io.Serializable;

/**
 * This class define a worker in the game.
 */
public class Worker implements Serializable {
    private Cell cell;
    public boolean available = true; //può muoversi

    public Worker(Cell cell){
        this.cell = cell;
    }

    public Cell getCell() {
        return cell;
    }

    public void setCell(Cell cell){
        this.cell = cell;
    }

    public boolean getStatus () {
        return available;
    }

    public void setStatus (boolean status) {
        this.available = status;
    }
}
