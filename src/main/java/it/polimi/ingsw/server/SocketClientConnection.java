package it.polimi.ingsw.server;


import it.polimi.ingsw.exceptions.*;
import it.polimi.ingsw.utils.ConnectionMessage;
import it.polimi.ingsw.utils.PlayerMessage;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class SocketClientConnection extends ClientConnection implements Runnable {

    private final Socket socket;
    private ObjectOutputStream out;
    private final Server server;

    private boolean active = true;

    public SocketClientConnection(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
    }

    private synchronized boolean isActive(){
        return active;
    }

    @Override
    public synchronized void send(Object message) {
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
        int numPlayer = 0;
        boolean isConfig = false;
        try{
            in = new Scanner(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
            send(PlayerMessage.WELCOME);
            String read = in.nextLine();
            name = read;
            int choice = 0;
            do {
                do {
                    send(PlayerMessage.GAME_MODE);
                    if(in.hasNextInt())
                        choice = in.nextInt();
                    else
                        in.next();
                } while (choice != 1 && choice != 2);
                if (choice == 1) {
                    do {
                        send(PlayerMessage.ASK_NUM_PLAYER);
                        if(in.hasNextInt()) {
                            numPlayer = in.nextInt();
                        }else
                            in.next();
                    } while (numPlayer < 2 || numPlayer > 3);
                    send(PlayerMessage.ASK_LOBBY_NAME);
                    String lobbyName = in.next();
                    do{
                        send(PlayerMessage.PLAY_MODE);
                        read = in.next();
                        read = read.toLowerCase();
                    }while(!read.equals("y") && !read.equals("n"));
                    boolean simplePlay = false;
                    if (read.equals("y"))
                        simplePlay = true;
                    server.addLobby(lobbyName, this, name, numPlayer, simplePlay);
                    isConfig = true;

                } else {
                    try {
                        send(server.getLobbiesNames());
                        int lobbyId;
                        lobbyId = in.nextInt();
                        if (lobbyId != 0) {
                            server.joinLobby(lobbyId, this, name);
                            isConfig = true;

                        }

                    } catch (FullLobbyException | InvalidLobbyException | NoLobbyException e){
                        send(e.getMessage());
                    } catch (UnavailablePlayerNameException e1){
                        send(e1.getMessage());
                        send(PlayerMessage.WELCOME);
                        name = in.next();
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
