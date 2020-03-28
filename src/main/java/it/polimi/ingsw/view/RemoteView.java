package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.server.ClientConnection;

import java.util.Observable;
import java.util.Observer;

public class RemoteView extends View {
    private ClientConnection clientConnection;


    private class MessageReceiver implements Observer {

        @Override
        public void update(Observable o, Object arg) {
            System.out.println("Received: " + arg);
        }

    }

    public RemoteView(Player player, String opponent, ClientConnection c) {
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
    public void update(Observable o, Object arg)
    {
        showMessage(arg);
        String resultMsg = "";
        showMessage(resultMsg);
    }

}
