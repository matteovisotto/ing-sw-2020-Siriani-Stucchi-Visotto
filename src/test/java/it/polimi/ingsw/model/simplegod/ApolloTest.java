package it.polimi.ingsw.model.simplegod;

import it.polimi.ingsw.model.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class ApolloTest {

    @Test
    public void testUsePower() {
        Cell cell = new Cell(1,2);
        Cell cell2 = new Cell(3,3);
        Worker worker = new Worker(cell);
        Worker worker1 = new Worker(cell2);
        Player player = new Player("Mario");
        Player player2 = new Player("Luigi");
        player.setWorkers(worker);
        player2.setWorkers(worker1);
        List<Object> workersList = new ArrayList<>();
        workersList.add(worker);
        workersList.add(worker1);
        GodCard godCard = new Apollo();
        godCard.usePower(workersList);
        assertTrue(player.getWorker(0).getCell().getX()==3 && player.getWorker(0).getCell().getY()==3);
        assertTrue(player2.getWorker(0).getCell().getX()==1 && player2.getWorker(0).getCell().getY()==2);
    }

    @Test
    public void setFirstBuildTest(){
        GodCard godCard = new Apollo();
        godCard.setFirstBuilt(new Cell(1,2));
        Cell cell = new Cell(1,2);
        assertEquals(godCard.getFirstBuilt(),cell);
    }

    @Test
    public void hasMovedTest(){
        GodCard godCard = new Apollo();
        godCard.hasMoved(true);
        assertTrue(godCard.isMoved());
    }

    @Test
    public void setBuildTest(){
        GodCard godCard = new Apollo();
        godCard.setBuild(true);
        assertTrue(godCard.hasBuilt());
    }

    @Test
    public void resetTest(){
        GodCard godCard = new Apollo();
        godCard.hasMoved(true);
        godCard.reset();
        assertFalse(godCard.isMoved());
    }

    @Test
    public void getPhaseTest(){
        GodCard godCard = new Apollo();
        Phase phase = godCard.getPhase();
        assertEquals(phase,Phase.MOVE);
    }

    @Test
    public void setActiveTest(){
        GodCard godCard = new Apollo();
        godCard.setActive(true);
        assertTrue(godCard.isActive());
    }
}
