package it.polimi.ingsw.model.MessageModel;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.messageModel.DrawedCards;
import it.polimi.ingsw.server.ClientConnection;
import it.polimi.ingsw.view.RemoteView;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DrawedCardsTest {
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
}
