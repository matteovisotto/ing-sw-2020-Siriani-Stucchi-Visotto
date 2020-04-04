package it.polimi.ingsw.model;

public class Worker {
    private Cell cell;
    public boolean available = true;

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
