package it.polimi.ingsw.model.simplegod;

import it.polimi.ingsw.model.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class AthenaTest {
    @Test
    public void testUsePower() {
        Cell cell = new Cell(1,2);
        Cell cell2 = new Cell(3,3);
        Cell cell3 = new Cell(4,3);
        Worker worker = new Worker(cell);
        Worker worker1 = new Worker(cell2);
        Worker worker2 = new Worker(cell3);
        Player player = new Player("Mario");
        Player player2 = new Player("Luigi");
        Player player3 = new Player("Toad");
        player.setWorkers(worker);
        player2.setWorkers(worker1);
        player3.setWorkers(worker2);
        Player[] players = new Player[3];
        players[0] = player;
        players[1] = player2;
        players[2] = player3;
        GodCard godCard = new Athena();
        GodCard godCard2 = new Apollo();
        GodCard godCard3 = new Prometheus();
        players[0].setGodCard(godCard);
        players[1].setGodCard(godCard2);
        players[2].setGodCard(godCard3);
        List<Object> athenaList = new ArrayList<>();
        Model model = new Model(players,true);
        athenaList.add(model);
        Model.athenaId = model.getAthenaPlayer();
        model.getActualPlayer().getGodCard().usePower(athenaList);
        assertTrue(model.isMovedUp());
        model.updateTurn();
        assertTrue(model.isMovedUp());
        model.updateTurn();
        assertTrue(model.isMovedUp());
    }

    @Test
    public void getPhaseTest(){
        GodCard godCard = new Athena();
        Phase phase = godCard.getPhase();
        assertEquals(phase,Phase.MOVE);
    }
}
