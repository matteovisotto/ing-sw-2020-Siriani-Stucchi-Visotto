package it.polimi.ingsw.model;

public class Worker {
    private Cell cell;

    public Worker(Cell cell) throws IllegalArgumentException{
        this.cell = cell;
    }

    public Cell getCell() {
        return cell;
    }

    public void setCell(Cell cell){
        this.cell = cell;
    }

    public boolean checkCell(){
        return true;
    }
}
