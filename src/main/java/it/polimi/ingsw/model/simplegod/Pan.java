package it.polimi.ingsw.model.simplegod;

import it.polimi.ingsw.model.*;

import java.util.List;

public class Pan extends GodCard {
    private final Phase phase = Phase.MOVE;
    private boolean moved = false;
    private Cell cell;
    public Pan(){
        super(SimpleGods.PAN);
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

    @Override
    public void usePower(List<Object> objectList) {
        Model model = (Model)objectList.get(0);
        Player player = (Player)objectList.get(1);
        model.victory(player);
    }
    @Override
    public void reset() {

    }
}
