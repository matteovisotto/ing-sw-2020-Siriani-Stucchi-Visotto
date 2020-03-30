package it.polimi.ingsw.model;

import it.polimi.ingsw.exceptions.FullWorkerException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

public class PlayerTest {
    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void getPlayerName() {
        Player player=new Player("Mario");
        assertEquals("Mario", player.getPlayerName());
    }

    /*@Test
    public void getCard() {
        Player player=new Player("Mario");

    }*/

    @Test
    public void setWorkers() {
        Cell cell=new Cell(1,1);
        Player player=new Player("Mario");
        Worker worker=new Worker(cell);
        player.setWorkers(worker);
        assertEquals(worker, player.getWorker(0));
    }

    @Test
    public void getWorker() {
        Cell cell=new Cell(1,1);
        Cell cell2 = new Cell(2,2);
        Player player=new Player("Mario");
        Worker worker=new Worker(cell);
        Worker worker2=new Worker(cell2);
        player.setWorkers(worker);
        player.setWorkers(worker2);

        assertNotEquals(worker2, player.getWorker(0));
        assertNotEquals(worker, player.getWorker(1));
        assertEquals(worker, player.getWorker(0));
        assertEquals(worker2, player.getWorker(1));
    }




    @Test
    public void setStatus() {

    }

    @Test
    public void getStatus() {

    }

    @Test
    public void setWorkersException(){
        exception.expect(FullWorkerException.class);
        Player player=new Player("Mario");
        Worker worker=new Worker(new Cell(1,1));
        player.setWorkers(worker);
        Worker worker1=new Worker(new Cell(2,1));
        player.setWorkers(worker1);
        Worker worker2=new Worker(new Cell(3,1));
        player.setWorkers(worker2);
    }

    @Test
    public void getWorkerException(){
        exception.expect(IndexOutOfBoundsException.class);
        Cell cell=new Cell(1,1);
        Cell cell2 = new Cell(2,2);
        Player player=new Player("Mario");
        Worker worker=new Worker(cell);
        Worker worker2=new Worker(cell2);
        player.setWorkers(worker);
        player.setWorkers(worker2);

        player.getWorker(3);
    }
}