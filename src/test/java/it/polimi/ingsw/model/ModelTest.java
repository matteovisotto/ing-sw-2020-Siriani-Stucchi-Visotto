package it.polimi.ingsw.model;

import it.polimi.ingsw.model.messageModel.PlayerMove;
import it.polimi.ingsw.model.messageModel.PlayerWorker;
import it.polimi.ingsw.model.simplegod.Apollo;
import it.polimi.ingsw.model.simplegod.Athena;
import it.polimi.ingsw.model.simplegod.Atlas;
import it.polimi.ingsw.server.ClientConnection;
import it.polimi.ingsw.view.RemoteView;
import org.junit.Test;


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
    public void isSimplePlayTest(){
        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        Model model = new Model(players,true);
        assertTrue(model.isSimplePlay());
    }

    @Test
    public void updateTurnTest(){
        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");

        Worker worker = new Worker(new Cell(0,0));
        players[0].setWorkers(worker);
        worker = new Worker(new Cell(0,1));
        players[0].setWorkers(worker);

        worker = new Worker(new Cell(1,0));
        players[1].setWorkers(worker);
        worker = new Worker(new Cell(1,1));
        players[1].setWorkers(worker);

        Model model = new Model(players,true);
        model.updateTurn();
        assertEquals(model.getActualPlayer(),players[1]);
        model.updateTurn();
        assertEquals(model.getActualPlayer(),players[0]);
    }

   /* @Test
    public void updateTurnLoseTest(){
        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");

        Worker worker = new Worker(new Cell(0,0));
        players[0].setWorkers(worker);
        worker = new Worker(new Cell(0,1));
        players[0].setWorkers(worker);

        worker = new Worker(new Cell(1,0));
        players[1].setWorkers(worker);
        worker = new Worker(new Cell(1,1));
        players[1].setWorkers(worker);

        Model model = new Model(players,true);

        players[0].getWorker(0).setStatus(false);
        players[0].getWorker(1).setStatus(false);

        model.updateTurn();
        model.updateTurn();
        assertTrue(model.getActualPlayer().getHasLost());
    }*/

    @Test
    public void getPlayerTest(){
        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        Model model = new Model(players,true);
        assertEquals(model.getPlayer(0),players[0]);
        assertEquals(model.getPlayer(1),players[1]);
    }

    @Test
    public void getLeftCardTest(){
        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        Model model = new Model(players, false);
        GodCard godCard = new Apollo();
        GodCard godCard1 = new Athena();
        GodCard godCard2 = new Atlas();
        model.addGod(godCard);
        assertEquals(model.getLeftCards(),1);
        model.addGod(godCard1);
        assertEquals(model.getLeftCards(),2);
        model.addGod(godCard2);
        assertEquals(model.getLeftCards(),3);
    }

    @Test
    public void isGodAvailableTest(){
        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        Model model = new Model(players, false);
        GodCard godCard = new Apollo();
        GodCard godCard1 = new Athena();
        model.addGod(godCard);
        model.addGod(godCard1);
        assertTrue(model.isGodAvailable(0));
        assertTrue(model.isGodAvailable(1));
    }

    @Test
    public void getGCPlayerTest(){
        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        Model model = new Model(players, false);
        GodCard godCard = new Apollo();
        GodCard godCard1 = new Athena();
        model.addGod(godCard);
        model.addGod(godCard1);
        model.assignCard(players[1],1);
        model.assignCard(players[0],0);
        assertEquals(players[0],model.getGCPlayer(godCard.getCardGod()));
        assertEquals(players[1],model.getGCPlayer(godCard1.getCardGod()));
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
        assertSame(phase, model.getPhase());
        model.updatePhase();
        assertNotSame(phase, model.getPhase());
    }

    @Test
    public void testSetPlayerWorker(){
        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        Model model = new Model(players,true);
        Cell cell = new Cell(1,2);
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
        }, null);
        PlayerWorker playerWorker = new PlayerWorker(players[0],1,2,remoteView);
        model.setPlayerWorker(playerWorker);
        assertTrue(cell.getX()==playerWorker.getX() && cell.getY()==playerWorker.getY());
    }

    /*@Test
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
    }*/

    @Test
    public void assignCardTest(){
        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        Model model = new Model(players, false);
        GodCard godCard = new Apollo();
        GodCard godCard1 = new Athena();
        model.addGod(godCard);
        model.addGod(godCard1);
        model.assignCard(players[0],0);
        assertEquals(godCard,model.getActualPlayer().getGodCard());
    }

    @Test
    public void moveTest() {
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
        }, null);
        PlayerMove playerMove = new PlayerMove(players[0],0,2,2,remoteView);
        model.move(playerMove);
        assertTrue(players[0].getWorker(0).getCell().getX()==2 && players[0].getWorker(0).getCell().getX()==2);
    }

    @Test
    public void increaseLevelTest() {
        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        Model model = new Model(players, true);
        Cell cell = model.getBoard().getCell(1,2);
        model.increaseLevel(cell,Blocks.LEVEL1);
        assertEquals(1, model.getBoard().getCell(1, 2).getLevel().getBlockId());
    }

    @Test
    public void victoryTest(){
        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        Model model = new Model(players, true);
        model.victory(players[0]);
        assertTrue(model.getActualPlayer().hasWon());
    }

    /*@Test
    public void looseTest(){
        Player[] players = new Player[3];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        players[2] = new Player("Toad");
        Model model = new Model(players, true);
        model.loose(players[0]);
        assertTrue(model.getActualPlayer().getHasLost());
        assertEquals(2, model.getLeftPlayers());
    }*/
}

