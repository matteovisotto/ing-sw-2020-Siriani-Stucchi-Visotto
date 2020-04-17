package it.polimi.ingsw.model.simplegod;

import it.polimi.ingsw.model.GodCard;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.SimpleGods;

import java.util.List;

public class Pan extends GodCard {

    public Pan(){
        super(SimpleGods.PAN);
    }

    @Override
    public void usePower(List<Object> objectList) {
        Model model=(Model)objectList.get(0);
        Player p=(Player)objectList.get(1);
        model.victory(p);
    }
}
