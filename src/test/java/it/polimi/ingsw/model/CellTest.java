package it.polimi.ingsw.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class CellTest {

    @Test
    public void getX() {
        Cell cell = new Cell(1,2);
        assertTrue("Result", 1==cell.getX());
    }

    @Test
    public void getY() {
        Cell cell = new Cell(1,2);
        assertTrue("Result", 2==cell.getY());
    }
}