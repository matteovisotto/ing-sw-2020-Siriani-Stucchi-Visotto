package it.polimi.ingsw.model.simplegod;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.messageModel.PlayerMove;

import java.util.List;

public class Demeter extends GodCard {
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

    @Override
    public void usePower(List<Object> objectList) {
        Model model=(Model)objectList.get(0);
        Worker w=(Worker)objectList.get(1);
        model.increaseLevel( w.getCell(), Blocks.getBlock(w.getCell().getLevel().getBlockId()+1));
    }
}
