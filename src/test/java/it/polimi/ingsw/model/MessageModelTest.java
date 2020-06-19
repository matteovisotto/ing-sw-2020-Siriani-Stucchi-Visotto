package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.messageModel.*;
import it.polimi.ingsw.server.ClientConnection;
import it.polimi.ingsw.view.RemoteView;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class MessageModelTest {
    @Test
    public void drawedCardsTest(){
        Player player = new Player("Mario");
        Player player1 = new Player("Luigi");
        Player player2 = new Player("Toad");
        Player[] players = new Player[3];
        players[0] = player;
        players[1] = player1;
        players[2] = player2;
        Model model = new Model(players,true);
        Controller controller = new Controller(model);
        RemoteView remoteView = new RemoteView(player, player1.getPlayerName(), player2.getPlayerName(), new ClientConnection() {
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
        DrawedCards drawedCards = new DrawedCards(player,1,12,3,remoteView);
        assertEquals(drawedCards.getFirst(),1);
        assertEquals(drawedCards.getSecond(),12);
        assertEquals(drawedCards.getThird(),3);
    }

    @Test
    public void clientConfiguratorTest(){
        Player player = new Player("Toad");
        Player player1 = new Player("Mario");
        HashMap<String,String> opponentsNames = new HashMap<>();
        opponentsNames.put("red",player1.getPlayerName());
        ClientConfigurator clientConfigurator = new ClientConfigurator(2,opponentsNames,player);
        assertEquals(clientConfigurator.getNumberOfPlayer(),2);
        assertEquals(clientConfigurator.getOpponentsNames().get("red"),player1.getPlayerName());
        assertEquals(clientConfigurator.getMyself(),player);
    }

    @Test
    public void gameBoardMessage(){
        Player[] players = new Player[2];
        players[0] = new Player("Mario");
        players[1] = new Player("Luigi");
        Board board = new Board(players);
        GameBoardMessage gameBoardMessage = new GameBoardMessage(board,players[0],"Error", MessageType.MOVE,Phase.MOVE);
        assertEquals(gameBoardMessage.getBoard(),board);
    }

    @Test
    public void gameMessage(){
        Player player = new Player("Toad");
        GameMessage gameMessage = new GameMessage(player,"Error",MessageType.MOVE,Phase.MOVE);
        assertEquals(gameMessage.getPlayer(),player);
    }

    @Test
    public void endGameMessage(){
        Player player = new Player("Toad");
        Player player2 = new Player("Mario");
        HashMap<Player, Integer> values = new HashMap<>();
        values.put(player,1);
        values.put(player2,2);
        EndGameMessage endGameMessage = new EndGameMessage(player,"Error",MessageType.MOVE,Phase.MOVE,values);
        values = endGameMessage.getPodium();
        values.get(player);
    }

    @Test
    public void viewMessageTest(){
        ViewMessage viewMessage = new ViewMessage(MessageType.MOVE,"Moving",Phase.MOVE);
        assertEquals(viewMessage.getMessage(),"Moving");
        assertEquals(viewMessage.getMessageType(),MessageType.MOVE);
        assertEquals(viewMessage.getPhase(),Phase.MOVE);
    }
}
