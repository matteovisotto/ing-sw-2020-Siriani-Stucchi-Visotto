package it.polimi.ingsw.model.simplegod;

import it.polimi.ingsw.model.*;

import java.util.List;

public class Minotaur extends GodCard {
    private final Phase phase=Phase.MOVE;
    public Minotaur() {
        super(SimpleGods.MINOTAUR);
    }

    public Phase getPhase() {
        return phase;
    }

    @Override
    public void usePower(List<Object> objectList) {
        Worker w1=(Worker)objectList.get(0);
        Worker w2=(Worker)objectList.get(1);
        Cell c=(Cell)objectList.get(2);

        w1.setCell(w2.getCell());
        w2.setCell(c);
    }
    @Override
    public void reset() {

    }
}
