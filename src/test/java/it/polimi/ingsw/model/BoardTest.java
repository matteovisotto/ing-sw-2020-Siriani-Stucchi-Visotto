package it.polimi.ingsw.model;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

public class BoardTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void getCell() {
        Player[] players=new Player[2];
        players[0]=new Player("pioppo");
        players[1]=new Player("Amedeus");
        Board board=new Board(players);
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 5; j++){
                assertEquals(i, board.getCell(i, j).getX());
                assertEquals(j, board.getCell(i, j).getY());
            }
        }
    }

    @Test
    public void checkCell() {
        Player[] players=new Player[2];
        players[0]=new Player("pioppo");
        players[1]=new Player("Amedeus");
        Board board=new Board(players);
        Worker worker= new Worker(board.getCell(1,1));
        assertTrue(board.checkCell(1,2,worker));
        assertFalse(board.checkCell(1,1,worker));
        board.getCell(2,1).setLevel(Blocks.DOME);
        assertFalse(board.checkCell(2,1,worker));

    }

    @Test
    public void getCellException(){
        exception.expect(IllegalArgumentException.class);
        Player[] players=new Player[2];
        players[0]=new Player("pioppo");
        players[1]=new Player("Amedeus");
        Board board=new Board(players);
        int i = -1,j = -1;
        board.getCell(i,j).getX();
        board.getCell(i,j).getY();
    }
}