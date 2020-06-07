package it.polimi.ingsw.model.simplegod;

import it.polimi.ingsw.model.GodCard;
import it.polimi.ingsw.model.Gods;
import it.polimi.ingsw.model.Phase;

import java.util.List;

public class Triton extends GodCard {
    /**
     * {@inheritDoc}
     */
    public Triton() {
        super(Gods.TRITON, Phase.MOVE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void usePower(List<Object> objectList) {

    }
}
