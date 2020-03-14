package it.polimi.ingsw.model;

public class Board {
    private Cell[][] board;

    public Board(){
        this.reset();
    }

    public Cell getCell(int x, int y){
        return board[x][y];
    }

    public void reset(){
        board=new Cell[5][5];
        for(int i=0; i<5; i++){
            for(int j=0; j<5; j++){
                board[i][j]=new Cell();
            }
        }
    }

    public int getCellLevel(int x, int y){
        return board[x][y].getLevel().getBlock();
    }
}
