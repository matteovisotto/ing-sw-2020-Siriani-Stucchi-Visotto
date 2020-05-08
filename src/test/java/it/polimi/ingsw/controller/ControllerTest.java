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
        Controller controller = new Controller(model);
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
        Controller controller = new Controller(model);
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

    @Test
    public void moveTest(){
        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        Model model = new Model(players,true);
        Controller controller = new Controller(model);
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
        PlayerWorker playerWorker = new PlayerWorker(players[0],1,2,remoteView);
        controller.setPlayerWorker(playerWorker);
        PlayerWorker playerWorker2 = new PlayerWorker(players[0],1,4,remoteView);
        controller.setPlayerWorker(playerWorker2);

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
        PlayerWorker playerWorker4 = new PlayerWorker(players[1],4,4,remoteView1);
        controller.setPlayerWorker(playerWorker4);

        PlayerMove playerMove = new PlayerMove(players[0],0,1,1,remoteView);
        controller.move(playerMove);
        assertFalse(controller.getModel().getBoard().getCell(1,1).isFree());
        assertTrue(controller.getModel().getBoard().getCell(1,2).isFree());
    }

    @Test
    public void increaseLevelTest(){
        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        Model model = new Model(players,true);
        Controller controller = new Controller(model);
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
        PlayerWorker playerWorker = new PlayerWorker(players[0],1,2,remoteView);
        controller.setPlayerWorker(playerWorker);
        PlayerWorker playerWorker2 = new PlayerWorker(players[0],1,4,remoteView);
        controller.setPlayerWorker(playerWorker2);

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
        PlayerWorker playerWorker4 = new PlayerWorker(players[1],4,4,remoteView1);
        controller.setPlayerWorker(playerWorker4);

        PlayerMove playerMove = new PlayerMove(players[0],0,1,1,remoteView);
        controller.move(playerMove);

        PlayerBuild playerBuild = new PlayerBuild(players[0],players[0].getUsedWorker(),1,2,remoteView);
        controller.increaseLevel(playerBuild);
        assertTrue(controller.getModel().getBoard().getCell(1,2).getLevel().getBlockId()==1);
    }

    @Test
    public void playerCannotMoveTest(){

        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        Model model = new Model(players,true);
        Controller controller = new Controller(model);
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
        PlayerWorker playerWorker = new PlayerWorker(players[0],0,0,remoteView);
        controller.setPlayerWorker(playerWorker);
        PlayerWorker playerWorker2 = new PlayerWorker(players[0],0,3,remoteView);
        controller.setPlayerWorker(playerWorker2);

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

        PlayerWorker playerWorker3 = new PlayerWorker(players[1],3,0,remoteView1);
        controller.setPlayerWorker(playerWorker3);
        PlayerWorker playerWorker4 = new PlayerWorker(players[1],1,3,remoteView1);
        controller.setPlayerWorker(playerWorker4);

        PlayerMove playerMoveWorker1_a = new PlayerMove(players[0],1,0,2,remoteView);
        controller.move(playerMoveWorker1_a);

        PlayerBuild playerBuildWorker1_a = new PlayerBuild(players[0],players[0].getUsedWorker(),0,1,remoteView);
        controller.increaseLevel(playerBuildWorker1_a);

        PlayerMove playerMove2Worker0_a = new PlayerMove(players[1],0,2,0,remoteView1);
        controller.move(playerMove2Worker0_a);

        PlayerBuild playerBuild2Worker0_a = new PlayerBuild(players[1],players[1].getUsedWorker(),1,0,remoteView1);
        controller.increaseLevel(playerBuild2Worker0_a);

        PlayerMove playerMoveWorker1_i = new PlayerMove(players[0],1,0,3,remoteView);
        controller.move(playerMoveWorker1_i);

        PlayerBuild playerBuildWorker1_i = new PlayerBuild(players[0],players[0].getUsedWorker(),0,4,remoteView);
        controller.increaseLevel(playerBuildWorker1_i);

        PlayerMove playerMove2Worker1_a = new PlayerMove(players[1],1,1,2,remoteView1);
        controller.move(playerMove2Worker1_a);

        PlayerBuild playerBuild2Worker1_a = new PlayerBuild(players[1],players[1].getUsedWorker(),1,1,remoteView1);
        controller.increaseLevel(playerBuild2Worker1_a);

        controller.move(playerMoveWorker1_a);
        controller.increaseLevel(playerBuildWorker1_a);

        PlayerMove playerMove2Worker0_i = new PlayerMove(players[1],0,3,0,remoteView1);
        controller.move(playerMove2Worker0_i);

        PlayerBuild playerBuild2Worker0_i = new PlayerBuild(players[1],players[1].getUsedWorker(),4,0,remoteView1);
        controller.increaseLevel(playerBuild2Worker0_i);

        controller.move(playerMoveWorker1_i);
        controller.increaseLevel(playerBuildWorker1_i);

        controller.move(playerMove2Worker0_a);
        controller.increaseLevel(playerBuild2Worker0_a);

        controller.move(playerMoveWorker1_a);
        PlayerBuild playerBuildWorker1_l = new PlayerBuild(players[0],players[0].getUsedWorker(),1,1,remoteView);
        controller.increaseLevel(playerBuildWorker1_l);

        controller.move(playerMove2Worker0_i);
        controller.increaseLevel(playerBuild2Worker0_i);

        assertTrue(controller.getModel().getBoard().getCell(0,1).getLevel().getBlockId()==2);
        assertTrue(controller.getModel().getBoard().getCell(1,0).getLevel().getBlockId()==2);
        assertTrue(controller.getModel().getBoard().getCell(1,1).getLevel().getBlockId()==2);
        assertTrue(controller.getModel().getActualPlayer().getPlayerName().equals(players[0].getPlayerName()));

        PlayerMove playerMove = new PlayerMove(players[0],0,1,0,remoteView);
        controller.move(playerMove);
        assertFalse(controller.getModel().getActualPlayer().getWorker(0).getStatus());
    }
}