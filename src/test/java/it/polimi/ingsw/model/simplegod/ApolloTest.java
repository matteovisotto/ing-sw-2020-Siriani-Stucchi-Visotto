package it.polimi.ingsw.model.simplegod;

import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.GodCard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Worker;
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
}
