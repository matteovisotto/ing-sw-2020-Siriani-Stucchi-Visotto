package it.polimi.ingsw.model.simplegod;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.messageModel.PlayerMove;
import it.polimi.ingsw.server.ClientConnection;
import it.polimi.ingsw.view.RemoteView;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class DemeterTest {
    @Test
    public void testUsePower() {
        Cell cell = new Cell(1,2);
        Cell cellBuilt = new Cell(2,2);
        Cell cellBuiltStatic = new Cell(2,2);
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
        List<Object> builtList = new ArrayList<>();
        builtList.add(model);
        builtList.add(cellBuilt);
        GodCard godCard = new Demeter();
        godCard.usePower(builtList);
        assertTrue(cellBuilt.getLevel().getBlockId()==cellBuiltStatic.getLevel().getBlockId()+1);
    }
}
