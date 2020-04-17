package it.polimi.ingsw.model.simplegod;

import it.polimi.ingsw.model.*;

import java.util.List;

public class Prometheus extends GodCard {
    private boolean built=false;
    private final Phase phase=Phase.BEGINNING;
    public Prometheus() {
        super(SimpleGods.PROMETHEUS);
    }

    public boolean hasBuilt() {
        return built;
    }


    @Override
    public void usePower(List<Object> objectList) {
        Model model=(Model)objectList.get(0);
        Worker w=(Worker)objectList.get(1);
        model.increaseLevel( w.getCell(), Blocks.getBlock(w.getCell().getLevel().getBlockId()+1));
        built=true;
    }
    @Override
    public void reset() {
        built=false;
    }
}
