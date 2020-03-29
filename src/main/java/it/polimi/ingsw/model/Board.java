package it.polimi.ingsw.model;

import java.io.Serializable;

public class Board implements Serializable, Cloneable {
    private Cell[][] board;


    public Board(){
        this.reset();
    }

    public Cell getCell(int x, int y) throws IllegalArgumentException{
        if((x<0 || x>=5) || (y<0 || y>=5)){
            throw new IllegalArgumentException();
        }
        return board[x][y];
    }

    public void reset(){
        board=new Cell[5][5];
        for(int i=0; i<5; i++){
            for(int j=0; j<5; j++){
                board[i][j]=new Cell(i,j);
            }
        }
    }


    public boolean checkCell (int x, int y, Worker worker) throws IllegalArgumentException{
        Cell cell = getCell(x,y);
        Cell other_cell = worker.getCell();
        if (cell.isFree() && !cell.equals(other_cell) && (other_cell.getLevel().getBlockId() - cell.getLevel().getBlockId()<2) && cell.getLevel().getBlockId() != 4){
            return true;
        } else return false;
    }

    public void print(){
        System.out.println(" \t   0\t 1\t   2\t 3\t   4\n");
        for(int i=0; i<5; i++){
            System.out.print(i + "\t| ");
            for(int j=0; j<5; j++){
                System.out.print(board[i][j] + " | ");
            }
            System.out.println("\n");
        }
    }

    @Override
    protected final Board clone() throws CloneNotSupportedException{
        final Board result = new Board();
        for(int i = 0; i < 5; i++){
            for(int j=0; j<5; j++)
            result.board[i][j] = (Cell) board[i][j].clone();
        }
        return result;
    }
}
