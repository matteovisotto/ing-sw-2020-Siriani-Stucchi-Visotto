package it.polimi.ingsw;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.GUIClient;

import java.io.IOException;
import java.util.Scanner;

public class ClientApp
{
    public static void main(String[] args){
        try{
            if(args.length!=0){
                if(args[0].equals("-cli")){
                    //Client client = new Client("93.43.230.177", 15986);
                    Client client = new Client("127.0.0.1", 15986);
                    client.run();
                }
                else{
                    System.out.println("Wrong args");
                }
            }
            else{
                //GUIClient client = new GUIClient("93.43.230.177", 15986);
                GUIClient client = new GUIClient("127.0.0.1", 15986);
                client.run();
            }
        }catch (IOException e){
            System.err.println(e.getMessage());
        }
    }
}
