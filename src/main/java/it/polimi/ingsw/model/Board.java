package it.polimi.ingsw.model;

import java.io.Serializable;

/**
 * This class defines the game's board.
 */
public class Board implements Serializable, Cloneable {
    private Cell[][] board;
    private final Player[] players;

    /**
     * This is the class' constructor.
     * @param players are the game's players
     */
    public Board(Player[] players){
        this.players = players;
        this.reset();
    }

    /**
     * @param x is the x value of the cell.
     * @param y is the y value of the cell.
     * @return the cell (x,y).
     * @throws IllegalArgumentException if the value 'x' or 'y' is higher or equal than 4 and if they are lower than 0.
     */
    public Cell getCell(int x, int y) throws IllegalArgumentException{
        if((x < 0 || x >= 5) || (y < 0 || y >= 5)){
            throw new IllegalArgumentException();
        }
        return board[x][y];
    }

    /**
     * This method resets the board and sets all the previous cells to new ones.
     */
    public void reset(){
        board=new Cell[5][5];
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 5; j++){
                board[i][j] = new Cell(i,j);
            }
        }
    }

    /**
     * Print board in standard output as string
     */
    public synchronized void print(){
        try {
            System.out.println(" \t\t 0\t\t   1\t\t 2\t\t   3\t     4");
            System.out.println("\t---------------------------------------------------");
            for (int i = 0; i < 5; i++) {
                System.out.print(i + "\t| ");
                for (int j = 0; j < 5; j++) {//prima riga
                    System.out.print(board[j][i] + " | ");
                }
                System.out.println();
                //qui devo stampare player e worker (seconda riga)
                System.out.print("\t");
                for (int j = 0; j < 5; j++) {
                    System.out.print("| ");
                    if (!board[j][i].isFree()) {
                        for (int p = 0; p < players.length; p++) {
                            Player player = players[p];

                            if (player.getWorker(0).getCell().equals(board[j][i])) {
                                System.out.print("P:" + p + " W:0 ");
                                break;
                            } else if (player.getWorker(1).getCell().equals(board[j][i])) {
                                System.out.print("P:" + p + " W:1 ");
                                break;
                            }

                        }
                    } else {
                        System.out.print("P:n W:n ");
                    }
                }
                System.out.print("|");
                System.out.println("\n\t---------------------------------------------------");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @return the players using this board
     */
    public Player[] getPlayers(){
        return players;
    }

    /**
     * @return a new instance of the class as a clone
     * @throws CloneNotSupportedException if it can't be cloneable
     */
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
