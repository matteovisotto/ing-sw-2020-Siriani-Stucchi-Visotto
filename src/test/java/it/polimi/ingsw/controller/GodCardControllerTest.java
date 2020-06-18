package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.messageModel.*;
import it.polimi.ingsw.model.simplegod.*;
import it.polimi.ingsw.server.ClientConnection;
import it.polimi.ingsw.server.Lobby;
import it.polimi.ingsw.view.RemoteView;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;


import static org.junit.Assert.*;
public class GodCardControllerTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void drawedCards2PlayersTest() {
        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        Model model = new Model(players, false);
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
        }, null);
        DrawedCards drawedCards = new DrawedCards(players[0], 0, 1, remoteView);
        controller.drawedCards(drawedCards);
        Phase phase = Phase.PICK_CARD;
        assertEquals(controller.getModel().getPhase(), phase);
        GodCard godCard = new Apollo();
        assertEquals(controller.getModel().getGods().get(0), godCard);
        GodCard godCard2 = new Artemis();
        assertEquals(controller.getModel().getGods().get(1), godCard2);
    }

    @Test
    public void drawedCards3PlayersTest() {
        Player[] players = new Player[3];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        players[2] = new Player("Toad");
        Model model = new Model(players, false);
        GodCardController controller = new GodCardController(model);
        RemoteView remoteView = new RemoteView(players[0], players[1].getPlayerName(), players[2].getPlayerName(), new ClientConnection() {
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
        DrawedCards drawedCards = new DrawedCards(players[0], 0, 1, 2, remoteView);
        controller.drawedCards(drawedCards);
        Phase phase = Phase.PICK_CARD;
        assertEquals(controller.getModel().getPhase(), phase);
        GodCard godCard = new Apollo();
        assertEquals(controller.getModel().getGods().get(0), godCard);
        GodCard godCard2 = new Artemis();
        assertEquals(controller.getModel().getGods().get(1), godCard2);
        GodCard godCard3 = new Athena();
        assertEquals(controller.getModel().getGods().get(2), godCard3);
    }

    @Test
    public void pickACard2PlayersTest() {
        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        Model model = new Model(players, false);
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
        }, null);
        DrawedCards drawedCards = new DrawedCards(players[0], 0, 1, remoteView);
        controller.drawedCards(drawedCards);
        RemoteView remoteView2 = new RemoteView(players[1], players[0].getPlayerName(), new ClientConnection() {
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
        PickedCard pickedCard = new PickedCard(players[1], remoteView2, 0);

        controller.pickACard(pickedCard);
        GodCard godCard = new Apollo();
        GodCard godCard1 = new Artemis();
        assertEquals(model.getPlayer(1).getGodCard(), godCard);

        PickedCard pickedCard1 = new PickedCard(players[0], remoteView, 1);
        controller.pickACard(pickedCard1);
        assertEquals(model.getPlayer(0).getGodCard(), godCard1);
    }

    @Test
    public void pickACard3PlayersTest() {
        Player[] players = new Player[3];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        players[2] = new Player("Toad");
        Model model = new Model(players, false);
        GodCardController controller = new GodCardController(model);
        RemoteView remoteView = new RemoteView(players[0], players[1].getPlayerName(), players[2].getPlayerName(), new ClientConnection() {
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
        DrawedCards drawedCards = new DrawedCards(players[0], 0, 1, 2, remoteView);
        controller.drawedCards(drawedCards);
        RemoteView remoteView2 = new RemoteView(players[1], players[0].getPlayerName(), players[2].getPlayerName(), new ClientConnection() {
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
        }, null);
        PickedCard pickedCard = new PickedCard(players[1], remoteView2, 2);
        PickedCard pickedCard1 = new PickedCard(players[2], remoteView3, 1);
        controller.pickACard(pickedCard);
        controller.pickACard(pickedCard1);
        GodCard godCard = new Apollo();
        GodCard godCard1 = new Artemis();
        GodCard godCard2 = new Athena();
        assertEquals(model.getPlayer(1).getGodCard(), godCard2);
        assertEquals(model.getPlayer(2).getGodCard(), godCard1);
        PickedCard pickedCard2 = new PickedCard(players[0], remoteView, 0);
        controller.pickACard(pickedCard2);
        assertEquals(model.getPlayer(0).getGodCard(), godCard);
    }

    @Test
    public void buildADomeTest() {
        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        Model model = new Model(players, false);
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
        }, null);

        DrawedCards drawedCards = new DrawedCards(players[0], 7, 0, remoteView);
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
        }, null);

        PickedCard pickedCard = new PickedCard(players[1], remoteView1, 1);
        controller.pickACard(pickedCard);

        PlayerWorker playerWorker = new PlayerWorker(players[0], 0, 0, remoteView);
        controller.setPlayerWorker(playerWorker);
        PlayerWorker playerWorker2 = new PlayerWorker(players[0], 1, 1, remoteView);
        controller.setPlayerWorker(playerWorker2);

        PlayerWorker playerWorker3 = new PlayerWorker(players[1], 2, 2, remoteView1);
        controller.setPlayerWorker(playerWorker3);
        PlayerWorker playerWorker4 = new PlayerWorker(players[1], 3, 0, remoteView1);
        controller.setPlayerWorker(playerWorker4);

        PlayerMove playerMoveWorker1_s = new PlayerMove(players[0], 1, 0, 1, remoteView);
        controller.move(playerMoveWorker1_s);

        PlayerBuild playerBuildWorker1_d = new PlayerBuild(players[0], players[0].getUsedWorker(), 1, 0, remoteView);
        controller.build(playerBuildWorker1_d);

        PlayerMove playerMove2Worker0_a = new PlayerMove(players[1], 0, 2, 1, remoteView1);
        controller.move(playerMove2Worker0_a);

        PlayerBuild playerBuild2Worker0_a = new PlayerBuild(players[1], players[1].getUsedWorker(), 1, 0, remoteView1);
        controller.build(playerBuild2Worker0_a);

        PlayerMove playerMoveWorker1_d = new PlayerMove(players[0], 1, 1, 1, remoteView);
        controller.move(playerMoveWorker1_d);

        PlayerBuild playerBuildWorker1_a = new PlayerBuild(players[0], players[0].getUsedWorker(), 1, 0, remoteView);
        controller.build(playerBuildWorker1_a);

        PlayerMove playerMove2Worker1_s = new PlayerMove(players[1], 1, 2, 0, remoteView1);
        controller.move(playerMove2Worker1_s);

        PlayerBuild playerBuild2Worker1_s = new PlayerBuild(players[1], players[1].getUsedWorker(), 1, 0, remoteView1);
        controller.build(playerBuild2Worker1_s);

        assertEquals(4, model.getBoard().getCell(1, 0).getLevel().getBlockId());
    }

    @Test
    public void useApolloPowerTest() {
        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        Model model = new Model(players, false);
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
        }, null);
        DrawedCards drawedCards = new DrawedCards(players[0], 0, 2, remoteView);
        controller.drawedCards(drawedCards);
        RemoteView remoteView2 = new RemoteView(players[1], players[0].getPlayerName(), new ClientConnection() {
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
        PickedCard pickedCard = new PickedCard(players[1], remoteView2, 1);
        controller.pickACard(pickedCard);
        assertEquals(model.getPlayer(0).getGodCard().getCardGod(), Gods.APOLLO);

        PlayerWorker playerWorker = new PlayerWorker(players[0], 0, 0, remoteView);
        controller.setPlayerWorker(playerWorker);
        PlayerWorker playerWorker2 = new PlayerWorker(players[0], 1, 0, remoteView);
        controller.setPlayerWorker(playerWorker2);

        PlayerWorker playerWorker3 = new PlayerWorker(players[1], 0, 1, remoteView2);
        controller.setPlayerWorker(playerWorker3);
        PlayerWorker playerWorker4 = new PlayerWorker(players[1], 1, 1, remoteView2);
        controller.setPlayerWorker(playerWorker4);

        PlayerMove playerMove = new PlayerMove(players[0], 0, 0, 1, remoteView);
        controller.move(playerMove);
        PlayerBuild playerBuild = new PlayerBuild(players[0], players[0].getUsedWorker(), 0, 2, remoteView);
        controller.build(playerBuild);

        assertEquals(model.getPlayer(0).getWorker(0).getCell(), model.getBoard().getCell(0, 1));

        PlayerMove playerMove2 = new PlayerMove(players[1], 1, 2, 1, remoteView2);
        controller.move(playerMove2);
        PlayerBuild playerBuild2 = new PlayerBuild(players[1], players[1].getUsedWorker(), 3, 1, remoteView2);
        controller.build(playerBuild2);

        PlayerMove playerMove3 = new PlayerMove(players[0], 1, 0, 0, remoteView);
        controller.move(playerMove3);

        assertEquals(model.getPlayer(0).getWorker(1).getCell(), model.getBoard().getCell(0, 0));
    }

    @Test
    public void useAthenaPowerTest() {
        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        Model model = new Model(players, false);
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
        }, null);
        DrawedCards drawedCards = new DrawedCards(players[0], 0, 2, remoteView);
        controller.drawedCards(drawedCards);
        RemoteView remoteView2 = new RemoteView(players[1], players[0].getPlayerName(), new ClientConnection() {
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
        PickedCard pickedCard = new PickedCard(players[1], remoteView2, 1);
        controller.pickACard(pickedCard);

        PlayerWorker playerWorker = new PlayerWorker(players[0], 0, 0, remoteView);
        controller.setPlayerWorker(playerWorker);
        PlayerWorker playerWorker2 = new PlayerWorker(players[0], 1, 1, remoteView);
        controller.setPlayerWorker(playerWorker2);

        PlayerWorker playerWorker3 = new PlayerWorker(players[1], 2, 0, remoteView2);
        controller.setPlayerWorker(playerWorker3);
        PlayerWorker playerWorker4 = new PlayerWorker(players[1], 3, 2, remoteView2);
        controller.setPlayerWorker(playerWorker4);

        PlayerMove playerMove = new PlayerMove(players[0], 0, 0, 1, remoteView);
        controller.move(playerMove);
        PlayerBuild playerBuild = new PlayerBuild(players[0], players[0].getUsedWorker(), 1, 0, remoteView);
        controller.build(playerBuild);

        PlayerMove playerMove2 = new PlayerMove(players[1], 0, 1, 0, remoteView2);
        controller.move(playerMove2);
        PlayerBuild playerBuild2 = new PlayerBuild(players[1], players[1].getUsedWorker(), 2, 0, remoteView2);
        controller.build(playerBuild2);


        assertTrue(model.isMovedUp());
    }

    @Test
    public void useArtemisPowerTest() {
        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        Model model = new Model(players, false);
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
        }, null);
        DrawedCards drawedCards = new DrawedCards(players[0], 0, 1, remoteView);
        controller.drawedCards(drawedCards);
        RemoteView remoteView2 = new RemoteView(players[1], players[0].getPlayerName(), new ClientConnection() {
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
        PickedCard pickedCard = new PickedCard(players[1], remoteView2, 0);
        controller.pickACard(pickedCard);

        PlayerWorker playerWorker = new PlayerWorker(players[0], 0, 0, remoteView);
        controller.setPlayerWorker(playerWorker);
        PlayerWorker playerWorker2 = new PlayerWorker(players[0], 1, 1, remoteView);
        controller.setPlayerWorker(playerWorker2);

        PlayerWorker playerWorker3 = new PlayerWorker(players[1], 2, 0, remoteView2);
        controller.setPlayerWorker(playerWorker3);
        PlayerWorker playerWorker4 = new PlayerWorker(players[1], 3, 2, remoteView2);
        controller.setPlayerWorker(playerWorker4);

        PlayerMove playerMove = new PlayerMove(players[0], 0, 0, 1, remoteView);
        controller.move(playerMove);
        assertEquals(model.getPhase(), Phase.WAIT_GOD_ANSWER);
        char ch = 'y';
        UseGodPower useGodPower = new UseGodPower(players[0], remoteView, ch);
        useGodPower.handler(controller);
        PlayerMove playerMove2 = new PlayerMove(players[0], 0, 0, 2, remoteView);
        controller.move(playerMove2);
        assertEquals(model.getBoard().getCell(0, 2), players[0].getWorker(0).getCell());
        PlayerBuild playerBuild = new PlayerBuild(players[0], players[0].getUsedWorker(), 0, 3, remoteView);
        controller.build(playerBuild);
        assertEquals(((Artemis) playerMove.getPlayer().getGodCard()).getFirstMove(), model.getBoard().getCell(0, 0));
    }

    @Test
    public void useAtlasPowerTest() {
        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        Model model = new Model(players, false);
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
        }, null);
        DrawedCards drawedCards = new DrawedCards(players[0], 0, 3, remoteView);
        controller.drawedCards(drawedCards);
        RemoteView remoteView2 = new RemoteView(players[1], players[0].getPlayerName(), new ClientConnection() {
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
        PickedCard pickedCard = new PickedCard(players[1], remoteView2, 0);
        controller.pickACard(pickedCard);

        PlayerWorker playerWorker = new PlayerWorker(players[0], 0, 0, remoteView);
        controller.setPlayerWorker(playerWorker);
        PlayerWorker playerWorker2 = new PlayerWorker(players[0], 1, 1, remoteView);
        controller.setPlayerWorker(playerWorker2);

        PlayerWorker playerWorker3 = new PlayerWorker(players[1], 2, 0, remoteView2);
        controller.setPlayerWorker(playerWorker3);
        PlayerWorker playerWorker4 = new PlayerWorker(players[1], 3, 2, remoteView2);
        controller.setPlayerWorker(playerWorker4);

        PlayerMove playerMove = new PlayerMove(players[0], 0, 0, 1, remoteView);
        controller.move(playerMove);
        assertEquals(model.getPhase(), Phase.WAIT_GOD_ANSWER);
        char ch = 'y';
        UseGodPower useGodPower = new UseGodPower(players[0], remoteView, ch);
        useGodPower.handler(controller);
        PlayerBuild playerBuild = new PlayerBuild(players[0], players[0].getUsedWorker(), 1, 0, remoteView);
        controller.build(playerBuild);
        assertEquals(model.getBoard().getCell(1, 0).getLevel().getBlockId(), 4);
    }

    @Test
    public void useDemeterPowerTest() {
        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        Model model = new Model(players, false);
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
        }, null);
        DrawedCards drawedCards = new DrawedCards(players[0], 0, 4, remoteView);
        controller.drawedCards(drawedCards);
        RemoteView remoteView2 = new RemoteView(players[1], players[0].getPlayerName(), new ClientConnection() {
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
        PickedCard pickedCard = new PickedCard(players[1], remoteView2, 0);
        controller.pickACard(pickedCard);

        PlayerWorker playerWorker = new PlayerWorker(players[0], 0, 0, remoteView);
        controller.setPlayerWorker(playerWorker);
        PlayerWorker playerWorker2 = new PlayerWorker(players[0], 1, 1, remoteView);
        controller.setPlayerWorker(playerWorker2);

        PlayerWorker playerWorker3 = new PlayerWorker(players[1], 2, 0, remoteView2);
        controller.setPlayerWorker(playerWorker3);
        PlayerWorker playerWorker4 = new PlayerWorker(players[1], 3, 2, remoteView2);
        controller.setPlayerWorker(playerWorker4);

        PlayerMove playerMove = new PlayerMove(players[0], 0, 0, 1, remoteView);
        controller.move(playerMove);

        PlayerBuild playerBuild = new PlayerBuild(players[0], players[0].getUsedWorker(), 0, 2, remoteView);
        controller.build(playerBuild);
        assertEquals(model.getPhase(), Phase.WAIT_GOD_ANSWER);
        char ch = 'y';
        UseGodPower useGodPower = new UseGodPower(players[0], remoteView, ch);
        useGodPower.handler(controller);
        PlayerBuild playerBuild2 = new PlayerBuild(players[0], players[0].getUsedWorker(), 0, 0, remoteView);
        controller.build(playerBuild2);
        assertEquals(((Demeter) playerBuild.getPlayer().getGodCard()).getFirstBuild(), model.getBoard().getCell(0, 2));
        assertEquals(model.getBoard().getCell(0, 0).getLevel().getBlockId(), 1);
    }

    @Test
    public void useHephaestusPowerTest() {
        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        Model model = new Model(players, false);
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
        }, null);
        DrawedCards drawedCards = new DrawedCards(players[0], 0, 5, remoteView);
        controller.drawedCards(drawedCards);
        RemoteView remoteView2 = new RemoteView(players[1], players[0].getPlayerName(), new ClientConnection() {
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
        PickedCard pickedCard = new PickedCard(players[1], remoteView2, 0);
        controller.pickACard(pickedCard);

        PlayerWorker playerWorker = new PlayerWorker(players[0], 0, 0, remoteView);
        controller.setPlayerWorker(playerWorker);
        PlayerWorker playerWorker2 = new PlayerWorker(players[0], 1, 1, remoteView);
        controller.setPlayerWorker(playerWorker2);

        PlayerWorker playerWorker3 = new PlayerWorker(players[1], 2, 0, remoteView2);
        controller.setPlayerWorker(playerWorker3);
        PlayerWorker playerWorker4 = new PlayerWorker(players[1], 3, 2, remoteView2);
        controller.setPlayerWorker(playerWorker4);

        PlayerMove playerMove = new PlayerMove(players[0], 0, 0, 1, remoteView);
        controller.move(playerMove);

        PlayerBuild playerBuild = new PlayerBuild(players[0], players[0].getUsedWorker(), 0, 2, remoteView);
        controller.build(playerBuild);

        assertEquals(model.getPhase(), Phase.WAIT_GOD_ANSWER);
        char ch = 'y';
        UseGodPower useGodPower = new UseGodPower(players[0], remoteView, ch);
        useGodPower.handler(controller);

        assertEquals(((Hephaestus) playerBuild.getPlayer().getGodCard()).getFirstBuilt(), model.getBoard().getCell(0, 2));
        assertEquals(model.getBoard().getCell(0, 2).getLevel().getBlockId(), 2);
    }

    @Test
    public void useHephaestusPowerCannotBuildOverDomeTest() {
        //exception.expect(IllegalArgumentException.class);
        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        Model model = new Model(players, false);
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
        }, null);
        DrawedCards drawedCards = new DrawedCards(players[0], 0, 5, remoteView);
        controller.drawedCards(drawedCards);
        RemoteView remoteView2 = new RemoteView(players[1], players[0].getPlayerName(), new ClientConnection() {
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
        PickedCard pickedCard = new PickedCard(players[1], remoteView2, 0);
        controller.pickACard(pickedCard);

        PlayerWorker playerWorker = new PlayerWorker(players[0], 0, 0, remoteView);
        controller.setPlayerWorker(playerWorker);
        PlayerWorker playerWorker2 = new PlayerWorker(players[0], 4, 4, remoteView);
        controller.setPlayerWorker(playerWorker2);

        PlayerWorker playerWorker3 = new PlayerWorker(players[1], 2, 0, remoteView2);
        controller.setPlayerWorker(playerWorker3);
        PlayerWorker playerWorker4 = new PlayerWorker(players[1], 3, 2, remoteView2);
        controller.setPlayerWorker(playerWorker4);

        PlayerMove playerMove = new PlayerMove(players[0], 0, 1, 1, remoteView);
        controller.move(playerMove);

        PlayerBuild playerBuild2 = new PlayerBuild(players[0], players[0].getUsedWorker(), 1, 0, remoteView);
        controller.build(playerBuild2);

        assertEquals(model.getPhase(), Phase.WAIT_GOD_ANSWER);
        char ch = 'y';
        UseGodPower useGodPower = new UseGodPower(players[0], remoteView, ch);
        useGodPower.handler(controller);

        assertEquals(model.getBoard().getCell(1, 0).getLevel().getBlockId(), 2);

        PlayerMove playerMove3 = new PlayerMove(players[1], 0, 2, 0, remoteView2);
        controller.move(playerMove3);

        PlayerBuild playerBuild3 = new PlayerBuild(players[1], players[1].getUsedWorker(), 1, 0, remoteView2);
        controller.build(playerBuild3);

        PlayerMove playerMove4 = new PlayerMove(players[0], 0, 1, 1, remoteView);
        controller.move(playerMove4);

        PlayerBuild playerBuild4 = new PlayerBuild(players[0], players[0].getUsedWorker(), 1, 0, remoteView);
        controller.build(playerBuild4);

        assertEquals(model.getBoard().getCell(1, 0).getLevel().getBlockId(), 4);
    }

    @Test
    public void useMinotaurPowerTest() {
        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        Model model = new Model(players, false);
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
        }, null);
        DrawedCards drawedCards = new DrawedCards(players[0], 0, 6, remoteView);
        controller.drawedCards(drawedCards);
        RemoteView remoteView2 = new RemoteView(players[1], players[0].getPlayerName(), new ClientConnection() {
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
        PickedCard pickedCard = new PickedCard(players[1], remoteView2, 0);
        controller.pickACard(pickedCard);

        PlayerWorker playerWorker = new PlayerWorker(players[0], 0, 0, remoteView);
        controller.setPlayerWorker(playerWorker);
        PlayerWorker playerWorker2 = new PlayerWorker(players[0], 1, 1, remoteView);
        controller.setPlayerWorker(playerWorker2);

        PlayerWorker playerWorker3 = new PlayerWorker(players[1], 0, 1, remoteView2);
        controller.setPlayerWorker(playerWorker3);
        PlayerWorker playerWorker4 = new PlayerWorker(players[1], 1, 0, remoteView2);
        controller.setPlayerWorker(playerWorker4);

        PlayerMove playerMove = new PlayerMove(players[0], 0, 0, 1, remoteView);
        controller.move(playerMove);

        assertEquals(model.getPlayer(0).getWorker(0).getCell(), model.getBoard().getCell(0, 1));
        assertEquals(model.getPlayer(1).getWorker(0).getCell(), model.getBoard().getCell(0, 2));
    }

    @Test
    public void usePanPowerTest() {
        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        Model model = new Model(players, false);
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
        }, null);
        DrawedCards drawedCards = new DrawedCards(players[0], 7, 0, remoteView);
        controller.drawedCards(drawedCards);
        RemoteView remoteView2 = new RemoteView(players[1], players[0].getPlayerName(), new ClientConnection() {
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
        PickedCard pickedCard = new PickedCard(players[1], remoteView2, 1);
        controller.pickACard(pickedCard);
        assertEquals(model.getPlayer(0).getGodCard().getCardGod(), Gods.PAN);

        PlayerWorker playerWorker = new PlayerWorker(players[0], 0, 0, remoteView);
        controller.setPlayerWorker(playerWorker);
        PlayerWorker playerWorker2 = new PlayerWorker(players[0], 1, 1, remoteView);
        controller.setPlayerWorker(playerWorker2);

        PlayerWorker playerWorker3 = new PlayerWorker(players[1], 2, 2, remoteView2);
        controller.setPlayerWorker(playerWorker3);
        PlayerWorker playerWorker4 = new PlayerWorker(players[1], 3, 2, remoteView2);
        controller.setPlayerWorker(playerWorker4);

        PlayerMove playerMove = new PlayerMove(players[0], 1, 0, 1, remoteView);
        controller.move(playerMove);
        PlayerBuild playerBuild = new PlayerBuild(players[0], players[0].getUsedWorker(), 1, 0, remoteView);
        controller.build(playerBuild);

        PlayerMove playerMove2 = new PlayerMove(players[1], 0, 2, 1, remoteView2);
        controller.move(playerMove2);
        PlayerBuild playerBuild2 = new PlayerBuild(players[1], players[1].getUsedWorker(), 2, 0, remoteView2);
        controller.build(playerBuild2);

        PlayerMove playerMove3 = new PlayerMove(players[0], 0, 1, 0, remoteView);
        controller.move(playerMove3);
        PlayerBuild playerBuild3 = new PlayerBuild(players[0], players[0].getUsedWorker(), 2, 0, remoteView);
        controller.build(playerBuild3);

        PlayerMove playerMove4 = new PlayerMove(players[1], 1, 3, 3, remoteView2);
        controller.move(playerMove4);
        PlayerBuild playerBuild4 = new PlayerBuild(players[1], players[1].getUsedWorker(), 4, 3, remoteView2);
        controller.build(playerBuild4);

        PlayerMove playerMove5 = new PlayerMove(players[0], 0, 2, 0, remoteView);
        controller.move(playerMove5);
        PlayerBuild playerBuild5 = new PlayerBuild(players[0], players[0].getUsedWorker(), 3, 1, remoteView);
        controller.build(playerBuild5);

        PlayerMove playerMove6 = new PlayerMove(players[1], 1, 4, 3, remoteView2);
        controller.move(playerMove6);
        PlayerBuild playerBuild6 = new PlayerBuild(players[1], players[1].getUsedWorker(), 4, 4, remoteView2);
        controller.build(playerBuild6);

        PlayerMove playerMove7 = new PlayerMove(players[0], 0, 3, 0, remoteView);
        controller.move(playerMove7);

        assertTrue(model.getPlayer(0).hasWon());
    }

    @Test
    public void usePrometheusPowerPlayer1Test() {
        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        Model model = new Model(players, false);
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
        }, null);
        DrawedCards drawedCards = new DrawedCards(players[0], 0, 8, remoteView);
        controller.drawedCards(drawedCards);
        RemoteView remoteView2 = new RemoteView(players[1], players[0].getPlayerName(), new ClientConnection() {
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
        PickedCard pickedCard = new PickedCard(players[1], remoteView2, 0);
        controller.pickACard(pickedCard);

        PlayerWorker playerWorker = new PlayerWorker(players[0], 0, 0, remoteView);
        controller.setPlayerWorker(playerWorker);
        PlayerWorker playerWorker2 = new PlayerWorker(players[0], 1, 1, remoteView);
        controller.setPlayerWorker(playerWorker2);

        PlayerWorker playerWorker3 = new PlayerWorker(players[1], 2, 0, remoteView2);
        controller.setPlayerWorker(playerWorker3);
        PlayerWorker playerWorker4 = new PlayerWorker(players[1], 3, 2, remoteView2);
        controller.setPlayerWorker(playerWorker4);

        assertEquals(model.getPhase(), Phase.WAIT_GOD_ANSWER);
        char ch = 'y';
        UseGodPower useGodPower = new UseGodPower(players[0], remoteView, ch);
        useGodPower.handler(controller);

        assertEquals(model.getPhase(), Phase.PROMETHEUS_WORKER);
        SetPrometheus setPrometheus = new SetPrometheus(players[0], remoteView, 0);
        setPrometheus.handler(controller);

        assertEquals(model.getPhase(), Phase.BUILD);
        PlayerBuild playerBuild = new PlayerBuild(players[0], players[0].getUsedWorker(), 1, 0, remoteView);
        controller.build(playerBuild);

        PlayerMove playerMove = new PlayerMove(players[0], 0, 0, 1, remoteView);
        controller.move(playerMove);

        PlayerBuild playerBuild2 = new PlayerBuild(players[0], players[0].getUsedWorker(), 0, 2, remoteView);
        controller.build(playerBuild2);

        assertEquals(model.getBoard().getCell(1, 0).getLevel().getBlockId(), 1);
        assertEquals(model.getBoard().getCell(0, 2).getLevel().getBlockId(), 1);
    }

    @Test
    public void usePrometheusPowerPlayer2Test() {
        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        Model model = new Model(players, false);
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
        }, null);
        DrawedCards drawedCards = new DrawedCards(players[0], 0, 8, remoteView);
        controller.drawedCards(drawedCards);
        RemoteView remoteView2 = new RemoteView(players[1], players[0].getPlayerName(), new ClientConnection() {
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
        PickedCard pickedCard = new PickedCard(players[1], remoteView2, 1);
        controller.pickACard(pickedCard);

        PlayerWorker playerWorker = new PlayerWorker(players[0], 2, 0, remoteView);
        controller.setPlayerWorker(playerWorker);
        PlayerWorker playerWorker2 = new PlayerWorker(players[0], 3, 2, remoteView);
        controller.setPlayerWorker(playerWorker2);

        PlayerWorker playerWorker3 = new PlayerWorker(players[1], 0, 0, remoteView2);
        controller.setPlayerWorker(playerWorker3);
        PlayerWorker playerWorker4 = new PlayerWorker(players[1], 1, 1, remoteView2);
        controller.setPlayerWorker(playerWorker4);

        PlayerMove playerMove = new PlayerMove(players[0], 0, 2, 1, remoteView);
        controller.move(playerMove);

        PlayerBuild playerBuild2 = new PlayerBuild(players[0], players[0].getUsedWorker(), 2, 2, remoteView);
        controller.build(playerBuild2);

        assertEquals(model.getPhase(), Phase.WAIT_GOD_ANSWER);
        char ch = 'y';
        UseGodPower useGodPower = new UseGodPower(players[1], remoteView2, ch);
        useGodPower.handler(controller);

        assertEquals(model.getPhase(), Phase.PROMETHEUS_WORKER);
        SetPrometheus setPrometheus = new SetPrometheus(players[1], remoteView2, 0);
        setPrometheus.handler(controller);

        assertEquals(model.getPhase(), Phase.BUILD);
        PlayerBuild playerBuild = new PlayerBuild(players[1], players[1].getUsedWorker(), 1, 0, remoteView2);
        controller.build(playerBuild);

        PlayerMove playerMove3 = new PlayerMove(players[1], 0, 0, 1, remoteView2);
        controller.move(playerMove3);

        PlayerBuild playerBuild3 = new PlayerBuild(players[1], players[1].getUsedWorker(), 0, 2, remoteView2);
        controller.build(playerBuild3);

        assertEquals(model.getBoard().getCell(1, 0).getLevel().getBlockId(), 1);
        assertEquals(model.getBoard().getCell(0, 2).getLevel().getBlockId(), 1);
    }

    @Test
    public void prometheusNotUsePowerTest() {
        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        Model model = new Model(players, false);
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
        }, null);
        DrawedCards drawedCards = new DrawedCards(players[0], 0, 8, remoteView);
        controller.drawedCards(drawedCards);
        RemoteView remoteView2 = new RemoteView(players[1], players[0].getPlayerName(), new ClientConnection() {
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
        PickedCard pickedCard = new PickedCard(players[1], remoteView2, 0);
        controller.pickACard(pickedCard);

        PlayerWorker playerWorker = new PlayerWorker(players[0], 0, 0, remoteView);
        controller.setPlayerWorker(playerWorker);
        PlayerWorker playerWorker2 = new PlayerWorker(players[0], 1, 1, remoteView);
        controller.setPlayerWorker(playerWorker2);

        PlayerWorker playerWorker3 = new PlayerWorker(players[1], 2, 0, remoteView2);
        controller.setPlayerWorker(playerWorker3);
        PlayerWorker playerWorker4 = new PlayerWorker(players[1], 3, 2, remoteView2);
        controller.setPlayerWorker(playerWorker4);

        assertEquals(model.getPhase(), Phase.WAIT_GOD_ANSWER);
        char ch = 'n';
        UseGodPower useGodPower = new UseGodPower(players[0], remoteView, ch);
        useGodPower.handler(controller);

        assertEquals(model.getPhase(), Phase.MOVE);

        PlayerMove playerMove = new PlayerMove(players[0], 0, 0, 1, remoteView);
        controller.move(playerMove);

        PlayerBuild playerBuild2 = new PlayerBuild(players[0], players[0].getUsedWorker(), 0, 2, remoteView);
        controller.build(playerBuild2);

        assertEquals(model.getBoard().getCell(1, 0).getLevel().getBlockId(), 0);
        assertEquals(model.getBoard().getCell(0, 2).getLevel().getBlockId(), 1);
    }

    @Test
    public void useChronusPowerTest() {
        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        Model model = new Model(players, false);
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
        }, null);
        DrawedCards drawedCards = new DrawedCards(players[0], 9, 3, remoteView);
        controller.drawedCards(drawedCards);
        RemoteView remoteView2 = new RemoteView(players[1], players[0].getPlayerName(), new ClientConnection() {
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
        PickedCard pickedCard = new PickedCard(players[1], remoteView2, 0);
        controller.pickACard(pickedCard);

        PlayerWorker playerWorker = new PlayerWorker(players[0], 0, 0, remoteView);
        controller.setPlayerWorker(playerWorker);
        PlayerWorker playerWorker2 = new PlayerWorker(players[0], 1, 1, remoteView);
        controller.setPlayerWorker(playerWorker2);

        PlayerWorker playerWorker3 = new PlayerWorker(players[1], 2, 2, remoteView2);
        controller.setPlayerWorker(playerWorker3);
        PlayerWorker playerWorker4 = new PlayerWorker(players[1], 3, 3, remoteView2);
        controller.setPlayerWorker(playerWorker4);

        PlayerMove playerMove = new PlayerMove(players[0], 0, 0, 1, remoteView);
        controller.move(playerMove);
        assertEquals(model.getPhase(), Phase.WAIT_GOD_ANSWER);
        char ch = 'y';
        UseGodPower useGodPower = new UseGodPower(players[0], remoteView, ch);
        useGodPower.handler(controller);
        PlayerBuild playerBuild = new PlayerBuild(players[0], players[0].getUsedWorker(), 1, 0, remoteView);
        controller.build(playerBuild);
        assertEquals(model.getBoard().getCell(1, 0).getLevel().getBlockId(), 4);

        PlayerMove playerMove2 = new PlayerMove(players[1], 0, 2, 3, remoteView2);
        controller.move(playerMove2);
        PlayerBuild playerBuild2 = new PlayerBuild(players[1], players[1].getUsedWorker(), 2, 2, remoteView2);
        controller.build(playerBuild2);

        PlayerMove playerMove3 = new PlayerMove(players[0], 1, 2, 1, remoteView);
        controller.move(playerMove3);
        assertEquals(model.getPhase(), Phase.WAIT_GOD_ANSWER);
        UseGodPower useGodPower2 = new UseGodPower(players[0], remoteView, ch);
        useGodPower2.handler(controller);
        PlayerBuild playerBuild3 = new PlayerBuild(players[0], players[0].getUsedWorker(), 2, 0, remoteView);
        controller.build(playerBuild3);
        assertEquals(model.getBoard().getCell(2, 0).getLevel().getBlockId(), 4);

        PlayerMove playerMove4 = new PlayerMove(players[1], 1, 3, 2, remoteView2);
        controller.move(playerMove4);
        PlayerBuild playerBuild4 = new PlayerBuild(players[1], players[1].getUsedWorker(), 2, 2, remoteView2);
        controller.build(playerBuild4);

        PlayerMove playerMove5 = new PlayerMove(players[0], 0, 1, 1, remoteView);
        controller.move(playerMove5);
        assertEquals(model.getPhase(), Phase.WAIT_GOD_ANSWER);
        UseGodPower useGodPower3 = new UseGodPower(players[0], remoteView, ch);
        useGodPower3.handler(controller);
        PlayerBuild playerBuild5 = new PlayerBuild(players[0], players[0].getUsedWorker(), 0, 0, remoteView);
        controller.build(playerBuild5);
        assertEquals(model.getBoard().getCell(0, 0).getLevel().getBlockId(), 4);

        PlayerMove playerMove6 = new PlayerMove(players[1], 1, 3, 3, remoteView2);
        controller.move(playerMove6);
        PlayerBuild playerBuild6 = new PlayerBuild(players[1], players[1].getUsedWorker(), 2, 2, remoteView2);
        controller.build(playerBuild6);

        PlayerMove playerMove7 = new PlayerMove(players[0], 0, 1, 2, remoteView);
        controller.move(playerMove7);
        assertEquals(model.getPhase(), Phase.WAIT_GOD_ANSWER);
        UseGodPower useGodPower4 = new UseGodPower(players[0], remoteView, ch);
        useGodPower4.handler(controller);
        PlayerBuild playerBuild7 = new PlayerBuild(players[0], players[0].getUsedWorker(), 1, 3, remoteView);
        controller.build(playerBuild7);
        assertEquals(model.getBoard().getCell(1, 3).getLevel().getBlockId(), 4);

        PlayerMove playerMove8 = new PlayerMove(players[1], 1, 2, 3, remoteView2);
        controller.move(playerMove8);
        PlayerBuild playerBuild8 = new PlayerBuild(players[1], players[1].getUsedWorker(), 2, 2, remoteView2);
        controller.build(playerBuild8);
        assertEquals(model.getBoard().getCell(2, 2).getLevel().getBlockId(), 4);
        //assertTrue(players[1].hasWon());
    }

    @Test
    public void useHestiaPowerTest() {
        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        Model model = new Model(players, false);
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
        }, null);
        DrawedCards drawedCards = new DrawedCards(players[0], 0, 10, remoteView);
        controller.drawedCards(drawedCards);
        RemoteView remoteView2 = new RemoteView(players[1], players[0].getPlayerName(), new ClientConnection() {
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
        PickedCard pickedCard = new PickedCard(players[1], remoteView2, 0);
        controller.pickACard(pickedCard);

        PlayerWorker playerWorker = new PlayerWorker(players[0], 0, 0, remoteView);
        controller.setPlayerWorker(playerWorker);
        PlayerWorker playerWorker2 = new PlayerWorker(players[0], 1, 1, remoteView);
        controller.setPlayerWorker(playerWorker2);

        PlayerWorker playerWorker3 = new PlayerWorker(players[1], 2, 0, remoteView2);
        controller.setPlayerWorker(playerWorker3);
        PlayerWorker playerWorker4 = new PlayerWorker(players[1], 3, 2, remoteView2);
        controller.setPlayerWorker(playerWorker4);

        PlayerMove playerMove = new PlayerMove(players[0], 0, 0, 1, remoteView);
        controller.move(playerMove);

        PlayerBuild playerBuild = new PlayerBuild(players[0], players[0].getUsedWorker(), 0, 2, remoteView);
        controller.build(playerBuild);
        assertEquals(model.getPhase(), Phase.WAIT_GOD_ANSWER);
        char ch = 'y';
        UseGodPower useGodPower = new UseGodPower(players[0], remoteView, ch);
        useGodPower.handler(controller);
        PlayerBuild playerBuild2 = new PlayerBuild(players[0], players[0].getUsedWorker(), 1, 2, remoteView);
        controller.build(playerBuild2);
        assertEquals(model.getBoard().getCell(1, 2).getLevel().getBlockId(), 1);
    }

    @Test
    public void usePoseidonPowerTest() {
        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        Model model = new Model(players, false);
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
        }, null);
        DrawedCards drawedCards = new DrawedCards(players[0], 0, 11, remoteView);
        controller.drawedCards(drawedCards);
        RemoteView remoteView2 = new RemoteView(players[1], players[0].getPlayerName(), new ClientConnection() {
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
        PickedCard pickedCard = new PickedCard(players[1], remoteView2, 0);
        controller.pickACard(pickedCard);

        PlayerWorker playerWorker = new PlayerWorker(players[0], 0, 0, remoteView);
        controller.setPlayerWorker(playerWorker);
        PlayerWorker playerWorker2 = new PlayerWorker(players[0], 3, 0, remoteView);
        controller.setPlayerWorker(playerWorker2);

        PlayerWorker playerWorker3 = new PlayerWorker(players[1], 2, 2, remoteView2);
        controller.setPlayerWorker(playerWorker3);
        PlayerWorker playerWorker4 = new PlayerWorker(players[1], 3, 2, remoteView2);
        controller.setPlayerWorker(playerWorker4);

        PlayerMove playerMove = new PlayerMove(players[0], 0, 0, 1, remoteView);
        controller.move(playerMove);

        PlayerBuild playerBuild = new PlayerBuild(players[0], players[0].getUsedWorker(), 0, 2, remoteView);
        controller.build(playerBuild);
        assertEquals(model.getPhase(), Phase.WAIT_GOD_ANSWER);
        char ch = 'y';
        UseGodPower useGodPower = new UseGodPower(players[0], remoteView, ch);
        useGodPower.handler(controller);
        PlayerBuild playerBuild2 = new PlayerBuild(players[0], 1, 4, 0, remoteView);
        controller.build(playerBuild2);
        assertEquals(model.getBoard().getCell(4, 0).getLevel().getBlockId(), 1);

        assertEquals(model.getPhase(), Phase.WAIT_GOD_ANSWER);
        UseGodPower useGodPower2 = new UseGodPower(players[0], remoteView, ch);
        useGodPower2.handler(controller);
        PlayerBuild playerBuild3 = new PlayerBuild(players[0], 1, 3, 1, remoteView);
        controller.build(playerBuild3);
        assertEquals(model.getBoard().getCell(3, 1).getLevel().getBlockId(), 1);

        assertEquals(model.getPhase(), Phase.WAIT_GOD_ANSWER);
        UseGodPower useGodPower3 = new UseGodPower(players[0], remoteView, ch);
        useGodPower3.handler(controller);
        PlayerBuild playerBuild4 = new PlayerBuild(players[0], 1, 2, 0, remoteView);
        controller.build(playerBuild4);
        assertEquals(model.getBoard().getCell(2, 0).getLevel().getBlockId(), 1);

        assertEquals(model.getPhase(), Phase.MOVE);
    }

    @Test
    public void useZeusPowerTest() {
        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        Model model = new Model(players, false);
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
        }, null);
        DrawedCards drawedCards = new DrawedCards(players[0], 0, 13, remoteView);
        controller.drawedCards(drawedCards);
        RemoteView remoteView2 = new RemoteView(players[1], players[0].getPlayerName(), new ClientConnection() {
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
        PickedCard pickedCard = new PickedCard(players[1], remoteView2, 0);
        controller.pickACard(pickedCard);

        PlayerWorker playerWorker = new PlayerWorker(players[0], 0, 0, remoteView);
        controller.setPlayerWorker(playerWorker);
        PlayerWorker playerWorker2 = new PlayerWorker(players[0], 1, 1, remoteView);
        controller.setPlayerWorker(playerWorker2);

        PlayerWorker playerWorker3 = new PlayerWorker(players[1], 2, 0, remoteView2);
        controller.setPlayerWorker(playerWorker3);
        PlayerWorker playerWorker4 = new PlayerWorker(players[1], 3, 2, remoteView2);
        controller.setPlayerWorker(playerWorker4);

        PlayerMove playerMove = new PlayerMove(players[0], 0, 0, 1, remoteView);
        controller.move(playerMove);

        PlayerBuild playerBuild = new PlayerBuild(players[0], players[0].getUsedWorker(), 0, 1, remoteView);
        controller.build(playerBuild);
        assertEquals(model.getBoard().getCell(0, 1).getLevel().getBlockId(), 1);
    }

    @Test
    public void useTritonPowerTest() {
        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        Model model = new Model(players, false);
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
        }, null);
        DrawedCards drawedCards = new DrawedCards(players[0], 0, 12, remoteView);
        controller.drawedCards(drawedCards);
        RemoteView remoteView2 = new RemoteView(players[1], players[0].getPlayerName(), new ClientConnection() {
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
        PickedCard pickedCard = new PickedCard(players[1], remoteView2, 0);
        controller.pickACard(pickedCard);

        PlayerWorker playerWorker = new PlayerWorker(players[0], 0, 0, remoteView);
        controller.setPlayerWorker(playerWorker);
        PlayerWorker playerWorker2 = new PlayerWorker(players[0], 1, 1, remoteView);
        controller.setPlayerWorker(playerWorker2);

        PlayerWorker playerWorker3 = new PlayerWorker(players[1], 2, 2, remoteView2);
        controller.setPlayerWorker(playerWorker3);
        PlayerWorker playerWorker4 = new PlayerWorker(players[1], 3, 2, remoteView2);
        controller.setPlayerWorker(playerWorker4);

        PlayerMove playerMove = new PlayerMove(players[0], 0, 0, 1, remoteView);
        controller.move(playerMove);
        assertEquals(model.getPhase(), Phase.WAIT_GOD_ANSWER);
        char ch = 'y';
        UseGodPower useGodPower = new UseGodPower(players[0], remoteView, ch);
        useGodPower.handler(controller);
        PlayerMove playerMove2 = new PlayerMove(players[0], 0, 0, 2, remoteView);
        controller.move(playerMove2);
        useGodPower.handler(controller);
        PlayerMove playerMove3 = new PlayerMove(players[0], 0, 0, 3, remoteView);
        controller.move(playerMove3);
        useGodPower.handler(controller);
        PlayerMove playerMove4 = new PlayerMove(players[0], 0, 0, 4, remoteView);
        controller.move(playerMove4);
        useGodPower.handler(controller);

        PlayerMove playerMove5 = new PlayerMove(players[0], 0, 1, 4, remoteView);
        controller.move(playerMove5);
        useGodPower.handler(controller);
        PlayerMove playerMove6 = new PlayerMove(players[0], 0, 2, 4, remoteView);
        controller.move(playerMove6);
        useGodPower.handler(controller);
        PlayerMove playerMove7 = new PlayerMove(players[0], 0, 3, 4, remoteView);
        controller.move(playerMove7);
        useGodPower.handler(controller);
        PlayerMove playerMove8 = new PlayerMove(players[0], 0, 4, 4, remoteView);
        controller.move(playerMove8);
        useGodPower.handler(controller);

        PlayerMove playerMove9 = new PlayerMove(players[0], 0, 4, 3, remoteView);
        controller.move(playerMove9);
        useGodPower.handler(controller);
        PlayerMove playerMove10 = new PlayerMove(players[0], 0, 4, 2, remoteView);
        controller.move(playerMove10);
        useGodPower.handler(controller);
        PlayerMove playerMove11 = new PlayerMove(players[0], 0, 4, 1, remoteView);
        controller.move(playerMove11);
        useGodPower.handler(controller);
        PlayerMove playerMove12 = new PlayerMove(players[0], 0, 4, 0, remoteView);
        controller.move(playerMove12);
        useGodPower.handler(controller);

        PlayerMove playerMove13 = new PlayerMove(players[0], 0, 3, 0, remoteView);
        controller.move(playerMove13);
        useGodPower.handler(controller);
        PlayerMove playerMove14 = new PlayerMove(players[0], 0, 2, 0, remoteView);
        controller.move(playerMove14);
        useGodPower.handler(controller);
        PlayerMove playerMove15 = new PlayerMove(players[0], 0, 1, 0, remoteView);
        controller.move(playerMove15);
        useGodPower.handler(controller);
        PlayerMove playerMove16 = new PlayerMove(players[0], 0, 0, 0, remoteView);
        controller.move(playerMove16);
        assertEquals(players[0].getWorker(0).getCell().getX(), 0);
        assertEquals(players[0].getWorker(0).getCell().getY(), 0);
    }

    @Test
    public void newGameTest() {
        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        Model model = new Model(players, false);
        GodCardController controller = new GodCardController(model);
        ClientConnection clientConnection = new ClientConnection() {
            @Override
            public void closeConnection() {

            }

            @Override
            public void send(Object message) {

            }

            @Override
            public void asyncSend(Object message) {

            }
        };
        Lobby lobby = new Lobby("ciao", players[0].getPlayerName(), clientConnection, 2, false);
        RemoteView remoteView = new RemoteView(players[0], players[1].getPlayerName(), clientConnection, lobby);

        ClientConnection clientConnection2 = new ClientConnection() {
            @Override
            public void closeConnection() {

            }

            @Override
            public void send(Object message) {

            }

            @Override
            public void asyncSend(Object message) {

            }
        };
        lobby.addPlayer(players[1].getPlayerName(), clientConnection2);
        RemoteView remoteView1 = new RemoteView(players[1], players[0].getPlayerName(), clientConnection2, lobby);

        DrawedCards drawedCards = new DrawedCards(players[0], 0, 6, remoteView);
        controller.drawedCards(drawedCards);

        PickedCard pickedCard = new PickedCard(players[1], remoteView1, 1);
        controller.pickACard(pickedCard);

        PlayerWorker playerWorker = new PlayerWorker(players[0], 0, 0, remoteView);
        controller.setPlayerWorker(playerWorker);
        PlayerWorker playerWorker2 = new PlayerWorker(players[0], 1, 1, remoteView);
        controller.setPlayerWorker(playerWorker2);

        PlayerWorker playerWorker3 = new PlayerWorker(players[1], 2, 2, remoteView1);
        controller.setPlayerWorker(playerWorker3);
        PlayerWorker playerWorker4 = new PlayerWorker(players[1], 3, 2, remoteView1);
        controller.setPlayerWorker(playerWorker4);

        PlayerMove playerMoveWorker1_s = new PlayerMove(players[0], 1, 0, 1, remoteView);
        controller.move(playerMoveWorker1_s);

        PlayerBuild playerBuildWorker1_d = new PlayerBuild(players[0], players[0].getUsedWorker(), 1, 0, remoteView);
        controller.build(playerBuildWorker1_d);

        PlayerMove playerMove2Worker0_a = new PlayerMove(players[1], 0, 2, 1, remoteView1);
        controller.move(playerMove2Worker0_a);

        PlayerBuild playerBuild2Worker0_a = new PlayerBuild(players[1], players[1].getUsedWorker(), 2, 0, remoteView1);
        controller.build(playerBuild2Worker0_a);

        PlayerMove playerMoveWorker0_d = new PlayerMove(players[0], 0, 1, 0, remoteView);
        controller.move(playerMoveWorker0_d);

        PlayerBuild playerBuildWorker0_d = new PlayerBuild(players[0], players[0].getUsedWorker(), 2, 0, remoteView);
        controller.build(playerBuildWorker0_d);

        PlayerMove playerMove2Worker1_a = new PlayerMove(players[1], 1, 3, 1, remoteView1);
        controller.move(playerMove2Worker1_a);

        PlayerBuild playerBuild2Worker1_a = new PlayerBuild(players[1], players[1].getUsedWorker(), 3, 0, remoteView1);
        controller.build(playerBuild2Worker1_a);

        PlayerMove playerMoveWorker0_d2 = new PlayerMove(players[0], 0, 2, 0, remoteView);
        controller.move(playerMoveWorker0_d2);

        PlayerBuild playerBuildWorker0_d2 = new PlayerBuild(players[0], players[0].getUsedWorker(), 3, 0, remoteView);
        controller.build(playerBuildWorker0_d2);

        PlayerMove playerMove2Worker1_d = new PlayerMove(players[1], 1, 4, 1, remoteView1);
        controller.move(playerMove2Worker1_d);

        PlayerBuild playerBuild2Worker1_s = new PlayerBuild(players[1], players[1].getUsedWorker(), 3, 0, remoteView1);
        controller.build(playerBuild2Worker1_s);

        PlayerMove playerMoveWorker0_win = new PlayerMove(players[0], 0, 3, 0, remoteView);
        controller.move(playerMoveWorker0_win);

        assertTrue(model.getActualPlayer().hasWon());

        char ch = 'y';
        NewGameMessage newGameMessage = new NewGameMessage(players[0], remoteView, ch, clientConnection, lobby);
        newGameMessage.handler(controller);

        NewGameMessage newGameMessage2 = new NewGameMessage(players[1], remoteView1, ch, clientConnection2, lobby);
        newGameMessage2.handler(controller);

        assertEquals(model.getPhase(), Phase.DRAWCARD);
    }

    @Test
    public void newGameNewPlayerTest() {
        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        Model model = new Model(players, false);
        GodCardController controller = new GodCardController(model);
        ClientConnection clientConnection = new ClientConnection() {
            @Override
            public void closeConnection() {

            }

            @Override
            public void send(Object message) {

            }

            @Override
            public void asyncSend(Object message) {

            }
        };
        Lobby lobby = new Lobby("ciao", players[0].getPlayerName(), clientConnection, 2, false);
        RemoteView remoteView = new RemoteView(players[0], players[1].getPlayerName(), clientConnection, lobby);

        ClientConnection clientConnection2 = new ClientConnection() {
            @Override
            public void closeConnection() {

            }

            @Override
            public void send(Object message) {

            }

            @Override
            public void asyncSend(Object message) {

            }
        };
        lobby.addPlayer(players[1].getPlayerName(), clientConnection2);
        RemoteView remoteView1 = new RemoteView(players[1], players[0].getPlayerName(), clientConnection2, lobby);

        DrawedCards drawedCards = new DrawedCards(players[0], 0, 6, remoteView);
        controller.drawedCards(drawedCards);

        PickedCard pickedCard = new PickedCard(players[1], remoteView1, 1);
        controller.pickACard(pickedCard);

        PlayerWorker playerWorker = new PlayerWorker(players[0], 0, 0, remoteView);
        controller.setPlayerWorker(playerWorker);
        PlayerWorker playerWorker2 = new PlayerWorker(players[0], 1, 1, remoteView);
        controller.setPlayerWorker(playerWorker2);

        PlayerWorker playerWorker3 = new PlayerWorker(players[1], 2, 2, remoteView1);
        controller.setPlayerWorker(playerWorker3);
        PlayerWorker playerWorker4 = new PlayerWorker(players[1], 3, 2, remoteView1);
        controller.setPlayerWorker(playerWorker4);

        PlayerMove playerMoveWorker1_s = new PlayerMove(players[0], 1, 0, 1, remoteView);
        controller.move(playerMoveWorker1_s);

        PlayerBuild playerBuildWorker1_d = new PlayerBuild(players[0], players[0].getUsedWorker(), 1, 0, remoteView);
        controller.build(playerBuildWorker1_d);

        PlayerMove playerMove2Worker0_a = new PlayerMove(players[1], 0, 2, 1, remoteView1);
        controller.move(playerMove2Worker0_a);

        PlayerBuild playerBuild2Worker0_a = new PlayerBuild(players[1], players[1].getUsedWorker(), 2, 0, remoteView1);
        controller.build(playerBuild2Worker0_a);

        PlayerMove playerMoveWorker0_d = new PlayerMove(players[0], 0, 1, 0, remoteView);
        controller.move(playerMoveWorker0_d);

        PlayerBuild playerBuildWorker0_d = new PlayerBuild(players[0], players[0].getUsedWorker(), 2, 0, remoteView);
        controller.build(playerBuildWorker0_d);

        PlayerMove playerMove2Worker1_a = new PlayerMove(players[1], 1, 3, 1, remoteView1);
        controller.move(playerMove2Worker1_a);

        PlayerBuild playerBuild2Worker1_a = new PlayerBuild(players[1], players[1].getUsedWorker(), 3, 0, remoteView1);
        controller.build(playerBuild2Worker1_a);

        PlayerMove playerMoveWorker0_d2 = new PlayerMove(players[0], 0, 2, 0, remoteView);
        controller.move(playerMoveWorker0_d2);

        PlayerBuild playerBuildWorker0_d2 = new PlayerBuild(players[0], players[0].getUsedWorker(), 3, 0, remoteView);
        controller.build(playerBuildWorker0_d2);

        PlayerMove playerMove2Worker1_d = new PlayerMove(players[1], 1, 4, 1, remoteView1);
        controller.move(playerMove2Worker1_d);

        PlayerBuild playerBuild2Worker1_s = new PlayerBuild(players[1], players[1].getUsedWorker(), 3, 0, remoteView1);
        controller.build(playerBuild2Worker1_s);

        PlayerMove playerMoveWorker0_win = new PlayerMove(players[0], 0, 3, 0, remoteView);
        controller.move(playerMoveWorker0_win);

        assertTrue(model.getActualPlayer().hasWon());

        char ch = 'y';
        NewGameMessage newGameMessage = new NewGameMessage(players[0], remoteView, ch, clientConnection, lobby);
        newGameMessage.handler(controller);

        char ch2 = 'n';
        NewGameMessage newGameMessage2 = new NewGameMessage(players[1], remoteView1, ch2, clientConnection2, lobby);
        newGameMessage2.handler(controller);

        Lobby lobby1 = new Lobby("pizza", players[0].getPlayerName(), clientConnection, 2, false);

        Player player3 = new Player("Toad");

        Player[] players1 = new Player[2];
        players1[0] = players[0];
        players1[1] = player3;
        Model model1 = new Model(players1, false);
        Controller controller1 = new Controller(model1);

        ClientConnection clientConnection3 = new ClientConnection() {
            @Override
            public void closeConnection() {

            }

            @Override
            public void send(Object message) {

            }

            @Override
            public void asyncSend(Object message) {

            }
        };
        lobby1.addPlayer(players1[1].getPlayerName(), clientConnection3);
        RemoteView remoteView2 = new RemoteView(players1[1], players1[0].getPlayerName(), clientConnection3, lobby1);

        assertEquals(controller1.getModel().getPhase(), Phase.DRAWCARD);
    }

    @Test
    public void newGame3PlayersTest() {
        Player[] players = new Player[3];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        players[2] = new Player("Toad");
        Model model = new Model(players, false);
        GodCardController controller = new GodCardController(model);
        ClientConnection clientConnection = new ClientConnection() {
            @Override
            public void closeConnection() {

            }

            @Override
            public void send(Object message) {

            }

            @Override
            public void asyncSend(Object message) {

            }
        };
        Lobby lobby = new Lobby("ciao", players[0].getPlayerName(), clientConnection, 3, false);
        RemoteView remoteView = new RemoteView(players[0], players[1].getPlayerName(), players[2].getPlayerName(), clientConnection, lobby);

        ClientConnection clientConnection2 = new ClientConnection() {
            @Override
            public void closeConnection() {

            }

            @Override
            public void send(Object message) {

            }

            @Override
            public void asyncSend(Object message) {

            }
        };
        lobby.addPlayer(players[1].getPlayerName(), clientConnection2);
        RemoteView remoteView1 = new RemoteView(players[1], players[0].getPlayerName(), players[2].getPlayerName(), clientConnection2, lobby);

        ClientConnection clientConnection3 = new ClientConnection() {
            @Override
            public void closeConnection() {

            }

            @Override
            public void send(Object message) {

            }

            @Override
            public void asyncSend(Object message) {

            }
        };
        lobby.addPlayer(players[2].getPlayerName(), clientConnection3);
        RemoteView remoteView2 = new RemoteView(players[2], players[0].getPlayerName(), players[1].getPlayerName(), clientConnection3, lobby);

        DrawedCards drawedCards = new DrawedCards(players[0], 0, 6, 7, remoteView);
        controller.drawedCards(drawedCards);

        PickedCard pickedCard = new PickedCard(players[1], remoteView1, 2);
        controller.pickACard(pickedCard);

        PickedCard pickedCard2 = new PickedCard(players[2], remoteView2, 1);
        controller.pickACard(pickedCard2);

        PlayerWorker playerWorker = new PlayerWorker(players[0], 0, 0, remoteView);
        controller.setPlayerWorker(playerWorker);
        PlayerWorker playerWorker2 = new PlayerWorker(players[0], 1, 1, remoteView);
        controller.setPlayerWorker(playerWorker2);

        PlayerWorker playerWorker3 = new PlayerWorker(players[1], 2, 2, remoteView1);
        controller.setPlayerWorker(playerWorker3);
        PlayerWorker playerWorker4 = new PlayerWorker(players[1], 3, 2, remoteView1);
        controller.setPlayerWorker(playerWorker4);

        PlayerWorker playerWorker5 = new PlayerWorker(players[2], 2, 4, remoteView2);
        controller.setPlayerWorker(playerWorker5);
        PlayerWorker playerWorker6 = new PlayerWorker(players[2], 4, 0, remoteView2);
        controller.setPlayerWorker(playerWorker6);

        PlayerMove playerMoveWorker1_s = new PlayerMove(players[0], 1, 0, 1, remoteView);
        controller.move(playerMoveWorker1_s);
        PlayerBuild playerBuildWorker1_d = new PlayerBuild(players[0], players[0].getUsedWorker(), 1, 0, remoteView);
        controller.build(playerBuildWorker1_d);

        PlayerMove playerMove2Worker0_a = new PlayerMove(players[1], 0, 2, 1, remoteView1);
        controller.move(playerMove2Worker0_a);
        PlayerBuild playerBuild2Worker0_a = new PlayerBuild(players[1], players[1].getUsedWorker(), 2, 0, remoteView1);
        controller.build(playerBuild2Worker0_a);

        PlayerMove playerMove = new PlayerMove(players[2], 1, 4, 1, remoteView2);
        controller.move(playerMove);
        PlayerBuild playerBuild = new PlayerBuild(players[2], players[2].getUsedWorker(), 3, 0, remoteView2);
        controller.build(playerBuild);

        PlayerMove playerMoveWorker0_d = new PlayerMove(players[0], 0, 1, 0, remoteView);
        controller.move(playerMoveWorker0_d);
        PlayerBuild playerBuildWorker0_d = new PlayerBuild(players[0], players[0].getUsedWorker(), 2, 0, remoteView);
        controller.build(playerBuildWorker0_d);

        PlayerMove playerMove2Worker1_a = new PlayerMove(players[1], 0, 1, 1, remoteView1);
        controller.move(playerMove2Worker1_a);
        PlayerBuild playerBuild2Worker1_a = new PlayerBuild(players[1], players[1].getUsedWorker(), 2, 1, remoteView1);
        controller.build(playerBuild2Worker1_a);

        PlayerMove playerMove2 = new PlayerMove(players[2], 1, 4, 0, remoteView2);
        controller.move(playerMove2);
        PlayerBuild playerBuild2 = new PlayerBuild(players[2], players[2].getUsedWorker(), 3, 0, remoteView2);
        controller.build(playerBuild2);

        PlayerMove playerMoveWorker0_d2 = new PlayerMove(players[0], 0, 2, 0, remoteView);
        controller.move(playerMoveWorker0_d2);
        PlayerBuild playerBuildWorker0_d2 = new PlayerBuild(players[0], players[0].getUsedWorker(), 3, 0, remoteView);
        controller.build(playerBuildWorker0_d2);

        PlayerMove playerMove2Worker1_d = new PlayerMove(players[1], 0, 1, 0, remoteView1);
        controller.move(playerMove2Worker1_d);
        PlayerBuild playerBuild2Worker1_s = new PlayerBuild(players[1], players[1].getUsedWorker(), 1, 1, remoteView1);
        controller.build(playerBuild2Worker1_s);

        controller.move(playerMove);
        PlayerBuild playerBuild12 = new PlayerBuild(players[2], 1, 4, 0, remoteView2);
        controller.build(playerBuild12);

        PlayerMove playerMoveWorker0_win = new PlayerMove(players[0], 0, 3, 0, remoteView);
        controller.move(playerMoveWorker0_win);

        assertTrue(model.getPlayer(0).hasWon());
        assertEquals(model.getActualPlayer(), model.getPlayer(1));

        PlayerMove playerMove2Worker0_win = new PlayerMove(players[1], 0, 2, 0, remoteView1);
        controller.move(playerMove2Worker0_win);
        PlayerBuild playerBuild2Worker1_win = new PlayerBuild(players[1], players[1].getUsedWorker(), 2, 1, remoteView1);
        controller.build(playerBuild2Worker1_win);

        controller.move(playerMove2);
        PlayerBuild playerBuild1 = new PlayerBuild(players[2], 1, 4, 1, remoteView2);
        controller.build(playerBuild1);

        PlayerMove playerMove2Worker0_winNow = new PlayerMove(players[1], 0, 3, 0, remoteView1);
        controller.move(playerMove2Worker0_winNow);
        assertTrue(model.getPlayer(1).hasWon());

        char ch = 'y';
        NewGameMessage newGameMessage = new NewGameMessage(players[0], remoteView, ch, clientConnection, lobby);
        newGameMessage.handler(controller);

        NewGameMessage newGameMessage2 = new NewGameMessage(players[1], remoteView1, ch, clientConnection2, lobby);
        newGameMessage2.handler(controller);

        NewGameMessage newGameMessage3 = new NewGameMessage(players[2], remoteView2, ch, clientConnection3, lobby);
        newGameMessage3.handler(controller);

        assertEquals(model.getPhase(), Phase.DRAWCARD);
    }

    @Test
    public void newGame3PlayersNewPlayerTest() {
        Player[] players = new Player[3];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        players[2] = new Player("Toad");
        Model model = new Model(players, false);
        GodCardController controller = new GodCardController(model);
        ClientConnection clientConnection = new ClientConnection() {
            @Override
            public void closeConnection() {

            }

            @Override
            public void send(Object message) {

            }

            @Override
            public void asyncSend(Object message) {

            }
        };
        Lobby lobby = new Lobby("ciao", players[0].getPlayerName(), clientConnection, 3, false);
        RemoteView remoteView = new RemoteView(players[0], players[1].getPlayerName(), players[2].getPlayerName(), clientConnection, lobby);

        ClientConnection clientConnection2 = new ClientConnection() {
            @Override
            public void closeConnection() {

            }

            @Override
            public void send(Object message) {

            }

            @Override
            public void asyncSend(Object message) {

            }
        };
        lobby.addPlayer(players[1].getPlayerName(), clientConnection2);
        RemoteView remoteView1 = new RemoteView(players[1], players[0].getPlayerName(), players[2].getPlayerName(), clientConnection2, lobby);

        ClientConnection clientConnection3 = new ClientConnection() {
            @Override
            public void closeConnection() {

            }

            @Override
            public void send(Object message) {

            }

            @Override
            public void asyncSend(Object message) {

            }
        };
        lobby.addPlayer(players[2].getPlayerName(), clientConnection3);
        RemoteView remoteView2 = new RemoteView(players[2], players[0].getPlayerName(), players[1].getPlayerName(), clientConnection3, lobby);

        DrawedCards drawedCards = new DrawedCards(players[0], 0, 6, 7, remoteView);
        controller.drawedCards(drawedCards);

        PickedCard pickedCard = new PickedCard(players[1], remoteView1, 2);
        controller.pickACard(pickedCard);

        PickedCard pickedCard2 = new PickedCard(players[2], remoteView2, 1);
        controller.pickACard(pickedCard2);

        PlayerWorker playerWorker = new PlayerWorker(players[0], 0, 0, remoteView);
        controller.setPlayerWorker(playerWorker);
        PlayerWorker playerWorker2 = new PlayerWorker(players[0], 1, 1, remoteView);
        controller.setPlayerWorker(playerWorker2);

        PlayerWorker playerWorker3 = new PlayerWorker(players[1], 2, 2, remoteView1);
        controller.setPlayerWorker(playerWorker3);
        PlayerWorker playerWorker4 = new PlayerWorker(players[1], 3, 2, remoteView1);
        controller.setPlayerWorker(playerWorker4);

        PlayerWorker playerWorker5 = new PlayerWorker(players[2], 2, 4, remoteView2);
        controller.setPlayerWorker(playerWorker5);
        PlayerWorker playerWorker6 = new PlayerWorker(players[2], 4, 0, remoteView2);
        controller.setPlayerWorker(playerWorker6);

        PlayerMove playerMoveWorker1_s = new PlayerMove(players[0], 1, 0, 1, remoteView);
        controller.move(playerMoveWorker1_s);
        PlayerBuild playerBuildWorker1_d = new PlayerBuild(players[0], players[0].getUsedWorker(), 1, 0, remoteView);
        controller.build(playerBuildWorker1_d);

        PlayerMove playerMove2Worker0_a = new PlayerMove(players[1], 0, 2, 1, remoteView1);
        controller.move(playerMove2Worker0_a);
        PlayerBuild playerBuild2Worker0_a = new PlayerBuild(players[1], players[1].getUsedWorker(), 2, 0, remoteView1);
        controller.build(playerBuild2Worker0_a);

        PlayerMove playerMove = new PlayerMove(players[2], 1, 4, 1, remoteView2);
        controller.move(playerMove);
        PlayerBuild playerBuild = new PlayerBuild(players[2], players[2].getUsedWorker(), 3, 0, remoteView2);
        controller.build(playerBuild);

        PlayerMove playerMoveWorker0_d = new PlayerMove(players[0], 0, 1, 0, remoteView);
        controller.move(playerMoveWorker0_d);
        PlayerBuild playerBuildWorker0_d = new PlayerBuild(players[0], players[0].getUsedWorker(), 2, 0, remoteView);
        controller.build(playerBuildWorker0_d);

        PlayerMove playerMove2Worker1_a = new PlayerMove(players[1], 0, 1, 1, remoteView1);
        controller.move(playerMove2Worker1_a);
        PlayerBuild playerBuild2Worker1_a = new PlayerBuild(players[1], players[1].getUsedWorker(), 2, 1, remoteView1);
        controller.build(playerBuild2Worker1_a);

        PlayerMove playerMove2 = new PlayerMove(players[2], 1, 4, 0, remoteView2);
        controller.move(playerMove2);
        PlayerBuild playerBuild2 = new PlayerBuild(players[2], players[2].getUsedWorker(), 3, 0, remoteView2);
        controller.build(playerBuild2);

        PlayerMove playerMoveWorker0_d2 = new PlayerMove(players[0], 0, 2, 0, remoteView);
        controller.move(playerMoveWorker0_d2);
        PlayerBuild playerBuildWorker0_d2 = new PlayerBuild(players[0], players[0].getUsedWorker(), 3, 0, remoteView);
        controller.build(playerBuildWorker0_d2);

        PlayerMove playerMove2Worker1_d = new PlayerMove(players[1], 0, 1, 0, remoteView1);
        controller.move(playerMove2Worker1_d);
        PlayerBuild playerBuild2Worker1_s = new PlayerBuild(players[1], players[1].getUsedWorker(), 1, 1, remoteView1);
        controller.build(playerBuild2Worker1_s);

        controller.move(playerMove);
        PlayerBuild playerBuild12 = new PlayerBuild(players[2], 1, 4, 0, remoteView2);
        controller.build(playerBuild12);

        PlayerMove playerMoveWorker0_win = new PlayerMove(players[0], 0, 3, 0, remoteView);
        controller.move(playerMoveWorker0_win);

        assertTrue(model.getPlayer(0).hasWon());
        assertEquals(model.getActualPlayer(), model.getPlayer(1));

        PlayerMove playerMove2Worker0_win = new PlayerMove(players[1], 0, 2, 0, remoteView1);
        controller.move(playerMove2Worker0_win);
        PlayerBuild playerBuild2Worker1_win = new PlayerBuild(players[1], players[1].getUsedWorker(), 2, 1, remoteView1);
        controller.build(playerBuild2Worker1_win);

        controller.move(playerMove2);
        PlayerBuild playerBuild1 = new PlayerBuild(players[2], 1, 4, 1, remoteView2);
        controller.build(playerBuild1);

        PlayerMove playerMove2Worker0_winNow = new PlayerMove(players[1], 0, 3, 0, remoteView1);
        controller.move(playerMove2Worker0_winNow);
        assertTrue(model.getPlayer(1).hasWon());

        char ch = 'y';
        NewGameMessage newGameMessage = new NewGameMessage(players[0], remoteView, ch, clientConnection, lobby);
        newGameMessage.handler(controller);

        NewGameMessage newGameMessage2 = new NewGameMessage(players[1], remoteView1, ch, clientConnection2, lobby);
        newGameMessage2.handler(controller);

        char ch2 = 'n';
        NewGameMessage newGameMessage3 = new NewGameMessage(players[2], remoteView2, ch2, clientConnection3, lobby);
        newGameMessage3.handler(controller);

        Lobby lobby1 = new Lobby("pizza", players[0].getPlayerName(), clientConnection, 3, false);
        lobby1.addPlayer(players[1].getPlayerName(), clientConnection2);
        Player player3 = new Player("Peach");

        Player[] players1 = new Player[3];
        players1[0] = players[0];
        players1[1] = players[1];
        players1[2] = player3;
        Model model1 = new Model(players1, false);
        Controller controller1 = new Controller(model1);

        ClientConnection clientConnection4 = new ClientConnection() {
            @Override
            public void closeConnection() {

            }

            @Override
            public void send(Object message) {

            }

            @Override
            public void asyncSend(Object message) {

            }
        };
        lobby1.addPlayer(players1[2].getPlayerName(), clientConnection4);
        RemoteView remoteView4 = new RemoteView(players1[2], players1[0].getPlayerName(), players[1].getPlayerName(), clientConnection4, lobby1);

        assertEquals(model1.getPhase(), Phase.DRAWCARD);
    }

    @Test
    public void checkLooseCantBuildTest() {
        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        Model model = new Model(players, false);
        GodCardController controller = new GodCardController(model);
        ClientConnection clientConnection = new ClientConnection() {
            @Override
            public void closeConnection() {

            }

            @Override
            public void send(Object message) {

            }

            @Override
            public void asyncSend(Object message) {

            }
        };
        Lobby lobby = new Lobby("ciao", players[0].getPlayerName(), clientConnection, 2, false);
        RemoteView remoteView = new RemoteView(players[0], players[1].getPlayerName(), clientConnection, lobby);

        ClientConnection clientConnection2 = new ClientConnection() {
            @Override
            public void closeConnection() {

            }

            @Override
            public void send(Object message) {

            }

            @Override
            public void asyncSend(Object message) {

            }
        };
        lobby.addPlayer(players[1].getPlayerName(), clientConnection2);
        RemoteView remoteView1 = new RemoteView(players[1], players[0].getPlayerName(), clientConnection2, lobby);

        DrawedCards drawedCards = new DrawedCards(players[0], 0, 11, remoteView);
        controller.drawedCards(drawedCards);

        PickedCard pickedCard = new PickedCard(players[1], remoteView1, 0);
        controller.pickACard(pickedCard);

        PlayerWorker playerWorker = new PlayerWorker(players[0], 0, 0, remoteView);
        controller.setPlayerWorker(playerWorker);
        PlayerWorker playerWorker2 = new PlayerWorker(players[0], 1, 1, remoteView);
        controller.setPlayerWorker(playerWorker2);

        PlayerWorker playerWorker3 = new PlayerWorker(players[1], 4, 4, remoteView1);
        controller.setPlayerWorker(playerWorker3);
        PlayerWorker playerWorker4 = new PlayerWorker(players[1], 0, 1, remoteView1);
        controller.setPlayerWorker(playerWorker4);

        PlayerMove playerMoveWorker1_s = new PlayerMove(players[0], 1, 2, 2, remoteView);
        controller.move(playerMoveWorker1_s);

        PlayerBuild playerBuildWorker1_d = new PlayerBuild(players[0], players[0].getUsedWorker(), 1, 1, remoteView);
        controller.build(playerBuildWorker1_d);

        char ch = 'y';
        UseGodPower useGodPower = new UseGodPower(players[0], remoteView, ch);
        useGodPower.handler(controller);

        PlayerBuild playerBuild = new PlayerBuild(players[0], 0, 1, 0, remoteView);
        controller.build(playerBuild);

        useGodPower.handler(controller);
        controller.build(playerBuild);
        useGodPower.handler(controller);
        controller.build(playerBuild);

        PlayerMove playerMove2Worker0_a = new PlayerMove(players[1], 0, 3, 3, remoteView1);
        controller.move(playerMove2Worker0_a);

        PlayerBuild playerBuild2Worker0_a = new PlayerBuild(players[1], players[1].getUsedWorker(), 4, 4, remoteView1);
        controller.build(playerBuild2Worker0_a);

        PlayerMove playerMoveWorker0_d = new PlayerMove(players[0], 1, 1, 1, remoteView);
        controller.move(playerMoveWorker0_d);

        PlayerBuild playerBuildWorker0_d = new PlayerBuild(players[0], players[0].getUsedWorker(), 2, 2, remoteView);
        controller.build(playerBuildWorker0_d);

        useGodPower.handler(controller);
        controller.build(playerBuild);

        assertEquals(model.getPhase(), Phase.MOVE);
    }

    @Test
    public void notUsePowerTest() {
        Player[] players = new Player[3];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        players[2] = new Player("Toad");
        Model model = new Model(players, false);
        GodCardController controller = new GodCardController(model);
        ClientConnection clientConnection = new ClientConnection() {
            @Override
            public void closeConnection() {

            }

            @Override
            public void send(Object message) {

            }

            @Override
            public void asyncSend(Object message) {

            }
        };
        Lobby lobby = new Lobby("ciao", players[0].getPlayerName(), clientConnection, 3, false);
        RemoteView remoteView = new RemoteView(players[0], players[1].getPlayerName(), players[2].getPlayerName(), clientConnection, lobby);

        ClientConnection clientConnection2 = new ClientConnection() {
            @Override
            public void closeConnection() {

            }

            @Override
            public void send(Object message) {

            }

            @Override
            public void asyncSend(Object message) {

            }
        };
        lobby.addPlayer(players[1].getPlayerName(), clientConnection2);
        RemoteView remoteView1 = new RemoteView(players[1], players[0].getPlayerName(), players[2].getPlayerName(), clientConnection2, lobby);

        ClientConnection clientConnection3 = new ClientConnection() {
            @Override
            public void closeConnection() {

            }

            @Override
            public void send(Object message) {

            }

            @Override
            public void asyncSend(Object message) {

            }
        };
        lobby.addPlayer(players[2].getPlayerName(), clientConnection3);
        RemoteView remoteView2 = new RemoteView(players[2], players[0].getPlayerName(), players[1].getPlayerName(), clientConnection3, lobby);

        DrawedCards drawedCards = new DrawedCards(players[0], 1, 4, 8, remoteView);
        controller.drawedCards(drawedCards);

        PickedCard pickedCard = new PickedCard(players[1], remoteView1, 1);
        controller.pickACard(pickedCard);

        PickedCard pickedCard2 = new PickedCard(players[2], remoteView2, 1);
        controller.pickACard(pickedCard2);

        PlayerWorker playerWorker = new PlayerWorker(players[0], 0, 0, remoteView);
        controller.setPlayerWorker(playerWorker);
        PlayerWorker playerWorker2 = new PlayerWorker(players[0], 1, 1, remoteView);
        controller.setPlayerWorker(playerWorker2);

        PlayerWorker playerWorker3 = new PlayerWorker(players[1], 2, 2, remoteView1);
        controller.setPlayerWorker(playerWorker3);
        PlayerWorker playerWorker4 = new PlayerWorker(players[1], 3, 2, remoteView1);
        controller.setPlayerWorker(playerWorker4);

        PlayerWorker playerWorker5 = new PlayerWorker(players[2], 2, 4, remoteView2);
        controller.setPlayerWorker(playerWorker5);
        PlayerWorker playerWorker6 = new PlayerWorker(players[2], 4, 0, remoteView2);
        controller.setPlayerWorker(playerWorker6);

        PlayerMove playerMoveWorker1_s = new PlayerMove(players[0], 1, 0, 1, remoteView);
        controller.move(playerMoveWorker1_s);
        char ch = 'n';
        UseGodPower useGodPower = new UseGodPower(players[0], remoteView, ch);
        useGodPower.handler(controller);
        PlayerBuild playerBuildWorker1_d = new PlayerBuild(players[0], players[0].getUsedWorker(), 1, 0, remoteView);
        controller.build(playerBuildWorker1_d);


        PlayerMove playerMove2Worker0_a = new PlayerMove(players[1], 0, 2, 1, remoteView1);
        controller.move(playerMove2Worker0_a);
        PlayerBuild playerBuild2Worker0_a = new PlayerBuild(players[1], players[1].getUsedWorker(), 2, 0, remoteView1);
        controller.build(playerBuild2Worker0_a);
        UseGodPower useGodPower2 = new UseGodPower(players[1], remoteView1, ch);
        useGodPower2.handler(controller);

        UseGodPower useGodPower3 = new UseGodPower(players[2], remoteView2, ch);
        useGodPower3.handler(controller);
        PlayerMove playerMove = new PlayerMove(players[2], 1, 4, 1, remoteView2);
        controller.move(playerMove);
        PlayerBuild playerBuild = new PlayerBuild(players[2], players[2].getUsedWorker(), 3, 0, remoteView2);
        controller.build(playerBuild);
    }

    @Test
    public void notUsePowerPart2Test() {
        Player[] players = new Player[3];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        players[2] = new Player("Toad");
        Model model = new Model(players, false);
        GodCardController controller = new GodCardController(model);
        ClientConnection clientConnection = new ClientConnection() {
            @Override
            public void closeConnection() {

            }

            @Override
            public void send(Object message) {

            }

            @Override
            public void asyncSend(Object message) {

            }
        };
        Lobby lobby = new Lobby("ciao", players[0].getPlayerName(), clientConnection, 3, false);
        RemoteView remoteView = new RemoteView(players[0], players[1].getPlayerName(), players[2].getPlayerName(), clientConnection, lobby);

        ClientConnection clientConnection2 = new ClientConnection() {
            @Override
            public void closeConnection() {

            }

            @Override
            public void send(Object message) {

            }

            @Override
            public void asyncSend(Object message) {

            }
        };
        lobby.addPlayer(players[1].getPlayerName(), clientConnection2);
        RemoteView remoteView1 = new RemoteView(players[1], players[0].getPlayerName(), players[2].getPlayerName(), clientConnection2, lobby);

        ClientConnection clientConnection3 = new ClientConnection() {
            @Override
            public void closeConnection() {

            }

            @Override
            public void send(Object message) {

            }

            @Override
            public void asyncSend(Object message) {

            }
        };
        lobby.addPlayer(players[2].getPlayerName(), clientConnection3);
        RemoteView remoteView2 = new RemoteView(players[2], players[0].getPlayerName(), players[1].getPlayerName(), clientConnection3, lobby);

        DrawedCards drawedCards = new DrawedCards(players[0], 1, 4, 0, remoteView);
        controller.drawedCards(drawedCards);

        PickedCard pickedCard = new PickedCard(players[1], remoteView1, 1);
        controller.pickACard(pickedCard);

        PickedCard pickedCard2 = new PickedCard(players[2], remoteView2, 1);
        controller.pickACard(pickedCard2);

        PlayerWorker playerWorker = new PlayerWorker(players[0], 0, 0, remoteView);
        controller.setPlayerWorker(playerWorker);
        PlayerWorker playerWorker2 = new PlayerWorker(players[0], 1, 1, remoteView);
        controller.setPlayerWorker(playerWorker2);

        PlayerWorker playerWorker3 = new PlayerWorker(players[1], 2, 2, remoteView1);
        controller.setPlayerWorker(playerWorker3);
        PlayerWorker playerWorker4 = new PlayerWorker(players[1], 3, 2, remoteView1);
        controller.setPlayerWorker(playerWorker4);

        PlayerWorker playerWorker5 = new PlayerWorker(players[2], 2, 4, remoteView2);
        controller.setPlayerWorker(playerWorker5);
        PlayerWorker playerWorker6 = new PlayerWorker(players[2], 4, 0, remoteView2);
        controller.setPlayerWorker(playerWorker6);

        PlayerMove playerMoveWorker1_s = new PlayerMove(players[0], 1, 0, 1, remoteView);
        controller.move(playerMoveWorker1_s);
        char ch = 'n';
        UseGodPower useGodPower = new UseGodPower(players[0], remoteView, ch);
        useGodPower.handler(controller);
        PlayerBuild playerBuildWorker1_d = new PlayerBuild(players[0], players[0].getUsedWorker(), 1, 0, remoteView);
        controller.build(playerBuildWorker1_d);


        PlayerMove playerMove2Worker0_a = new PlayerMove(players[1], 0, 2, 1, remoteView1);
        controller.move(playerMove2Worker0_a);
        PlayerBuild playerBuild2Worker0_a = new PlayerBuild(players[1], players[1].getUsedWorker(), 2, 0, remoteView1);
        controller.build(playerBuild2Worker0_a);
        UseGodPower useGodPower2 = new UseGodPower(players[1], remoteView1, ch);
        useGodPower2.handler(controller);


        PlayerMove playerMove = new PlayerMove(players[2], 1, 4, 1, remoteView2);
        controller.move(playerMove);
        PlayerBuild playerBuild = new PlayerBuild(players[2], players[2].getUsedWorker(), 3, 0, remoteView2);
        controller.build(playerBuild);
    }

    @Test
    public void dontSsePoseidonPowerTest() {
        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        Model model = new Model(players, false);
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
        }, null);
        DrawedCards drawedCards = new DrawedCards(players[0], 0, 11, remoteView);
        controller.drawedCards(drawedCards);
        RemoteView remoteView2 = new RemoteView(players[1], players[0].getPlayerName(), new ClientConnection() {
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
        PickedCard pickedCard = new PickedCard(players[1], remoteView2, 0);
        controller.pickACard(pickedCard);

        PlayerWorker playerWorker = new PlayerWorker(players[0], 0, 0, remoteView);
        controller.setPlayerWorker(playerWorker);
        PlayerWorker playerWorker2 = new PlayerWorker(players[0], 3, 0, remoteView);
        controller.setPlayerWorker(playerWorker2);

        PlayerWorker playerWorker3 = new PlayerWorker(players[1], 2, 2, remoteView2);
        controller.setPlayerWorker(playerWorker3);
        PlayerWorker playerWorker4 = new PlayerWorker(players[1], 3, 2, remoteView2);
        controller.setPlayerWorker(playerWorker4);

        PlayerMove playerMove = new PlayerMove(players[0], 0, 0, 1, remoteView);
        controller.move(playerMove);

        PlayerBuild playerBuild = new PlayerBuild(players[0], players[0].getUsedWorker(), 0, 2, remoteView);
        controller.build(playerBuild);
        assertEquals(model.getPhase(), Phase.WAIT_GOD_ANSWER);
        char ch = 'n';
        UseGodPower useGodPower = new UseGodPower(players[0], remoteView, ch);
        useGodPower.handler(controller);
        assertEquals(model.getPhase(), Phase.MOVE);
    }
}
