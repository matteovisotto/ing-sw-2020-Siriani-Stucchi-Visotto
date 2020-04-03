package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.server.ClientConnection;

import java.util.Observable;
import java.util.Observer;

public class RemoteView extends View {
    private final ClientConnection clientConnection;

    private class MessageReceiver implements Observer {

        @Override
        public void update(Observable o, Object arg) {
            System.out.println("Received: " + arg);
            char c=((String) arg).charAt(0);
            //arg è la stringa ricevuta dall'input del client
            //inserire quindi qui le chiamate ai metodi di view per fare le mosse
            if(c=='0'){
                try{
                    String[] s=((String) arg).substring(1).split(",");
                    placeWorker(Integer.parseInt(s[0]), Integer.parseInt(s[1]));
                }
                catch (IllegalArgumentException e){
                    clientConnection.asyncSend("Wrong input");
                }
            }

        }

    }

    //Constructor for 2 players
    public RemoteView(Player player, String opponent, ClientConnection c) {
        super(player);
        this.clientConnection = c;
        c.addObserver(new MessageReceiver());
        c.asyncSend("Your opponent is: " + opponent);

    }

    //Constructor for 3 players
    public RemoteView(Player player, String opponent1, String opponent2, ClientConnection c) {
        super(player);
        this.clientConnection = c;
        c.addObserver(new MessageReceiver());
        c.asyncSend("Your opponents are: " + opponent1 + " and " + opponent2);
    }

    @Override
    protected void showMessage(Object message) {
        clientConnection.asyncSend(message);
    }

    @Override
    public void update(Observable o, Object arg) {
        String resultMsg = "";
        showMessage(resultMsg);
    }

}
