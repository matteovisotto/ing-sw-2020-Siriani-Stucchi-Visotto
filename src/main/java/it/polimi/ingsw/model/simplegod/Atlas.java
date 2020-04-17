package it.polimi.ingsw.model.simplegod;

import it.polimi.ingsw.model.*;

import java.util.List;

public class Atlas extends GodCard {
    private final Phase phase=Phase.BUILD;
    public Atlas() {
        super(SimpleGods.ATLAS);
    }

    @Override
    public void usePower(List<Object> objectList) {
        Worker w= (Worker)objectList.get(0);
        w.getCell().setLevel(Blocks.DOME);
    }
    @Override
    public void reset() {

    }
}
