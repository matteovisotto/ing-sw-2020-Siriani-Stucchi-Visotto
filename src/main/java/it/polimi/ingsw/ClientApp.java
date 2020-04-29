package it.polimi.ingsw;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.GUIClient;

import java.io.IOException;
import java.util.Scanner;

public class ClientApp
{
    public static void main(String[] args){
        char c;
        Scanner in=new Scanner(System.in);
        do{
            System.out.println("Would you like to use the graphic interface?(y/n)");
            c=in.nextLine().toLowerCase().charAt(0);
        }while(c!='y' && c!='n');

        try{
            if(c=='n'){
                Client client = new Client("127.0.0.1", 12345);
                client.run();
            }
            else{
                GUIClient client = new GUIClient("127.0.0.1", 12345);
                client.run();
            }
        }catch (IOException e){
            System.err.println(e.getMessage());
        }
    }
}
