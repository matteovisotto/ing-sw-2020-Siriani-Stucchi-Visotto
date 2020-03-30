package it.polimi.ingsw.model;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

public class BlocksTest {
    @Rule
    public final ExpectedException exception = ExpectedException.none();
    @Test
    public void getBlockId() {
        assertTrue(0 == Blocks.EMPTY.getBlockId());
        assertTrue(1 == Blocks.LEVEL1.getBlockId());
        assertTrue(2 == Blocks.LEVEL2.getBlockId());
        assertTrue(3 == Blocks.LEVEL3.getBlockId());
        assertTrue(4 == Blocks.DOME.getBlockId());
    }

    @Test
    public void getBlock() {
        assertEquals(Blocks.getBlock(0), Blocks.EMPTY);
        assertEquals(Blocks.getBlock(1), Blocks.LEVEL1);
        assertEquals(Blocks.getBlock(2), Blocks.LEVEL2);
        assertEquals(Blocks.getBlock(3), Blocks.LEVEL3);
        assertEquals(Blocks.getBlock(4), Blocks.DOME);
    }

    @Test
    public void getBlockException(){
        exception.expect(IllegalArgumentException.class);
        Blocks.getBlock(10);
    }
}