package it.polimi.ingsw.model.simplegod;

import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.GodCard;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.SimpleGods;
import it.polimi.ingsw.model.messageModel.PlayerMove;

import java.util.List;

public class Arthemis extends GodCard {

    public Arthemis() {
        super(SimpleGods.ARTHEMIS);
    }

    @Override
    public void usePower(List<Object> objectList) {
        Model model=(Model)objectList.get(0);
        model.move((PlayerMove)objectList.get(1));
    }
}
