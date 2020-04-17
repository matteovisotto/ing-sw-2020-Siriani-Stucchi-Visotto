package it.polimi.ingsw.model.simplegod;

import it.polimi.ingsw.model.GodCard;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.SimpleGods;
import it.polimi.ingsw.model.messageModel.PlayerMove;

import java.util.List;

public class Demeter extends GodCard {

    public Demeter() {
        super(SimpleGods.DEMETER);
    }

    @Override
    public void usePower(List<Object> objectList) {
        Model model=(Model)objectList.get(0);
        model.move((PlayerMove)objectList.get(1));

    }
}
