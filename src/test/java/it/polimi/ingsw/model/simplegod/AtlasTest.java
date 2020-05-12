package it.polimi.ingsw.model.simplegod;

import it.polimi.ingsw.model.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class AtlasTest {
    @Test
    public void testUsePower() {
        Cell cell = new Cell(1,2);
        List<Object> cellList = new ArrayList<>();
        cellList.add(cell);
        GodCard godCard = new Atlas();
        godCard.usePower(cellList);
        assertEquals(4, cell.getLevel().getBlockId());
    }

    @Test
    public void setFirstBuildTest(){
        GodCard godCard = new Atlas();
        godCard.setFirstBuilt(new Cell(1,2));
        Cell cell = new Cell(1,2);
        assertEquals(godCard.getFirstBuilt(),cell);
    }

    @Test
    public void hasMovedTest(){
        GodCard godCard = new Atlas();
        godCard.hasMoved(true);
        assertTrue(godCard.isMoved());
    }

    @Test
    public void setBuildTest(){
        GodCard godCard = new Atlas();
        godCard.setBuild(true);
        assertTrue(godCard.hasBuilt());
    }

    @Test
    public void resetTest(){
        GodCard godCard = new Atlas();
        godCard.hasMoved(true);
        godCard.reset();
        assertFalse(godCard.isMoved());
    }

    @Test
    public void getPhaseTest(){
        GodCard godCard = new Atlas();
        Phase phase = godCard.getPhase();
        assertEquals(phase,Phase.BUILD);
    }
}
