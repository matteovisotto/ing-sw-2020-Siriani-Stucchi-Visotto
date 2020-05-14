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
        /*Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        Model model = new Model(players,false);
        GodCardController controller = new GodCardController(model);*/
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
}
