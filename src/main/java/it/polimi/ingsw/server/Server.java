package it.polimi.ingsw.server;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.RemoteView;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static final int PORT= 12345;
    private ServerSocket serverSocket;

    private ExecutorService executor = Executors.newCachedThreadPool();

    private List<ClientConnection> connections = new ArrayList<ClientConnection>();
    private Map<String, ClientConnection> waitingConnection = new HashMap<>();
    private Map<ClientConnection, ClientConnection> playingConnection = new HashMap<>();

    private int numPlayer = 0;

    //Register connection
    private synchronized void registerConnection(ClientConnection c){
        connections.add(c);
    }

    //Deregister connection
    public synchronized void deregisterConnection(ClientConnection c){
        connections.remove(c);
        ClientConnection opponent = playingConnection.get(c);
        if(opponent != null){
            opponent.closeConnection();
            playingConnection.remove(c);
            playingConnection.remove(opponent);

        }
    }

    public synchronized int getLobbyWaiter(){
        return waitingConnection.size();
    }

    public synchronized void lobby(ClientConnection c, String name, int numPlayer){
        if(numPlayer!=0) this.numPlayer = numPlayer;
        waitingConnection.put(name, c);                                     //aggiungo chi ha effettuato l'accesso alla lista dei giocatori in attesa
        if(waitingConnection.size() == this.numPlayer){                          //appena si riempie la stanza
            ClientConnection c1,c2,c3=null;
            Player p1,p2,p3;
            Player[] playerArray = new Player[this.numPlayer];
            RemoteView rv1,rv2,rv3=null;
            List<String> keys = new ArrayList<>(waitingConnection.keySet());//creo un array di stringhe contenente i nomi dei giocatori
            //se si è in due o in 3
            c1 = waitingConnection.get(keys.get(0));
            c2 = waitingConnection.get(keys.get(1));
            p1 = new Player(keys.get(0));
            p2 = new Player(keys.get(1));
            playerArray[0] = p1;
            playerArray[1] = p2;
            //se si è in 3
            if(this.numPlayer==3){                          //se si è scelto di giocare con 3 giocatori
                c3 = waitingConnection.get(keys.get(2));    //stesse operazioni fatte sopra
                p3 = new Player(keys.get(2));
                playerArray[2] = p3;
                rv1 = new RemoteView(p1, keys.get(1), keys.get(2), c1);
                rv2 = new RemoteView(p2, keys.get(0), keys.get(2), c2);
                rv3 = new RemoteView(p3, keys.get(1), keys.get(0), c3);
            } else {
                rv1 = new RemoteView(p1, keys.get(1), c1);
                rv2 = new RemoteView(p2, keys.get(0), c2);
            }
            Model model = new Model(playerArray);
            Controller controller = new Controller(model);
            model.addObserver(rv1);
            model.addObserver(rv2);
            rv1.addObserver(controller);
            rv2.addObserver(controller);
            if(numPlayer==3){
                model.addObserver(rv3);
                rv3.addObserver(controller);
                playingConnection.put(c1, c2);
                playingConnection.put(c2, c3);
                playingConnection.put(c3, c1);
            }
            else{
                playingConnection.put(c1, c2);
                playingConnection.put(c2, c1);
            }
            waitingConnection.clear();
            this.numPlayer=0;
            //Gestione turni
        }

    }

    public Server() throws IOException {
        this.serverSocket = new ServerSocket(PORT);
    }

    //non dovrebbe essere @override?
    public void run(){
        System.out.println("Server listening on port: " + PORT);
        while(true){
            try {
                Socket socket = serverSocket.accept();
                SocketClientConnection connection = new SocketClientConnection(socket, this);
                registerConnection(connection);
                executor.submit(connection);
            } catch (IOException e){
                System.err.println("Connection error!");
            }
        }
    }

}
