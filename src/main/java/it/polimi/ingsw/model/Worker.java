package it.polimi.ingsw.model;

public class Worker {
    private Cell cell;

    public Worker(int x, int y) throws IllegalArgumentException{
        cell=Board.shared.getCell(x,y);
    }

    public Cell move () {
        //Chiama la board.getCell che alza una eccezione

        return cell;
    }
    private boolean checkCell(){
        return true;
    }
}
