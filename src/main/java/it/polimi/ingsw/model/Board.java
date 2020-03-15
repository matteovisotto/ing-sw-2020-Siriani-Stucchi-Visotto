package it.polimi.ingsw.model;

public class Board {
    private Cell[][] board;

    public static Board shared = new Board();

    public Board(){
        this.reset();
    }

    public Cell getCell(int x, int y) throws IllegalArgumentException{
        if((x<0 || x>5) || (y<0 || y>5)){
            throw new IllegalArgumentException();
        }
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
        return board[x][y].getLevel().getBlockId();
    }
}
