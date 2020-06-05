package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Phase;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.messageModel.ClientConfigurator;
import it.polimi.ingsw.model.messageModel.ViewMessage;
import it.polimi.ingsw.observer.Observer;
import it.polimi.ingsw.server.ClientConnection;
import it.polimi.ingsw.server.Lobby;
import it.polimi.ingsw.utils.CommandParser;

import java.util.ArrayList;

public class RemoteView extends View {
    private final ClientConnection clientConnection;
    private Phase phase = Phase.SETWORKER1;

    public Lobby getLobby() {
        return lobby;
    }

    private final Lobby lobby;
    private class MessageReceiver implements Observer<String> {

        @Override
        public void update(String msg) {//questa riceve dal client
            //arg è la stringa ricevuta dall'input del client
            //inserire quindi qui le chiamate ai metodi di view per fare le mosse

            CommandParser commandParser = new CommandParser(phase, msg, getPlayer(), RemoteView.this);
            try{
                doAction(commandParser.parse());
            } catch(NumberFormatException e){
                reportError("Please insert numbers only");
            } catch(IndexOutOfBoundsException | IllegalArgumentException e){
                reportError(e.getMessage());
            }

        }
    }

    //Constructor for 2 players
    public RemoteView(Player player, String opponent, ClientConnection c, Lobby lobby) {
        super(player);
        this.clientConnection = c;
        MessageReceiver messageReceiver = new MessageReceiver();
        c.addObserver(messageReceiver);
        c.removeExcept(messageReceiver);
        c.asyncSend("Your opponent is: " + opponent);
        this.lobby = lobby;
        ArrayList<String> opponents = new ArrayList<>();
        opponents.add(opponent);
        clientConnection.asyncSend(new ClientConfigurator(2, opponents, player));
    }

    //Constructor for 3 players
    public RemoteView(Player player, String opponent1, String opponent2, ClientConnection c, Lobby lobby) {
        super(player);
        this.clientConnection = c;
        MessageReceiver messageReceiver = new MessageReceiver();
        c.addObserver(messageReceiver);
        c.removeExcept(messageReceiver);
        c.asyncSend("Your opponents are: " + opponent1 + " and " + opponent2);
        this.lobby = lobby;
        ArrayList<String> opponents = new ArrayList<>();
        opponents.add(opponent1);
        opponents.add(opponent2);
        clientConnection.asyncSend(new ClientConfigurator(3, opponents, player));
    }

    @Override
    protected void showMessage(Object message) {
        clientConnection.send(message);
    }

    @Override
    public void update(ViewMessage arg) {//questa riceve dal model
        phase = arg.getPhase();
        showMessage(arg);

    }



    public ClientConnection getConnection(){
        return this.clientConnection;
    }

}
