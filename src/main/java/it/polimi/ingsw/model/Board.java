package it.polimi.ingsw.model;

import java.io.Serializable;

/**
 * This class define the board of the game.
 */
public class Board implements Serializable, Cloneable {
    private Cell[][] board;
    private Player[] players;

    public Board(Player[] players){
        this.players = players;
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
            for(int j = 0; j < 5; j++){
                board[i][j] = new Cell(i,j);
            }
        }
    }


    public boolean checkCell (int x, int y, Worker actualWorker, int maxUpDifference) throws IllegalArgumentException{
        Cell nextCell = getCell(x,y);
        Cell actualCell = actualWorker.getCell();
        return nextCell.isFree() && !nextCell.equals(actualCell) && (nextCell.getLevel().getBlockId() - actualCell.getLevel().getBlockId() < maxUpDifference) && nextCell.getLevel().getBlockId() != 4;
    }

    public boolean checkCellApollo (int x, int y, Worker actualWorker, int maxUpDifference) throws IllegalArgumentException{
        Cell nextCell = getCell(x,y);
        Cell actualCell = actualWorker.getCell();
        boolean isPlayerSwitchable = false;
        for (int p = 0; p < players.length; p++) {
            Player player = players[p];
            //controllo che il player non sia quello del turno
            if(!player.getGodCard().getCardGod().equals(Gods.APOLLO)){
                if(!nextCell.isFree() && (player.getWorker(0).getCell() == nextCell || player.getWorker(1).getCell() == nextCell)){
                    isPlayerSwitchable = true;
                }
            }
        }
        return (nextCell.isFree() || isPlayerSwitchable) && !nextCell.equals(actualCell) && (nextCell.getLevel().getBlockId() -  actualCell.getLevel().getBlockId()< maxUpDifference) && nextCell.getLevel().getBlockId() != 4;
    }
    public boolean checkCellMinotaur (int x, int y, Worker actualWorker, int maxUpDifference) throws IllegalArgumentException{
        Cell nextCell = getCell(x,y);
        Cell actualCell = actualWorker.getCell();
        Cell behindCell;
        boolean isEnemyPushable = false;
        for (int p = 0; p < players.length; p++) {
            Player player = players[p];
            //controllo che il player non sia quello del turno
            if(!player.getGodCard().getCardGod().equals(Gods.MINOTAUR)){
                if(!nextCell.isFree() && (player.getWorker(0).getCell() == nextCell || player.getWorker(1).getCell() == nextCell)){
                    behindCell = this.getCell((nextCell.getX() - actualCell.getX()) + nextCell.getX(), (nextCell.getY() - actualCell.getY()) + nextCell.getY());
                    if(behindCell.getLevel().getBlockId() != 4 && behindCell.isFree()){
                        isEnemyPushable = true;
                    }
                }
            }
        }
        return (nextCell.isFree() || isEnemyPushable) && !nextCell.equals(actualCell) && (nextCell.getLevel().getBlockId() - actualCell.getLevel().getBlockId() < maxUpDifference) && nextCell.getLevel().getBlockId() != 4;
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
                            System.out.print("P:" + p + " W:0 ");
                            break;
                        }
                        if(player.getWorker(1).getCell().equals(board[j][i])){
                            System.out.print("P:" + p + " W:1 ");
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
