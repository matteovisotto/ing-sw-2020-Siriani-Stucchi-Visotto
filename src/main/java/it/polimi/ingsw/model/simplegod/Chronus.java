package it.polimi.ingsw.model.simplegod;

import it.polimi.ingsw.model.GodCard;
import it.polimi.ingsw.model.Gods;
import it.polimi.ingsw.model.Phase;

import java.util.List;

public class Chronus extends GodCard {
    /**
     * {@inheritDoc}
     */
    public Chronus() {
        super(Gods.CHRONUS, Phase.BEGINNING);
    }

    /**
     *
     * @param objectList a generic list of Objects
     */
    @Override
    public void usePower(List<Object> objectList) {
        //win condition
    }
}
