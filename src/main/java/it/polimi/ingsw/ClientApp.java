package it.polimi.ingsw;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.client.GUIClient;

import java.io.IOException;
import java.util.Scanner;

public class ClientApp
{
    public static void main(String[] args){
        char graphicInterface = 'a';
        Scanner in = new Scanner(System.in);
        /*do{
            try{
                System.out.println("Would you like to use the graphic interface?(y/n)");
                graphicInterface = in.nextLine().toLowerCase().charAt(0);
            }catch (StringIndexOutOfBoundsException e){

            }
        }while(graphicInterface != 'y' && graphicInterface != 'n');
*/
        try{
            if(args.length!=0){
                if(args[0].equals("-cli")){
                    Client client = new Client("127.0.0.1", 12345);
                    client.run();
                }
                else{
                    System.out.println("Wrong args");
                }
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
