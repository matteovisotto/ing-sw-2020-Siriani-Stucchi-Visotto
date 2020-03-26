package it.polimi.ingsw.view;

import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

public class View extends Observable implements Runnable, Observer {

    private Scanner in = new Scanner(System.in);
    private int players;

    @Override
    public void run() {
        boolean end=false;
        while(end != true){
            try{
                do{
                    System.out.println("How many players?");
                    players=in.nextInt();
                }while(players>3 && players<2);
            }
            catch (Exception e){
                System.out.println("Insert a valid number");
            }
            //chiedere nome per ogni giocatore e farli pescare
            //Turno giocatore 1

            //Eseguire la mossa


        }



    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
