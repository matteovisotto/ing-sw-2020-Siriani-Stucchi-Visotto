package it.polimi.ingsw.model.simplegod;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.messageModel.PlayerMove;
import it.polimi.ingsw.server.ClientConnection;
import it.polimi.ingsw.view.RemoteView;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class ArthemisTest {
    @Test
    public void testUsePower() {
        Cell cell = new Cell(1,2);
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
        PlayerMove playerMove = new PlayerMove(players[0],0,2,2,remoteView);
        List<Object> movingList = new ArrayList<>();
        movingList.add(model);
        movingList.add(playerMove);
        GodCard godCard = new Arthemis();
        model.getActualPlayer().setGodCard(godCard);
        model.getActualPlayer().getGodCard().usePower(movingList);
        godCard.usePower(movingList);
        assertTrue(players[0].getWorker(0).getCell().getX()==2);
        assertTrue(model.getActualPlayer().getWorker(0).getCell().getX()==2);
    }
}
