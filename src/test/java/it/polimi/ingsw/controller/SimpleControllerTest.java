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

public class SimpleControllerTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void moveTest(){
        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        Model model = new Model(players,true);
        SimpleController controller = new SimpleController(model);
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
        }, null);
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
        }, null);

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
        SimpleController controller = new SimpleController(model);
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
        }, null);
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
        },null);

        PlayerWorker playerWorker3 = new PlayerWorker(players[1],3,4,remoteView1);
        controller.setPlayerWorker(playerWorker3);
        PlayerWorker playerWorker4 = new PlayerWorker(players[1],4,4,remoteView1);
        controller.setPlayerWorker(playerWorker4);

        PlayerMove playerMove = new PlayerMove(players[0],0,1,1,remoteView);
        controller.move(playerMove);

        PlayerBuild playerBuild = new PlayerBuild(players[0],players[0].getUsedWorker(),1,2,remoteView);
        controller.increaseLevel(playerBuild);
        assertEquals(1, controller.getModel().getBoard().getCell(1, 2).getLevel().getBlockId());
    }

    @Test
    public void playerCannotMoveTest(){

        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        Model model = new Model(players,true);
        SimpleController controller = new SimpleController(model);
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
        },null);
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
        }, null);

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

        assertEquals(2, controller.getModel().getBoard().getCell(0, 1).getLevel().getBlockId());
        assertEquals(2, controller.getModel().getBoard().getCell(1, 0).getLevel().getBlockId());
        assertEquals(2, controller.getModel().getBoard().getCell(1, 1).getLevel().getBlockId());
        assertEquals(controller.getModel().getActualPlayer().getPlayerName(), players[0].getPlayerName());

        PlayerMove playerMove = new PlayerMove(players[0],0,1,0,remoteView);
        controller.move(playerMove);
        assertFalse(controller.getModel().getActualPlayer().getWorker(0).getStatus());
    }

    @Test
    public void victoryTest(){
        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        Model model = new Model(players,true);
        SimpleController controller = new SimpleController(model);
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
        }, null);
        PlayerWorker playerWorker = new PlayerWorker(players[0],0,0,remoteView);
        controller.setPlayerWorker(playerWorker);
        PlayerWorker playerWorker2 = new PlayerWorker(players[0],1,1,remoteView);
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
        }, null);

        PlayerWorker playerWorker3 = new PlayerWorker(players[1],2,2,remoteView1);
        controller.setPlayerWorker(playerWorker3);
        PlayerWorker playerWorker4 = new PlayerWorker(players[1],3,2,remoteView1);
        controller.setPlayerWorker(playerWorker4);

        PlayerMove playerMoveWorker1_s = new PlayerMove(players[0],1,0,1,remoteView);
        controller.move(playerMoveWorker1_s);

        PlayerBuild playerBuildWorker1_d = new PlayerBuild(players[0],players[0].getUsedWorker(),1,0,remoteView);
        controller.increaseLevel(playerBuildWorker1_d);

        PlayerMove playerMove2Worker0_a = new PlayerMove(players[1],0,2,1,remoteView1);
        controller.move(playerMove2Worker0_a);

        PlayerBuild playerBuild2Worker0_a = new PlayerBuild(players[1],players[1].getUsedWorker(),2,0,remoteView1);
        controller.increaseLevel(playerBuild2Worker0_a);

        PlayerMove playerMoveWorker0_d = new PlayerMove(players[0],0,1,0,remoteView);
        controller.move(playerMoveWorker0_d);

        PlayerBuild playerBuildWorker0_d = new PlayerBuild(players[0],players[0].getUsedWorker(),2,0,remoteView);
        controller.increaseLevel(playerBuildWorker0_d);

        PlayerMove playerMove2Worker1_a = new PlayerMove(players[1],1,3,1,remoteView1);
        controller.move(playerMove2Worker1_a);

        PlayerBuild playerBuild2Worker1_a = new PlayerBuild(players[1],players[1].getUsedWorker(),3,0,remoteView1);
        controller.increaseLevel(playerBuild2Worker1_a);

        PlayerMove playerMoveWorker0_d2 = new PlayerMove(players[0],0,2,0,remoteView);
        controller.move(playerMoveWorker0_d2);

        PlayerBuild playerBuildWorker0_d2 = new PlayerBuild(players[0],players[0].getUsedWorker(),3,0,remoteView);
        controller.increaseLevel(playerBuildWorker0_d2);

        PlayerMove playerMove2Worker1_d = new PlayerMove(players[1],1,4,1,remoteView1);
        controller.move(playerMove2Worker1_d);

        PlayerBuild playerBuild2Worker1_s = new PlayerBuild(players[1],players[1].getUsedWorker(),3,0,remoteView1);
        controller.increaseLevel(playerBuild2Worker1_s);

        PlayerMove playerMoveWorker0_win = new PlayerMove(players[0],0,3,0,remoteView);
        controller.move(playerMoveWorker0_win);

        assertTrue(model.getActualPlayer().hasWon());
    }

    @Test
    public void buildADomeTest(){
        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        Model model = new Model(players,true);
        SimpleController controller = new SimpleController(model);
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
        }, null);
        PlayerWorker playerWorker = new PlayerWorker(players[0],0,0,remoteView);
        controller.setPlayerWorker(playerWorker);
        PlayerWorker playerWorker2 = new PlayerWorker(players[0],1,1,remoteView);
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
        }, null);

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

    @Test
    public void IllegalBuildException(){
        exception.expect(IllegalArgumentException.class);
        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        Model model = new Model(players,true);
        SimpleController controller = new SimpleController(model);
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
        }, null);
        PlayerWorker playerWorker = new PlayerWorker(players[0],0,0,remoteView);
        controller.setPlayerWorker(playerWorker);
        PlayerWorker playerWorker2 = new PlayerWorker(players[0],1,1,remoteView);
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
        }, null);

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

        controller.move(playerMoveWorker1_s);
        controller.increaseLevel(playerBuildWorker1_d);
    }
}