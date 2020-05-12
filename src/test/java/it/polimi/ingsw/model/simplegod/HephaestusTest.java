package it.polimi.ingsw.model.simplegod;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.messageModel.PlayerMove;
import it.polimi.ingsw.server.ClientConnection;
import it.polimi.ingsw.view.RemoteView;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class HephaestusTest {
    @Test
    public void testUsePower() {
        Cell cell = new Cell(1,2);
        Cell cellBuilt = new Cell(2,2);
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
        RemoteView remoteView = new RemoteView(players[0], players[1].getPlayerName(), new ClientConnection() {
            @Override
            public void closeConnection() {

            }

            @Override
            public void send(Object message) {

            }

            @Override
            public void asyncSend(Object message) {

            }
        });
        Model model = new Model(players,true);
        List<Object> buildAgainList = new ArrayList<>();
        buildAgainList.add(model);
        GodCard godCard = new Hephaestus();
        godCard.setFirstBuilt(cellBuilt);
        model.getActualPlayer().setGodCard(godCard);
        model.getActualPlayer().getGodCard().setFirstBuilt(cellBuilt);
        Cell cellFirstBuild = model.getActualPlayer().getGodCard().getFirstBuilt();
        model.getActualPlayer().getGodCard().usePower(buildAgainList);
        assertEquals(cellFirstBuild.getLevel().getBlockId(), Blocks.LEVEL1.getBlockId());
    }

    @Test
    public void setFirstBuildTest(){
        GodCard godCard = new Hephaestus();
        godCard.setFirstBuilt(new Cell(1,2));
        Cell cell = new Cell(1,2);
        assertEquals(godCard.getFirstBuilt(),cell);
    }

    @Test
    public void hasMovedTest(){
        GodCard godCard = new Hephaestus();
        godCard.hasMoved(true);
        assertTrue(godCard.isMoved());
    }

    @Test
    public void setBuildTest(){
        GodCard godCard = new Hephaestus();
        godCard.setBuild(true);
        assertTrue(godCard.hasBuilt());
    }

    @Test
    public void resetTest(){
        GodCard godCard = new Hephaestus();
        godCard.hasMoved(true);
        godCard.reset();
        assertFalse(godCard.isMoved());
    }

    @Test
    public void getPhaseTest(){
        GodCard godCard = new Hephaestus();
        Phase phase = godCard.getPhase();
        assertEquals(phase,Phase.BUILD);
    }
}
