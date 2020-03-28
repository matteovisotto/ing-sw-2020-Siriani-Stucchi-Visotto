package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.server.Connection;

import java.util.Observer;

public class RemoteView extends View {
    private ClientConnection clientConnection;


    private class MessageReceiver implements Observer {

        @Override
        public void update(String message) {
            System.out.println("Received: " + message);
        }

    }

    public RemoteView(Player player, String opponent, Connection c) {
        super(player);
        this.clientConnection = c;
        c.addObserver(new MessageReceiver());
        c.asyncSend("Your opponent is: " + opponent);

    }

    @Override
    protected void showMessage(Object message) {
        clientConnection.asyncSend(message);
    }

    @Override
    public void update(MoveMessage message)
    {
        showMessage(message.getBoard());
        String resultMsg = "";


        showMessage(resultMsg);
    }

}
