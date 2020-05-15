package it.polimi.ingsw.model;

import java.io.Serializable;

/**
 * This class define the board of the game.
 */
public class Board implements Serializable, Cloneable {
    private Cell[][] board;
    private Player[] players;

    public Board(Player[] players){
        this.players=players;
        this.reset();
    }

    public Cell getCell(int x, int y) throws IllegalArgumentException{
        if((x < 0 || x >= 5) || (y < 0 || y >= 5)){
            throw new IllegalArgumentException();
        }
        return board[x][y];
    }

    public void reset(){
        board=new Cell[5][5];
        for(int i = 0; i < 5; i++){
            for(int j=0; j<5; j++){
                board[i][j]=new Cell(i,j);
            }
        }
    }


    public boolean checkCell (int x, int y, Worker worker) throws IllegalArgumentException{
        Cell cell = getCell(x,y);
        Cell other_cell = worker.getCell();
        return cell.isFree() && !cell.equals(other_cell) && (cell.getLevel().getBlockId() -  other_cell.getLevel().getBlockId()< 2) && cell.getLevel().getBlockId() != 4;
    }
    public boolean checkCellApollo (int x, int y, Worker worker) throws IllegalArgumentException{
        Cell cell = getCell(x,y);
        Cell other_cell = worker.getCell();
        boolean test=false;
        for (int p = 0; p < players.length; p++) {
            Player player = players[p];
            //controllo che il player non sia quello del turno
            if(!player.getGodCard().getCardGod().equals(SimpleGods.APOLLO)){
                if(!cell.isFree() && (player.getWorker(0).getCell()==cell || player.getWorker(1).getCell()==cell)){
                    test=true;
                }
            }
        }
        return (cell.isFree() || test) && !cell.equals(other_cell) && (cell.getLevel().getBlockId() -  other_cell.getLevel().getBlockId()< 2) && cell.getLevel().getBlockId() != 4;
    }

    public synchronized void print(){
        System.out.println(" \t\t 0\t\t   1\t\t 2\t\t   3\t     4");
        System.out.println("\t---------------------------------------------------");
        for(int i = 0; i < 5; i++){
            System.out.print(i + "\t| ");
            for(int j = 0; j < 5; j++){//prima riga
                System.out.print(board[j][i] + " | ");
            }
            System.out.println();
            //qui devo stampare player e worker (seconda riga)
            System.out.print("\t");
            for(int j = 0; j < 5; j++) {
                System.out.print("| ");
                if (!board[j][i].isFree()){
                    for (int p = 0; p < players.length; p++) {
                        Player player = players[p];
                        if(player.getWorker(0).getCell().equals(board[j][i])){
                            System.out.print("P:"+p+" W:0 ");
                            break;
                        }
                        if(player.getWorker(1).getCell().equals(board[j][i])){
                            System.out.print("P:"+p+" W:1 ");
                            break;
                        }
                    }
                }
                else{
                    System.out.print("P:n W:n ");
                }
            }
            System.out.print("|");
            System.out.println("\n\t---------------------------------------------------");
        }
    }

    @Override
    protected final Board clone() throws CloneNotSupportedException{
        super.clone();
        final Board result = new Board(players);
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 5; j++)
            result.board[i][j] = (Cell) board[i][j].clone();
        }
        return result;
    }
}
