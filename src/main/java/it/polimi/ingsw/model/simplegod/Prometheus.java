package it.polimi.ingsw.model.simplegod;

import it.polimi.ingsw.model.*;

import java.util.List;

public class Prometheus extends GodCard {
    private boolean built = false;
    private final Phase phase = Phase.BEGINNING;
    private Cell cell;
    public Prometheus() {
        super(SimpleGods.PROMETHEUS);
    }

    public boolean hasBuilt() {
        return built;
    }

    public Cell getFirstBuilt(){
        return cell;
    }

    public void setFirstBuilt(Cell firstBuilt){

    }

    public Phase getPhase() {
        return phase;
    }

    @Override
    public void usePower(List<Object> objectList) {
        Model model = (Model)objectList.get(0);
        Worker worker = (Worker)objectList.get(1);
        model.increaseLevel(worker.getCell(), Blocks.getBlock(worker.getCell().getLevel().getBlockId()+1));
        built = true;
    }
    @Override
    public void reset() {
        built=false;
    }
}
