package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.model.messageModel.ViewMessage;
import it.polimi.ingsw.observer.Observer;

import javax.swing.*;

public class Game extends JFrame implements Observer<ViewMessage> {

    public Game(){
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Santorini");

    }

    @Override
    public void update(ViewMessage msg) {

    }
}
