package it.polimi.ingsw.client;

import it.polimi.ingsw.client.GUI.Game;
import it.polimi.ingsw.client.GUI.Initialization;
import it.polimi.ingsw.model.messageModel.ClientConfigurator;
import it.polimi.ingsw.observer.Observable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class GUIClient extends Observable<Object> {
    private final String ip;
    private final int port;
    private boolean active = true;
    private final Initialization initialization;
    private final Game game;
    private PrintWriter socketOut;
    private boolean isConfig = false;

    public GUIClient(String ip, int port){
        this.ip = ip;
        this.port = port;
        initialization= new Initialization(this);
        game = new Game(this);
        addObserver(initialization);
        addObserver(game);
    }

    public synchronized void openInitializator() {
        initialization.setVisible(true);
    }

    public synchronized void closeInitializator() {
        game.setEnabled(true);
        this.isConfig = true;
    }

    public boolean isConfig() {
        return isConfig;
    }

    public synchronized boolean isActive(){
        return active;
    }

    public synchronized void setActive(boolean active){
        this.active = active;
    }
    //Aggiorna la tabella quando chiamata
    public Thread asyncReadFromSocket(final ObjectInputStream socketIn){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (isActive()) {
                        Object inputObject = socketIn.readObject();
                        if(inputObject instanceof ClientConfigurator) {//Configurator for client data
                            ClientConfigurator clientConfigurator = (ClientConfigurator) inputObject;
                            game.setPlayer(clientConfigurator.getMyself());
                            game.setClientConfigurator(clientConfigurator);
                        } else {
                            notifyObservers(inputObject);
                        }
                    }
                } catch (Exception e){
                    setActive(false);
                }
            }
        });
        t.start();
        return t;
    }
    //invia messaggi al server quando qualcosa nella view muta
    public Thread asyncWriteToSocket(final Scanner stdin, final PrintWriter socketOut){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (isActive()) {
                        String inputLine = stdin.nextLine();
                        socketOut.println(inputLine);
                        socketOut.flush();
                    }
                }catch(Exception e){
                    setActive(false);
                }
            }
        });
        t.start();
        return t;
    }

    public synchronized void send(String s){
        socketOut.println(s);
        socketOut.flush();
    }

    public void run() throws IOException {
        Socket socket = new Socket(ip, port);
        System.out.println("Connection established");
        ObjectInputStream socketIn = new ObjectInputStream(socket.getInputStream());
        socketOut = new PrintWriter(socket.getOutputStream());
        Scanner stdin = new Scanner(System.in);
        game.setVisible(true);
        try{
            Thread t0 = asyncReadFromSocket(socketIn);
            Thread t1 = asyncWriteToSocket(stdin, socketOut);
            t0.join();
            t1.join();//non dovrebbe fare qualcosa?
        } catch(InterruptedException | NoSuchElementException e){
            System.out.println("Connection closed from the client side");
        } finally {
            stdin.close();
            socketIn.close();
            socketOut.close();
            socket.close();
        }
    }
}
