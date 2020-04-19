package it.polimi.ingsw.model.simplegod;

import it.polimi.ingsw.model.*;

import java.util.List;

public class Athena extends GodCard {
    private final Phase phase=Phase.BUILD;
    private Cell cell;
    public Athena() {
        super(SimpleGods.ATHENA);
    }

    public Phase getPhase() {
        return phase;
    }

    public Cell getFirstBuilt(){
        return cell;
    }

    public void setFirstBuilt(Cell firstBuilt){

    }

    @Override
    public void usePower(List<Object> objectList) {

    }
    @Override
    public void reset() {

    }
}
