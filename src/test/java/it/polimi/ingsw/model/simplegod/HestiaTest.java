package it.polimi.ingsw.model.simplegod;

import it.polimi.ingsw.controller.GodCardController;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.Phase;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.messageModel.*;
import it.polimi.ingsw.server.ClientConnection;
import it.polimi.ingsw.view.RemoteView;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HestiaTest {

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
}
