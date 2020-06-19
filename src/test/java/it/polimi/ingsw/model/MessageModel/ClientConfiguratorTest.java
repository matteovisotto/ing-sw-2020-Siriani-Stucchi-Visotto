package it.polimi.ingsw.model.MessageModel;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.messageModel.ClientConfigurator;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;

public class ClientConfiguratorTest {
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
}
