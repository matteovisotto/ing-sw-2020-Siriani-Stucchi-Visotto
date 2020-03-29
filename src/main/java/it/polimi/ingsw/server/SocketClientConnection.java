package it.polimi.ingsw.server;


import it.polimi.ingsw.exceptions.FullLobbyException;
import it.polimi.ingsw.exceptions.InvalidLobbyException;
import it.polimi.ingsw.exceptions.LobbyException;
import it.polimi.ingsw.exceptions.NoLobbyException;
import it.polimi.ingsw.utils.ConnectionMessage;
import it.polimi.ingsw.utils.PlayerMessage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Observable;
import java.util.Scanner;

public class SocketClientConnection extends ClientConnection implements Runnable {

    private Socket socket;
    private ObjectOutputStream out;
    private Server server;

    private boolean active = true;

    public SocketClientConnection(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    private synchronized boolean isActive(){
        return active;
    }

    private synchronized void send(Object message) {
            try {
                out.reset();
                out.writeObject(message);
                out.flush();
            } catch(IOException e){
                System.err.println(e.getMessage());
            }

    }

    @Override
    public synchronized void closeConnection() {
        send(ConnectionMessage.CLOSE_SOCKET);
        try {
            socket.close();
        } catch (IOException e) {
            System.err.println(ConnectionMessage.CLOSE_SOCKET_ERROR);
        }
        active = false;
    }

    private void close() {
        closeConnection();
        System.out.println("Deregistering client...");
        server.deregisterConnection(this);
        System.out.println("Done!");
    }

    @Override
    public void asyncSend(final Object message){
        new Thread(new Runnable() {
            @Override
            public void run() {
                send(message);
            }
        }).start();
    }

    @Override
    public void run() {
        Scanner in;
        String name;
        int numPlayer=0;
        boolean isConfig = false;
        try{
            in = new Scanner(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
            send(PlayerMessage.WELCOME);
            String read = in.nextLine();
            name = read;
            int choice=0 ;
            do {
                do {
                    send(PlayerMessage.GAME_MODE);

                    choice = in.nextInt();

                } while (choice != 1 && choice != 2);
                if (choice == 1) {
                    do {
                        send(PlayerMessage.ASK_NUM_PLAYER);
                        numPlayer = in.nextInt();
                    } while (numPlayer < 2 || numPlayer > 3);
                    send(PlayerMessage.ASK_LOBBY_NAME);
                    String lobbyName = in.next();
                    server.addLobby(lobbyName, this, name, numPlayer);
                    isConfig = true;

                } else {
                    try {
                        send(PlayerMessage.JOIN_LOBBY);
                        send(server.getLobbiesNames());
                        int lobbyId;
                        lobbyId = in.nextInt();
                        if (lobbyId != 0) {
                            server.joinLobby(lobbyId, this, name);
                            isConfig = true;

                        }

                    } catch (LobbyException e){
                        send(e.getMessage());
                    }

                }
            }while (!isConfig);


            while(isActive()){
                read = in.nextLine();
                notifyObservers(read);
            }
        } catch (IOException | NoSuchElementException e) {
            System.err.println("Error!" + e.getMessage());
        }finally{
            close();
        }
    }
}
