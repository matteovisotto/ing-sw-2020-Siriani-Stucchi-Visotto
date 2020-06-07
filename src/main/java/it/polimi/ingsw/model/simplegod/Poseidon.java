package it.polimi.ingsw.model.simplegod;

import it.polimi.ingsw.model.GodCard;
import it.polimi.ingsw.model.Gods;
import it.polimi.ingsw.model.Phase;

import java.util.List;

public class Poseidon  extends GodCard {
    /**
     * {@inheritDoc}
     */
    public Poseidon() {
        super(Gods.POSEIDON, Phase.BUILD);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void usePower(List<Object> objectList) {

    }
}
