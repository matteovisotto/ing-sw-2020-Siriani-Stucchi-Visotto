package it.polimi.ingsw.Server;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.server.ClientConnection;
import it.polimi.ingsw.server.Server;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;

public class ServerTest {
    /*@Test
    public void createNewLobbyTest() throws IOException {
        Server server = new Server();
        Player player = new Player("Toad");
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
        server.addLobby("Pippo",clientConnection,player.getPlayerName(),2,false);
        assertEquals(server.getLobbiesCount(),1);
        assertEquals(server.getLobbiesNames(),"Select which lobby you want to join: \n" +
                "0 - Go Back\n" +
                "1 - Pippo\t[1/2]\tH\n"
                //"]");
        );
        Player player1 = new Player("Luigi");
        ClientConnection clientConnection1 = new ClientConnection() {
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
        server.joinLobby(1,clientConnection1,player1.getPlayerName());
        assertEquals(server.getLobbiesNames(),"Select which lobby you want to join: \n" +
                "0 - Go Back\n" +
                "1 - Pippo [FULL] \n");
    }*/
}
