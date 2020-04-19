package it.polimi.ingsw.model.simplegod;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.messageModel.PlayerMove;

import java.util.List;

public class Arthemis extends GodCard {
    private final Phase phase = Phase.MOVE;
    private boolean moved = false;
    private Cell cell;
    public Arthemis() {
        super(SimpleGods.ARTHEMIS);
    }

    public boolean isMoved() {
        return moved;
    }

    public void hasMoved(boolean moved) {
        this.moved = moved;
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
        model.move((PlayerMove)objectList.get(1));
    }
    @Override
    public void reset() {
        moved=false;
    }
}
