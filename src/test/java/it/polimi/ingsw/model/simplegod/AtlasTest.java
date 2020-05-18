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
        Player player = new Player("Mario");
        Player player2 = new Player("Luigi");
        Player[] players = new Player[2];
        players[0] = player;
        players[1] = player2;
        Model model = new Model(players,false);
        cellList.add(model);
        GodCard godCard = new Atlas();
        model.getActualPlayer().setGodCard(godCard);
        model.getActualPlayer().getGodCard().usePower(cellList);
        assertTrue(((Atlas)model.getActualPlayer().getGodCard()).hasUsedPower());
        assertEquals(model.getPhase(),Phase.BUILD);
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
