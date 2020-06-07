package it.polimi.ingsw.model.simplegod;

import it.polimi.ingsw.model.GodCard;
import it.polimi.ingsw.model.Gods;
import it.polimi.ingsw.model.Phase;

import java.util.List;

public class Hestia extends GodCard {
    /**
     * {@inheritDoc}
     */
    public Hestia() {
        super(Gods.HESTIA, Phase.BUILD);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void usePower(List<Object> objectList) {

    }
}
