package it.polimi.ingsw.model.simplegod;

import it.polimi.ingsw.model.GodCard;
import it.polimi.ingsw.model.Gods;
import it.polimi.ingsw.model.Phase;

import java.util.List;

public class Zeus extends GodCard {
    /**
     * {@inheritDoc}
     */
    public Zeus() {
        super(Gods.ZEUS, Phase.BUILD);
    }

    @Override
    public void usePower(List<Object> objectList) {

    }
}
