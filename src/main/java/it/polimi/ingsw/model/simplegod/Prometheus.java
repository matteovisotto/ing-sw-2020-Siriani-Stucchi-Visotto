/**
 This class is intended to represent the Prometheus's GodCard
 */
package it.polimi.ingsw.model.simplegod;

import it.polimi.ingsw.model.*;

import java.util.List;

public class Prometheus extends GodCard {
    private boolean built = false;
    private final Phase phase = Phase.BEGINNING;
    private boolean moved = false;
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

    public boolean isMoved() {
        return moved;
    }

    public void hasMoved(boolean moved) {

    }

    public Phase getPhase() {
        return phase;
    }

    @Override
    public void usePower(List<Object> objectList) {
        Model model = (Model)objectList.get(0);
        Cell cell = (Cell)objectList.get(1);//possibilitá che vada scritto con il PlayerBuilt
        model.increaseLevel(cell, Blocks.getBlock(cell.getLevel().getBlockId()+1));
        built = true;
    }
    @Override
    public void reset() {
        built=false;
    }
}
