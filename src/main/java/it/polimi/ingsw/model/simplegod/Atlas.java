package it.polimi.ingsw.model.simplegod;

import it.polimi.ingsw.model.*;

import java.util.List;

public class Atlas extends GodCard {
    private final Phase phase = Phase.BUILD;
    public Atlas() {
        super(SimpleGods.ATLAS);
    }
    private Cell cell;

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
        Cell cellBuild = (Cell)objectList.get(0);
        cellBuild.setLevel(Blocks.DOME);
    }
    @Override
    public void reset() {

    }
}
