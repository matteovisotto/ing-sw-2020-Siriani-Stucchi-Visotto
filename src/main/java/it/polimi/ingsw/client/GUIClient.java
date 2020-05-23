package it.polimi.ingsw.client;

import it.polimi.ingsw.client.GUI.Initialization;
import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.messageModel.ViewMessage;
import it.polimi.ingsw.observer.Observable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class GUIClient extends Observable<ViewMessage> {
    private final String ip;
    private final int port;
    private boolean active = true;
    private Initialization initialization;
    private PrintWriter socketOut;

    public GUIClient(String ip, int port){
        this.ip = ip;
        this.port = port;
        initialization= new Initialization(this);
        addObserver(initialization);
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
                        if(inputObject instanceof String) {//se viene passata una stringa
                            System.out.println((String) inputObject);
                        } else if (inputObject instanceof ViewMessage){
                            notifyObservers((ViewMessage) inputObject);
                        } else if (inputObject instanceof Board) { // se viene passata una board
                            ((Board) inputObject).print();
                        } else if (inputObject instanceof HashMap) { //se viene passata una HashMap
                            //Print movable cell
                        } else {
                            throw new IllegalArgumentException();
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
        initialization.setVisible(true);
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
