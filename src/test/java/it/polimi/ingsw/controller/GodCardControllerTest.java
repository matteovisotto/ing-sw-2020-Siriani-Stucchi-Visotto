package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.messageModel.*;
import it.polimi.ingsw.model.simplegod.Apollo;
import it.polimi.ingsw.model.simplegod.Arthemis;
import it.polimi.ingsw.model.simplegod.Athena;
import it.polimi.ingsw.server.ClientConnection;
import it.polimi.ingsw.view.RemoteView;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;


import static org.junit.Assert.*;
public class GodCardControllerTest {

    @Test
    public void checkPhaseTest(){
        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        Model model = new Model(players,false);
        GodCardController controller = new GodCardController(model);
        RemoteView remoteView = new RemoteView(players[0], players[1].getPlayerName(), new ClientConnection () {
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
        DrawedCards drawedCards = new DrawedCards(players[0],0,2,remoteView);
        controller.drawedCards(drawedCards);
        RemoteView remoteView2 = new RemoteView(players[1], players[0].getPlayerName(), new ClientConnection () {
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
        PickedCard pickedCard = new PickedCard(players[1],remoteView2,1);
        controller.pickACard(pickedCard);

        PlayerWorker playerWorker = new PlayerWorker(players[0],0,0,remoteView);
        controller.setPlayerWorker(playerWorker);
        PlayerWorker playerWorker2 = new PlayerWorker(players[0],1,0,remoteView);
        controller.setPlayerWorker(playerWorker2);

        PlayerWorker playerWorker3 = new PlayerWorker(players[1],0,1,remoteView2);
        controller.setPlayerWorker(playerWorker3);
        PlayerWorker playerWorker4 = new PlayerWorker(players[1],1,1,remoteView2);
        controller.setPlayerWorker(playerWorker4);

        assertTrue(controller.checkPhase());
        PlayerMove playerMove = new PlayerMove(players[0],0,0,1,remoteView);
        controller.move(playerMove);
        PlayerBuild playerBuild = new PlayerBuild(players[0],players[0].getUsedWorker(),0,2,remoteView);
        controller.increaseLevel(playerBuild);

        PlayerMove playerMove2 = new PlayerMove(players[1],1,2,1,remoteView2);
        controller.move(playerMove2);
        assertTrue(controller.checkPhase());
        PlayerBuild playerBuild2 = new PlayerBuild(players[1],players[1].getUsedWorker(),3,1,remoteView2);
        controller.increaseLevel(playerBuild2);
    }



    @Test
    public void drawedCards2PlayersTest(){
        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        Model model = new Model(players,false);
        GodCardController controller = new GodCardController(model);
        RemoteView remoteView = new RemoteView(players[0], players[1].getPlayerName(), new ClientConnection () {
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
        DrawedCards drawedCards = new DrawedCards(players[0],0,1,remoteView);
        controller.drawedCards(drawedCards);
        Phase phase = Phase.PICK_CARD;
        assertEquals(controller.getModel().getPhase(),phase);
        GodCard godCard = new Apollo();
        assertEquals(controller.getModel().getGods().get(0),godCard);
        GodCard godCard2 = new Arthemis();
        assertEquals(controller.getModel().getGods().get(1),godCard2);
    }

    @Test
    public void drawedCards3PlayersTest(){
        Player[] players = new Player[3];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        players[2] = new Player("Toad");
        Model model = new Model(players,false);
        GodCardController controller = new GodCardController(model);
        RemoteView remoteView = new RemoteView(players[0], players[1].getPlayerName(), players[2].getPlayerName(), new ClientConnection () {
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
        DrawedCards drawedCards = new DrawedCards(players[0],0,1,2,remoteView);
        controller.drawedCards(drawedCards);
        Phase phase = Phase.PICK_CARD;
        assertEquals(controller.getModel().getPhase(),phase);
        GodCard godCard = new Apollo();
        assertEquals(controller.getModel().getGods().get(0),godCard);
        GodCard godCard2 = new Arthemis();
        assertEquals(controller.getModel().getGods().get(1),godCard2);
        GodCard godCard3 = new Athena();
        assertEquals(controller.getModel().getGods().get(2),godCard3);
    }

    @Test
    public void pickACard2PlayersTest(){
        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        Model model = new Model(players,false);
        GodCardController controller = new GodCardController(model);
        RemoteView remoteView = new RemoteView(players[0], players[1].getPlayerName(), new ClientConnection () {
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
        DrawedCards drawedCards = new DrawedCards(players[0],0,1,remoteView);
        controller.drawedCards(drawedCards);
        RemoteView remoteView2 = new RemoteView(players[1], players[0].getPlayerName(), new ClientConnection () {
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
        PickedCard pickedCard = new PickedCard(players[1],remoteView2,0);

        controller.pickACard(pickedCard);
        GodCard godCard = new Apollo();
        GodCard godCard1 = new Arthemis();
        assertEquals(model.getPlayer(1).getGodCard(),godCard);

        PickedCard pickedCard1 = new PickedCard(players[0],remoteView,1);
        controller.pickACard(pickedCard1);
        assertEquals(model.getPlayer(0).getGodCard(),godCard1);
    }

    @Test
    public void pickACard3PlayersTest(){
        Player[] players = new Player[3];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        players[2] = new Player("Toad");
        Model model = new Model(players,false);
        GodCardController controller = new GodCardController(model);
        RemoteView remoteView = new RemoteView(players[0], players[1].getPlayerName(), players[2].getPlayerName(), new ClientConnection () {
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
        DrawedCards drawedCards = new DrawedCards(players[0],0,1,2,remoteView);
        controller.drawedCards(drawedCards);
        RemoteView remoteView2 = new RemoteView(players[1], players[0].getPlayerName(), players[2].getPlayerName(), new ClientConnection () {
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
        RemoteView remoteView3 = new RemoteView(players[2], players[0].getPlayerName(), players[1].getPlayerName(), new ClientConnection() {
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
        PickedCard pickedCard = new PickedCard(players[1],remoteView2,2);
        PickedCard pickedCard1 = new PickedCard(players[2],remoteView3,1);
        controller.pickACard(pickedCard);
        controller.pickACard(pickedCard1);
        GodCard godCard = new Apollo();
        GodCard godCard1 = new Arthemis();
        GodCard godCard2 = new Athena();
        assertEquals(model.getPlayer(1).getGodCard(),godCard2);
        assertEquals(model.getPlayer(2).getGodCard(),godCard1);
        PickedCard pickedCard2 = new PickedCard(players[0],remoteView,0);
        controller.pickACard(pickedCard2);
        assertEquals(model.getPlayer(0).getGodCard(),godCard);
    }

    @Test
    public void useApolloPowerTest(){
        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        Model model = new Model(players,false);
        GodCardController controller = new GodCardController(model);
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
        DrawedCards drawedCards = new DrawedCards(players[0],0,1,remoteView);
        controller.drawedCards(drawedCards);
        RemoteView remoteView2 = new RemoteView(players[1], players[0].getPlayerName(), new ClientConnection () {
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
        PickedCard pickedCard = new PickedCard(players[1],remoteView2,1);
        controller.pickACard(pickedCard);
        assertEquals(model.getPlayer(0).getGodCard().getCardGod(),SimpleGods.APOLLO);

        PlayerWorker playerWorker = new PlayerWorker(players[0],0,0,remoteView);
        controller.setPlayerWorker(playerWorker);
        PlayerWorker playerWorker2 = new PlayerWorker(players[0],1,0,remoteView);
        controller.setPlayerWorker(playerWorker2);

        PlayerWorker playerWorker3 = new PlayerWorker(players[1],0,1,remoteView2);
        controller.setPlayerWorker(playerWorker3);
        PlayerWorker playerWorker4 = new PlayerWorker(players[1],1,1,remoteView2);
        controller.setPlayerWorker(playerWorker4);

        PlayerMove playerMove = new PlayerMove(players[0],0,0,1,remoteView);
        controller.move(playerMove);
        PlayerBuild playerBuild = new PlayerBuild(players[0],players[0].getUsedWorker(),0,2,remoteView);
        controller.increaseLevel(playerBuild);

        assertEquals(model.getPlayer(0).getWorker(0).getCell(),model.getBoard().getCell(0,1));

        PlayerMove playerMove2 = new PlayerMove(players[1],1,2,1,remoteView2);
        controller.move(playerMove2);
        PlayerBuild playerBuild2 = new PlayerBuild(players[1],players[1].getUsedWorker(),3,1,remoteView2);
        controller.increaseLevel(playerBuild2);

        PlayerMove playerMove3 = new PlayerMove(players[0],1,0,0,remoteView);
        controller.move(playerMove3);

        assertEquals(model.getPlayer(0).getWorker(1).getCell(),model.getBoard().getCell(0,0));
    }

    @Test
    public void usePanPowerTest(){
        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        Model model = new Model(players,false);
        GodCardController controller = new GodCardController(model);
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
        DrawedCards drawedCards = new DrawedCards(players[0],7,0,remoteView);
        controller.drawedCards(drawedCards);
        RemoteView remoteView2 = new RemoteView(players[1], players[0].getPlayerName(), new ClientConnection () {
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
        PickedCard pickedCard = new PickedCard(players[1],remoteView2,1);
        controller.pickACard(pickedCard);
        assertEquals(model.getPlayer(0).getGodCard().getCardGod(),SimpleGods.PAN);

        PlayerWorker playerWorker = new PlayerWorker(players[0],0,0,remoteView);
        controller.setPlayerWorker(playerWorker);
        PlayerWorker playerWorker2 = new PlayerWorker(players[0],1,1,remoteView);
        controller.setPlayerWorker(playerWorker2);

        PlayerWorker playerWorker3 = new PlayerWorker(players[1],2,2,remoteView2);
        controller.setPlayerWorker(playerWorker3);
        PlayerWorker playerWorker4 = new PlayerWorker(players[1],3,2,remoteView2);
        controller.setPlayerWorker(playerWorker4);

        PlayerMove playerMove = new PlayerMove(players[0],1,0,1,remoteView);
        controller.move(playerMove);
        PlayerBuild playerBuild = new PlayerBuild(players[0],players[0].getUsedWorker(),1,0,remoteView);
        controller.increaseLevel(playerBuild);

        PlayerMove playerMove2 = new PlayerMove(players[1],0,2,1,remoteView2);
        controller.move(playerMove2);
        PlayerBuild playerBuild2 = new PlayerBuild(players[1],players[1].getUsedWorker(),2,0,remoteView2);
        controller.increaseLevel(playerBuild2);

        PlayerMove playerMove3 = new PlayerMove(players[0],0,1,0,remoteView);
        controller.move(playerMove3);
        PlayerBuild playerBuild3 = new PlayerBuild(players[0],players[0].getUsedWorker(),2,0,remoteView);
        controller.increaseLevel(playerBuild3);

        PlayerMove playerMove4 = new PlayerMove(players[1],1,3,3,remoteView2);
        controller.move(playerMove4);
        PlayerBuild playerBuild4 = new PlayerBuild(players[1],players[1].getUsedWorker(),4,3,remoteView2);
        controller.increaseLevel(playerBuild4);

        PlayerMove playerMove5 = new PlayerMove(players[0],0,2,0,remoteView);
        controller.move(playerMove5);
        PlayerBuild playerBuild5 = new PlayerBuild(players[0],players[0].getUsedWorker(),3,1,remoteView);
        controller.increaseLevel(playerBuild5);

        PlayerMove playerMove6 = new PlayerMove(players[1],1,4,3,remoteView2);
        controller.move(playerMove6);
        PlayerBuild playerBuild6 = new PlayerBuild(players[1],players[1].getUsedWorker(),4,4,remoteView2);
        controller.increaseLevel(playerBuild6);

        PlayerMove playerMove7 = new PlayerMove(players[0],0,3,0,remoteView);
        controller.move(playerMove7);

        assertTrue(model.getPlayer(0).hasWon());
    }

    @Test
    public void useAthenaPowerTest(){
        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        Model model = new Model(players,false);
        GodCardController controller = new GodCardController(model);
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
        DrawedCards drawedCards = new DrawedCards(players[0],0,2,remoteView);
        controller.drawedCards(drawedCards);
        RemoteView remoteView2 = new RemoteView(players[1], players[0].getPlayerName(), new ClientConnection () {
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
        PickedCard pickedCard = new PickedCard(players[1],remoteView2,1);
        controller.pickACard(pickedCard);

        PlayerWorker playerWorker = new PlayerWorker(players[0],0,0,remoteView);
        controller.setPlayerWorker(playerWorker);
        PlayerWorker playerWorker2 = new PlayerWorker(players[0],1,1,remoteView);
        controller.setPlayerWorker(playerWorker2);

        PlayerWorker playerWorker3 = new PlayerWorker(players[1],2,0,remoteView2);
        controller.setPlayerWorker(playerWorker3);
        PlayerWorker playerWorker4 = new PlayerWorker(players[1],3,2,remoteView2);
        controller.setPlayerWorker(playerWorker4);

        PlayerMove playerMove = new PlayerMove(players[0],0,0,1,remoteView);
        controller.move(playerMove);
        PlayerBuild playerBuild = new PlayerBuild(players[0],players[0].getUsedWorker(),1,0,remoteView);
        controller.increaseLevel(playerBuild);

        PlayerMove playerMove2 = new PlayerMove(players[1],0,1,0,remoteView2);
        controller.move(playerMove2);
        PlayerBuild playerBuild2 = new PlayerBuild(players[1],players[1].getUsedWorker(),2,0,remoteView2);
        controller.increaseLevel(playerBuild2);


        assertTrue(model.isMovedUp());
    }

    @Test
    public void buildADomeTest(){
        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        Model model = new Model(players,false);
        GodCardController controller = new GodCardController(model);
        RemoteView remoteView = new RemoteView(players[0], players[1].getPlayerName(), new ClientConnection () {
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

        DrawedCards drawedCards = new DrawedCards(players[0],7,0,remoteView);
        controller.drawedCards(drawedCards);

        RemoteView remoteView1 = new RemoteView(players[1], players[0].getPlayerName(), new ClientConnection() {
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

        PickedCard pickedCard = new PickedCard(players[1],remoteView1,1);
        controller.pickACard(pickedCard);

        PlayerWorker playerWorker = new PlayerWorker(players[0],0,0,remoteView);
        controller.setPlayerWorker(playerWorker);
        PlayerWorker playerWorker2 = new PlayerWorker(players[0],1,1,remoteView);
        controller.setPlayerWorker(playerWorker2);

        PlayerWorker playerWorker3 = new PlayerWorker(players[1],2,2,remoteView1);
        controller.setPlayerWorker(playerWorker3);
        PlayerWorker playerWorker4 = new PlayerWorker(players[1],3,0,remoteView1);
        controller.setPlayerWorker(playerWorker4);

        PlayerMove playerMoveWorker1_s = new PlayerMove(players[0],1,0,1,remoteView);
        controller.move(playerMoveWorker1_s);

        PlayerBuild playerBuildWorker1_d = new PlayerBuild(players[0],players[0].getUsedWorker(),1,0,remoteView);
        controller.increaseLevel(playerBuildWorker1_d);

        PlayerMove playerMove2Worker0_a = new PlayerMove(players[1],0,2,1,remoteView1);
        controller.move(playerMove2Worker0_a);

        PlayerBuild playerBuild2Worker0_a = new PlayerBuild(players[1],players[1].getUsedWorker(),1,0,remoteView1);
        controller.increaseLevel(playerBuild2Worker0_a);

        PlayerMove playerMoveWorker1_d = new PlayerMove(players[0],1,1,1,remoteView);
        controller.move(playerMoveWorker1_d);

        PlayerBuild playerBuildWorker1_a = new PlayerBuild(players[0],players[0].getUsedWorker(),1,0,remoteView);
        controller.increaseLevel(playerBuildWorker1_a);

        PlayerMove playerMove2Worker1_s = new PlayerMove(players[1],1,2,0,remoteView1);
        controller.move(playerMove2Worker1_s);

        PlayerBuild playerBuild2Worker1_s = new PlayerBuild(players[1],players[1].getUsedWorker(),1,0,remoteView1);
        controller.increaseLevel(playerBuild2Worker1_s);

        assertEquals(4, model.getBoard().getCell(1, 0).getLevel().getBlockId());
    }
}
