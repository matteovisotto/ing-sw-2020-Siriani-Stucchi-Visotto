package it.polimi.ingsw.model.simplegod;

import it.polimi.ingsw.model.GodCard;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.Phase;
import it.polimi.ingsw.model.SimpleGods;

import java.util.List;

public class Athena extends GodCard {
    private final Phase phase=Phase.BUILD;
    public Athena() {
        super(SimpleGods.ATHENA);
    }

    public Phase getPhase() {
        return phase;
    }

    @Override
    public void usePower(List<Object> objectList) {

    }
    @Override
    public void reset() {

    }
}
