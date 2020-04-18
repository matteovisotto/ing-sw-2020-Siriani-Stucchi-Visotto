package it.polimi.ingsw.model.simplegod;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.messageModel.PlayerMove;

import java.util.List;

public class Arthemis extends GodCard {
    private final Phase phase=Phase.MOVE;
    private boolean moved=false;
    public Arthemis() {
        super(SimpleGods.ARTHEMIS);
    }

    public boolean isMoved() {
        return moved;
    }

    public Phase getPhase() {
        return phase;
    }

    @Override
    public void usePower(List<Object> objectList) {
        Model model=(Model)objectList.get(0);
        model.move((PlayerMove)objectList.get(1));
    }
    @Override
    public void reset() {
        moved=false;
    }
}
