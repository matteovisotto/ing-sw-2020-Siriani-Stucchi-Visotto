package it.polimi.ingsw.model.simplegod;

import it.polimi.ingsw.model.*;

import java.util.List;

public class Apollo extends GodCard {

    public Apollo(){
        super(SimpleGods.APOLLO);
    }


    @Override
    public void usePower(List<Object> objectList) {
        Worker w1=(Worker)objectList.get(0);
        Worker w2=(Worker)objectList.get(1);
        Cell c;
        //scambia le celle dei worker
        c=w2.getCell();
        w2.setCell(w1.getCell());
        w1.setCell(c);
    }
}
