package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.messageModel.PlayerBuild;
import it.polimi.ingsw.model.messageModel.PlayerMove;
import it.polimi.ingsw.model.messageModel.PlayerWorker;
import it.polimi.ingsw.server.ClientConnection;
import it.polimi.ingsw.view.RemoteView;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;


import static org.junit.Assert.*;

public class ControllerTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void setPlayerWorkerTest(){
        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        Model model = new Model(players,true);
        Controller controller = new SimpleController(model);
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
        controller.setPlayerWorker(playerWorker);
        assertFalse(controller.getModel().getBoard().getCell(1,2).isFree());
        PlayerWorker playerWorker2 = new PlayerWorker(players[0],1,4,remoteView);
        controller.setPlayerWorker(playerWorker2);
        assertFalse(controller.getModel().getBoard().getCell(1,4).isFree());

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

        PlayerWorker playerWorker3 = new PlayerWorker(players[1],3,4,remoteView1);
        controller.setPlayerWorker(playerWorker3);
        assertFalse(controller.getModel().getBoard().getCell(3,4).isFree());

        PlayerWorker playerWorker4 = new PlayerWorker(players[1],4,4,remoteView1);
        controller.setPlayerWorker(playerWorker4);
        assertFalse(controller.getModel().getBoard().getCell(4,4).isFree());
    }

    @Test
    public void SetWorkerException(){
        exception.expect(IllegalArgumentException.class);
        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        Model model = new Model(players,true);
        Controller controller = new SimpleController(model);
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
        controller.setPlayerWorker(playerWorker);
        assertFalse(controller.getModel().getBoard().getCell(-1,2).isFree());
    }
}
