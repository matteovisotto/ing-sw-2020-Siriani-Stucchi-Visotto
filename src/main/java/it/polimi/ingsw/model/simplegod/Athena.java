
package it.polimi.ingsw.model.simplegod;

import it.polimi.ingsw.controller.GodCardController;
import it.polimi.ingsw.model.GodCard;
import it.polimi.ingsw.model.Gods;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.Phase;
import it.polimi.ingsw.model.messageModel.PlayerMove;

import java.util.ArrayList;
import java.util.Collections;
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

    /**
     * Before athena perform a move the previews value of the flag is reset
     * @param model the play model
     * @param controller the play controller
     * @param move the move message received from the view
     */
    @Override
    public void beforeMoveHandler(Model model, GodCardController controller, PlayerMove move) {
        model.setMovedUp(false);
    }

    /**
     * Athena power is auto activated, if she moved up the flag is set at true, else false
     * @param model the play model
     * @param controller the play controller
     * @param move the move message received from the view
     * @return always true because the flag changed
     */
    @Override
    public boolean handlerMove(Model model, GodCardController controller, PlayerMove move) {
        if(model.getBoard().getCell(move.getRow(), move.getColumn()).getLevel().getBlockId() > model.getActualPlayer().getWorker(move.getWorkerId()).getCell().getLevel().getBlockId()){
            move.getPlayer().getGodCard().usePower(new ArrayList<Object>(Collections.singletonList(model)));
        }
        else{
            model.setMovedUp(false);
        }
        model.move(move);
        model.notifyChanges();
        return true;
    }
}
