package it.polimi.ingsw.model.simplegod;

import it.polimi.ingsw.model.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class DemeterTest {
    @Test
    public void testUsePower() {
        Cell cell = new Cell(1,2);
        Cell cell2 = new Cell(3,3);
        Worker worker = new Worker(cell);
        Worker worker1 = new Worker(cell2);
        Player player = new Player("Mario");
        Player player2 = new Player("Luigi");
        Player[] players = new Player[2];
        players[0] = player;
        players[1] = player2;
        player.setWorkers(worker);
        player2.setWorkers(worker1);
        Model model = new Model(players,true);
        List<Object> builtList = new ArrayList<>();
        builtList.add(model);
        GodCard godCard = new Demeter();
        model.getActualPlayer().setGodCard(godCard);
        model.getActualPlayer().getGodCard().usePower(builtList);
        assertTrue(((Demeter)model.getActualPlayer().getGodCard()).hasUsedPower());
        assertEquals(model.getPhase(),Phase.BUILD);
    }

    @Test
    public void setFirstBuildTest(){
        GodCard godCard = new Demeter();
        godCard.setFirstBuilt(new Cell(1,2));
        Cell cell = new Cell(1,2);
        assertEquals(godCard.getFirstBuilt(),cell);
    }

    @Test
    public void hasMovedTest(){
        GodCard godCard = new Demeter();
        godCard.hasMoved(true);
        assertTrue(godCard.isMoved());
    }

    @Test
    public void setBuildTest(){
        GodCard godCard = new Demeter();
        godCard.setBuild(true);
        assertTrue(godCard.hasBuilt());
    }

    @Test
    public void resetTest(){
        GodCard godCard = new Demeter();
        godCard.hasMoved(true);
        godCard.reset();
        assertFalse(godCard.isMoved());
    }

    @Test
    public void getPhaseTest(){
        GodCard godCard = new Demeter();
        Phase phase = godCard.getPhase();
        assertEquals(phase,Phase.BUILD);
    }
}
