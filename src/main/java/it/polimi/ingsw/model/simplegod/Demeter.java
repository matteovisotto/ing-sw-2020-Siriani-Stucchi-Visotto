package it.polimi.ingsw.model.simplegod;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.messageModel.PlayerMove;

import java.util.List;

public class Demeter extends GodCard {
    private final Phase phase = Phase.BUILD;
    private Cell firstBuilt;
    public Demeter() {
        super(SimpleGods.DEMETER);
    }

    public Cell getFirstBuilt() {
        return firstBuilt;
    }

    public void setFirstBuilt(Cell firstBuilt) {
        this.firstBuilt = firstBuilt;
    }

    public Phase getPhase() {
        return phase;
    }

    @Override
    public void usePower(List<Object> objectList) {
        Model model = (Model)objectList.get(0);
        Cell cell = (Cell)objectList.get(1);
        model.increaseLevel(cell, Blocks.getBlock(cell.getLevel().getBlockId()+1)); //mi sa che va meglio se si implementa con PlayerBuilt
    }
    @Override
    public void reset() {

    }
}
