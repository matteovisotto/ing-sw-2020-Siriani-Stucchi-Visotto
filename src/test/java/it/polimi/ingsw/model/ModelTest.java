package it.polimi.ingsw.model;

import it.polimi.ingsw.model.messageModel.PlayerMove;
import it.polimi.ingsw.model.messageModel.PlayerWorker;
import it.polimi.ingsw.model.simplegod.Apollo;
import it.polimi.ingsw.model.simplegod.Atlas;
import it.polimi.ingsw.model.simplegod.Minotaur;
import it.polimi.ingsw.server.ClientConnection;
import it.polimi.ingsw.view.RemoteView;
import it.polimi.ingsw.view.View;
import org.junit.Test;

import java.util.EnumMap;
import java.util.Map;

import static org.junit.Assert.*;

public class ModelTest {
    @Test
    public void testIsPlayerTurn() {
        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        Model model = new Model(players,true);
        assertTrue(model.isPlayerTurn(players[0]));
        assertFalse(model.isPlayerTurn(players[1]));
    }

    @Test
    public void testUpdateTurn(){
        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");

        Worker worker= new Worker(new Cell(0,0));
        players[0].setWorkers(worker);
        worker= new Worker(new Cell(0,1));
        players[0].setWorkers(worker);

        worker= new Worker(new Cell(1,0));
        players[1].setWorkers(worker);
        worker= new Worker(new Cell(1,1));
        players[1].setWorkers(worker);

        Model model = new Model(players,true);
        model.updateTurn();
        assertEquals(model.getActualPlayer(),players[1]);
        model.updateTurn();
        assertEquals(model.getActualPlayer(),players[0]);
    }

    @Test
    public void testUpdatePhase(){
        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        Model model = new Model(players,true);
        Phase phase = Phase.SETWORKER2;
        Worker worker= new Worker(new Cell(0,0));
        players[0].setWorkers(worker);
        worker= new Worker(new Cell(0,1));
        players[0].setWorkers(worker);

        worker= new Worker(new Cell(1,0));
        players[1].setWorkers(worker);
        worker= new Worker(new Cell(1,1));
        players[1].setWorkers(worker);

        model.updatePhase();
        assertTrue(phase==model.getPhase());
        model.updatePhase();
        assertFalse(phase==model.getPhase());
    }

    @Test
    public void testSetPlayerWorker(){
        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        Model model = new Model(players,true);
        Cell cell = new Cell(1,2);
        Worker worker = new Worker(cell);
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
        PlayerWorker playerWorker = new PlayerWorker(players[0],1,2,remoteView);
        model.setPlayerWorker(playerWorker);
        assertTrue(cell.getX()==playerWorker.getX() && cell.getY()==playerWorker.getY());
    }

    @Test
    public void testChooseCard(){
        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        Model model = new Model(players,true);
        for (int i = 0; i < 10000; i++){
            GodCard[] godCards = model.chooseCards();
            assertTrue(godCards[0]!=null && godCards[1]!=null && godCards[0]!=godCards[1]);
        }
        Player[] players2 = new Player[3];
        players2[0] = new Player("Mario");
        players2[1] = new Player("Luigi");
        players2[2] = new Player("Toad");
        Model model2 = new Model(players2,true);
        for (int i = 0; i < 10000; i++){
            GodCard[] godCards2 = model2.chooseCards();
            assertTrue(godCards2[0]!=null && godCards2[1]!=null && godCards2[2]!=null && godCards2[0]!=godCards2[1] && godCards2[0]!=godCards2[2] && godCards2[1]!=godCards2[2]);
        }
    }
    @Test
    public void testAssignCard() {
        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        Model model = new Model(players, true);
        GodCard godCard = new Apollo();
        model.assignCard(players[0],godCard);
        assertEquals(players[0].getGodCard(),godCard);
    }

    @Test
    public void testMove() {
        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        Model model = new Model(players, true);
        players[0].setWorkers(new Worker(new Cell(1,2)));
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
        PlayerMove playerMove = new PlayerMove(players[0],0,2,2,remoteView);
        model.move(playerMove);
        assertTrue(players[0].getWorker(0).getCell().getX()==2 && players[0].getWorker(0).getCell().getX()==2);
    }
    @Test
    public void testIncreaseLevel() {
        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        Model model = new Model(players, true);
        Cell cell = model.getBoard().getCell(1,2);
        model.increaseLevel(cell,Blocks.LEVEL1);
        assertEquals(1, model.getBoard().getCell(1, 2).getLevel().getBlockId());
    }
}

