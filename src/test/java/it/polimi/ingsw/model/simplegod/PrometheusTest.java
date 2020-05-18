package it.polimi.ingsw.model.simplegod;

import it.polimi.ingsw.model.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class PrometheusTest{
    @Test
    public void testUsePower() {
        Cell cell = new Cell(1, 2);
        Cell cell2 = new Cell(3, 3);
        Worker worker = new Worker(cell);
        Worker worker1 = new Worker(cell2);
        Player player = new Player("Mario");
        Player player2 = new Player("Luigi");
        Player[] players = new Player[2];
        players[0] = player;
        players[1] = player2;
        player.setWorkers(worker);
        player2.setWorkers(worker1);
        Model model = new Model(players, true);
        List<Object> buildList = new ArrayList<>();
        buildList.add(model);
        GodCard godCard = new Prometheus();
        model.getActualPlayer().setGodCard(godCard);
        model.getActualPlayer().getGodCard().usePower(buildList);
        assertTrue(((Prometheus)model.getActualPlayer().getGodCard()).hasUsedPower());
        assertEquals(model.getPhase(),Phase.PROMETHEUS_WORKER);
    }

    @Test
    public void setFirstBuildTest(){
        GodCard godCard = new Prometheus();
        godCard.setFirstBuilt(new Cell(1,2));
        Cell cell = new Cell(1,2);
        assertEquals(godCard.getFirstBuilt(),cell);
    }

    @Test
    public void hasMovedTest(){
        GodCard godCard = new Prometheus();
        godCard.hasMoved(true);
        assertTrue(godCard.isMoved());
    }

    @Test
    public void setBuildTest(){
        GodCard godCard = new Prometheus();
        godCard.setBuild(true);
        assertTrue(godCard.hasBuilt());
    }

    @Test
    public void resetTest(){
        GodCard godCard = new Prometheus();
        godCard.setBuild(true);
        godCard.reset();
        assertFalse(godCard.hasBuilt());
    }

    @Test
    public void getPhaseTest(){
        GodCard godCard = new Prometheus();
        Phase phase = godCard.getPhase();
        assertEquals(phase,Phase.PROMETHEUS_WORKER);
    }
}


