
package it.polimi.ingsw.model.simplegod;

import it.polimi.ingsw.model.*;

import java.util.List;
/**
 This class is intended to represent the Athena's GodCard
 */
public class Athena extends GodCard {

    /**
     * {@inheritDoc}
     */
    public Athena() {
        super(Gods.ATHENA, Phase.MOVE);
    }

    /**
     * This method makes the setMovedUp static boolean (contained in the Model) to true -> the other player can't move up; it could be used only if the player decide to activate his power.
     * @param objectList contain the model of the actual game (objectList.get(0)).
     * @see Model {@link Model} In the model is contained the int static boolen MovedUp.
     */
    @Override
    public void usePower(List<Object> objectList) {
        Model model = (Model)objectList.get(0);
        model.setMovedUp(true);
    }
}
