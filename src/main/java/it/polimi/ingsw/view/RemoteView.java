package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Phase;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.messageModel.GameMessage;
import it.polimi.ingsw.model.messageModel.MessageType;
import it.polimi.ingsw.model.messageModel.ViewMessage;
import it.polimi.ingsw.observer.Observer;
import it.polimi.ingsw.server.ClientConnection;
import it.polimi.ingsw.utils.CommandParser;

public class RemoteView extends View {
    private final ClientConnection clientConnection;
    private Phase phase=Phase.SETWORKER1;
    private class MessageReceiver implements Observer<String> {

        @Override
        public void update(String msg) {//questa riceve dal client
            //arg è la stringa ricevuta dall'input del client
            //inserire quindi qui le chiamate ai metodi di view per fare le mosse

            CommandParser commandParser=new CommandParser(phase, msg, getPlayer(), RemoteView.this);
            try{
                doAction(commandParser.parse());
            }
            catch(IllegalArgumentException e){
                e.printStackTrace();
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
    public void update(ViewMessage arg) {//questa riceve dal model
        phase=arg.getPhase();
        if(arg instanceof GameMessage) {
            GameMessage gameMessage = (GameMessage) arg;
            handleTurnMessage(gameMessage, gameMessage.getPlayer());
        } else {
            showMessage(arg);
        }

    }

    private void handleTurnMessage(ViewMessage arg, Player player) {
        if (this.getPlayer() == player) {
            showMessage(arg);
        } else if ((phase == Phase.MOVE || phase == Phase.SETWORKER1 || phase == Phase.DRAWCARD) && this.getPlayer() != player) {
            showMessage(new ViewMessage(MessageType.OPPONENT_TURN, "Wait for your turn", arg.getPhase()));
        }
    }

}
