package it.polimi.ingsw;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.View;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main(String [] args){
        Model model = new Model(new Player[2]);
        View view = new View();
        Controller controller = new Controller(model);
        view.addObserver(controller);
        model.addObserver(view);
        view.run();
    }
}
