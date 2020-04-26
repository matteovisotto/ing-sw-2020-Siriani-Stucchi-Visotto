
package it.polimi.ingsw.model.simplegod;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.messageModel.PlayerMove;

import java.util.List;
/**
 This class is intended to represent the Athena's GodCard
 */
public class Athena extends GodCard {
    private final Phase phase = Phase.BUILD;
    private Cell cell;
    private boolean moved = false;
    private boolean built = false;
    public Athena() {
        super(SimpleGods.ATHENA);
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

    public void hasMoved(boolean movedUp) {

    }

    public boolean hasBuilt() {
        return built;
    }

    /**
     * This method makes the setMovedUp static boolean (contained in the Model) to true -> the other player can't move up; it could be used only if the player decide to activate his power.
     * @param objectList contain the model of the actual game (objectList.get(0)).
     * @see Model {@link Model} In the model is contained the int static boolen MovedUp.
     */
    @Override
    public void usePower(List<Object> objectList) {
        Model model = (Model)objectList.get(0);
        Model.setMovedUp(true);
    }
    @Override
    public void reset() {

    }
}
