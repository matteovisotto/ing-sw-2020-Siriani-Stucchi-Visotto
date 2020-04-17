package it.polimi.ingsw.model.simplegod;

import it.polimi.ingsw.model.*;

import java.util.List;

public class Pan extends GodCard {
    private final Phase phase=Phase.MOVE;
    public Pan(){
        super(SimpleGods.PAN);
    }

    @Override
    public void usePower(List<Object> objectList) {
        Model model=(Model)objectList.get(0);
        Player p=(Player)objectList.get(1);
        model.victory(p);
    }
    @Override
    public void reset() {

    }
}
