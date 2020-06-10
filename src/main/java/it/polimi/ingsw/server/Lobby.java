package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.GodCardController;
import it.polimi.ingsw.controller.SimpleController;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.Phase;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.messageModel.MessageType;
import it.polimi.ingsw.model.messageModel.ViewMessage;
import it.polimi.ingsw.utils.PlayerMessage;
import it.polimi.ingsw.view.RemoteView;

import java.util.*;

/**
 * This class represent a Lobby that is a room for a single play
 */
public class Lobby {
    private final int numPlayers;
    private final String lobbyName;
    private boolean isFull = false;
    private final List<ClientConnection> connections = new ArrayList<>();
    private final Map<String, ClientConnection> waitingConnection = new LinkedHashMap<>();
    private final boolean simplePlay;
    private boolean isGameEnded = false;

    /**
     * Class constructor
     * @param lobbyName The selected lobby name
     * @param playerName the name of the player who created the lobby
     * @param clientConnection the clientConnection instance of the player
     * @param numPlayers selected number of player
     * @param simplePlay the selected game mode
     */
    public Lobby(String lobbyName, String playerName, ClientConnection clientConnection, int numPlayers, boolean simplePlay){
        this.numPlayers = numPlayers;
        this.lobbyName = lobbyName;
        this.simplePlay = simplePlay;
        connections.add(clientConnection);
        waitingConnection.put(playerName, clientConnection);
        clientConnection.asyncSend(new ViewMessage(MessageType.WAIT_FOR_START, PlayerMessage.WAIT_PLAYERS, Phase.WAIT_PLAYERS));
    }

    /**
     *
     * @return the name of the lobby
     */
    public String getLobbyName(){
        return this.lobbyName;
    }

    /**
     *
     * @return an int for the number of the player
     */
    public int getNumPlayers(){
        return this.numPlayers;
    }

    /**
     *
     * @return false if the lobby have space for other players
     */
    public boolean isFull(){
        return this.isFull;
    }

    /**
     *
     * @return true if has being selected the simpla mode
     */
    public boolean isSimplePlay(){
        return this.simplePlay;
    }

    /**
     * This method remove all the players from the lobby and close their connections
     */
    public void closeLobby() {
        if(!isGameEnded) {
            for (int i = connections.size() - 1; i >= 0; i--) {
                ClientConnection clientConnection = connections.get(i);
                clientConnection.closeConnection();
                connections.remove(i);
            }
            waitingConnection.clear();
        }
    }

    /**
     * Set that the play for this lobby is ended
     */
    public void setEndGame(){
        this.isGameEnded = true;
    }

    /**
     * Set isEndGame to false
     */
    public void resetEndGame(){
        this.isGameEnded = false;
    }

    /**
     * This method is used to add a player to the lobby
     * @param playerName the name of the new player
     * @param clientConnection the ClientConnection instance of the new player
     *
     *  When the lobby is full is called a methid to configure the beginning of the game:
     *   twoPlayer if this.numPlayer==2 else threePlayer
     *
     *  If the lobby is not already full a wait message is sent to client
     */
    public void addPlayer(String playerName, ClientConnection clientConnection) {
        connections.add(clientConnection);
        waitingConnection.put(playerName, clientConnection);

        if(waitingConnection.size() == this.numPlayers){    //appena si riempie la stanza
            this.isFull = true;
            List<String> players = new ArrayList<>(waitingConnection.keySet());
            waitingConnection.clear();
            if(this.numPlayers == 2){
                twoPlayer(players);
            } else {
                threePlayer(players);
            }

        } else {
            clientConnection.asyncSend(new ViewMessage(MessageType.WAIT_FOR_START, PlayerMessage.WAIT_PLAYERS, Phase.WAIT_PLAYERS));
        }
    }

    /**
     * This method send the same message to all player of the lobby.
     * Is used only before creating model and controller
     * @param o an object to send throw network
     */
    private void sendAllPlayer(Object o){
        for (ClientConnection connection : connections) {
            connection.send(o);
        }
    }

    /**
     * This method check if the name of the new player already exists in the lobby
     * @param name the name chosen by the player
     * @return true is the name doesn't exist
     */
    public boolean isPlayerNameAvailable (String name) {
        return waitingConnection.get(name) == null;
    }

    /**
     * This method configure a play for two players
     * @param players an array list containing the names of the player who joined the lobby
     * For each player it create a remove view instance with his connection, the instance of the lobby,
     *                his own player object and the name of the opponents.
     *
     * Then a new model object is created and also a controller: SimpleController if in a simple play
     *              or a GodCardController otherwise
     */
    private void twoPlayer(List<String> players){
        ClientConnection c1,c2;
        Player player1,player2;
        Player[] playerArray = new Player[this.numPlayers];
        RemoteView remoteView1,remoteView2;

        c1 = connections.get(0);
        c2 = connections.get(1);
        player1 = new Player(players.get(0));
        player2 = new Player(players.get(1));
        playerArray[0] = player1;
        playerArray[1] = player2;

        remoteView1 = new RemoteView(player1, players.get(1), c1, this);
        remoteView2 = new RemoteView(player2, players.get(0), c2, this);

        Model model = new Model(playerArray, simplePlay);
        Controller controller;
        if(simplePlay){
            controller = new SimpleController(model);
        }
        else{
            controller=new GodCardController(model);
        }

        model.addObserver(remoteView1);
        model.addObserver(remoteView2);
        remoteView1.addObserver(controller);
        remoteView2.addObserver(controller);

        sendAllPlayer(PlayerMessage.START_PLAY);

        model.initialize();

    }

    /**
     * This method configure a play for three players
     * @param players an array list containing the names of the player who joined the lobby
     * For each player it create a remove view instance with his connection, the instance of the lobby,
     *                his own player object and the name of the opponents.
     *
     * Then a new model object is created and also a controller: SimpleController if in a simple play
     *              or a GodCardController otherwise
     */
    private void threePlayer(List<String> players) {
        ClientConnection c1, c2, c3;
        Player player1, player2, player3;
        Player[] playerArray = new Player[this.numPlayers];
        RemoteView remoteView1, remoteView2, remoteView3;

        c1 = connections.get(0);
        c2 = connections.get(1);
        c3 = connections.get(2);
        player1 = new Player(players.get(0));
        player2 = new Player(players.get(1));
        player3 = new Player(players.get(2));


        playerArray[0] = player1;
        playerArray[1] = player2;
        playerArray[2] = player3;

        remoteView1 = new RemoteView(player1, players.get(1), players.get(2), c1, this);
        remoteView2 = new RemoteView(player2, players.get(0), players.get(2), c2, this);
        remoteView3 = new RemoteView(player3, players.get(1), players.get(0), c3, this);


        Model model = new Model(playerArray, simplePlay);
        Controller controller;
        if(simplePlay){
            controller = new SimpleController(model);
        }
        else{
            controller=new GodCardController(model);
        }
        model.addObserver(remoteView1);
        model.addObserver(remoteView2);
        model.addObserver(remoteView3);

        remoteView1.addObserver(controller);
        remoteView2.addObserver(controller);
        remoteView3.addObserver(controller);

        sendAllPlayer(PlayerMessage.START_PLAY);

        model.initialize();

    }

}
