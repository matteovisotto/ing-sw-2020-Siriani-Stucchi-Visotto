package it.polimi.ingsw.model.simplegod;

import it.polimi.ingsw.model.*;

import java.util.List;

public class Minotaur extends GodCard {
    private final Phase phase = Phase.MOVE;
    private boolean moved = false;
    private boolean built = false;
    private Cell cell;
    public Minotaur() {
        super(SimpleGods.MINOTAUR);
    }

    public Phase getPhase() {
        return phase;
    }

    public Cell getFirstBuilt(){
        return cell;
    }

    public void setFirstBuilt(Cell firstBuilt){

    }
    public boolean isMoved() {
        return moved;
    }

    public void hasMoved(boolean moved) {

    }

    public boolean hasBuilt() {
        return built;
    }

    @Override
    public void usePower(List<Object> objectList) {
        Worker worker1 = (Worker)objectList.get(0);
        Worker worker2 = (Worker)objectList.get(1);
        Cell cell = (Cell)objectList.get(2);

        worker1.setCell(worker2.getCell());
        worker2.setCell(cell);
    }
    @Override
    public void reset() {

    }
}
