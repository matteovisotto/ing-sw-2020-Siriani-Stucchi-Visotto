package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.messageModel.ViewMessage;
import it.polimi.ingsw.observer.Observer;
import it.polimi.ingsw.server.ClientConnection;

public class RemoteView extends View {
    private final ClientConnection clientConnection;

    private class MessageReceiver implements Observer<String> {

        @Override
        public void update(String msg) {
            System.out.println("Received: " + msg);
            char c= msg.charAt(0);
            //arg è la stringa ricevuta dall'input del client
            //inserire quindi qui le chiamate ai metodi di view per fare le mosse
            if(c=='0'){
                try{
                    String[] s= msg.substring(1).split(",");
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
    public void update(ViewMessage arg) {
        String resultMsg = "";
        showMessage(resultMsg);
    }

}
