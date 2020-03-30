package it.polimi.ingsw.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class CellTest {

    @Test
    public void getX() {
        Cell cell = new Cell(1,2);
        assertTrue("Result", 1 == cell.getX());
    }

    @Test
    public void getY() {
        Cell cell = new Cell(1,2);
        assertTrue("Result", 2 == cell.getY());
    }

    @Test
    public void getLevel() {
        Cell cell = new Cell(1,2);
        assertTrue(cell.getLevel().getBlockId() == Blocks.EMPTY.getBlockId());
    }

    @Test
    public void setLevel() {
        Cell cell = new Cell(1,2);
        cell.setLevel(Blocks.DOME);
        assertTrue(cell.getLevel().getBlockId() == Blocks.DOME.getBlockId());
    }

    @Test
    public void isFree() {
        Cell cell = new Cell(1,2);
        assertTrue(cell.isFree());
    }

    @Test
    public void freeCell() {
        Cell cell = new Cell(1,2);
        cell.freeCell();
        assertTrue(cell.isFree());
    }

    @Test
    public void useCell() {
        Cell cell = new Cell(1,2);
        cell.useCell();
        assertFalse(cell.isFree());
    }
}