package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Worker;
import it.polimi.ingsw.view.View;

import java.util.Observable;
import java.util.Observer;

public class Controller implements Observer {

    private Model model;

    public Controller(Model model){
        this.model = model;

    }

    public void move(int playerId, int workerId, Cell newCell) {
        try {
            Worker worker = model.getPlayer(playerId).getWorker(workerId);
            worker.setCell(newCell);

        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println(e.getMessage());
        }

    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
