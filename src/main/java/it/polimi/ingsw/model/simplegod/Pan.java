
package it.polimi.ingsw.model.simplegod;

import it.polimi.ingsw.model.*;

import java.util.List;
/**
 This class is intended to represent the Pan's GodCard
 */
public class Pan extends GodCard {

    /**
     * {@inheritDoc}
     */
    public Pan(){
        super(Gods.PAN, Phase.MOVE);
    }

    /**
     * This method makes a player win if his worker moves down two or more levels; it could be used only if the player decide to activate his power.
     * @param objectList contain the model of the actual game (objectList.get(0)).
     * @see Model {@link Model} In the Model is contained the victory method.
     */
    @Override
    public void usePower(List<Object> objectList) {
        Model model = (Model)objectList.get(0);
        model.victory(model.getActualPlayer());
    }
}
