package it.polimi.ingsw.model.simplegod;

import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.GodCard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Worker;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class MinotaurTest {
    @Test
    public void testUsePower() {
        Cell cell = new Cell(3,2);
        Cell cell2 = new Cell(2,2);
        Cell backwardCell = new Cell(1,2);
        Worker worker = new Worker(cell);
        Worker worker1 = new Worker(cell2);
        Player player = new Player("Mario");
        Player player2 = new Player("Luigi");
        player.setWorkers(worker);
        player2.setWorkers(worker1);
        List<Object> movedList = new ArrayList<>();
        movedList.add(worker);
        movedList.add(worker1);
        movedList.add(backwardCell);
        GodCard godCard = new Minotaur();
        godCard.usePower(movedList);
        assertTrue(player.getWorker(0).getCell().getX()==2 && player.getWorker(0).getCell().getY()==2);
        assertTrue(player2.getWorker(0).getCell().getX()==1 && player2.getWorker(0).getCell().getY()==2);
    }
}
