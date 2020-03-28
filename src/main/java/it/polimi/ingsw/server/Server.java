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

    public synchronized void lobby(ClientConnection c, String name, int numPlayer){
        waitingConnection.put(name, c);
        if(waitingConnection.size() == numPlayer){
            List<String> keys = new ArrayList<>(waitingConnection.keySet());
            ClientConnection c1 = waitingConnection.get(keys.get(0));
            ClientConnection c2 = waitingConnection.get(keys.get(1));
            Player p1 = new Player(keys.get(0));
            Player p2 = new Player(keys.get(1));
            Player[] playerArray = new Player[numPlayer];
            playerArray[0] = p1;
            playerArray[1] = p2;
            RemoteView rv1 = new RemoteView(p1, keys.get(1), c1);
            RemoteView rv2 = new RemoteView(p2, keys.get(0), c2);
            Model model = new Model(playerArray);
            Controller controller = new Controller(model);
            model.addObserver(rv1);
            model.addObserver(rv2);
            rv1.addObserver(controller);
            rv2.addObserver(controller);
            playingConnection.put(c1, c2);
            playingConnection.put(c2, c1);
            waitingConnection.clear();
        }
        //Gestione turni

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
