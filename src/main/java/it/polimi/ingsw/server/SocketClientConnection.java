package it.polimi.ingsw.server;


import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
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
        send("Connection closed!");
        try {
            socket.close();
        } catch (IOException e) {
            System.err.println("Error when closing socket!");
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
        try{
            in = new Scanner(socket.getInputStream());
            out = new ObjectOutputStream(socket.getOutputStream());
            send("Welcome!\nWhat is your name?");
            String read = in.nextLine();
            name = read;
            do{
                send("How many players?");
                numPlayer=in.nextInt();
            }while(numPlayer<2 || numPlayer>3);
            server.lobby(this, name, numPlayer);

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
