
package it.polimi.ingsw.model.simplegod;

import it.polimi.ingsw.controller.GodCardController;
import it.polimi.ingsw.model.GodCard;
import it.polimi.ingsw.model.Gods;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.Phase;
import it.polimi.ingsw.model.messageModel.PlayerMove;

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

    /**
     * This control has been done in the move action, if the selected cell is 2 or more level smaller, the victory is called.
     * @param model the play model
     * @param controller the play controller
     * @param move the move message received from the view
     * @return true if the victory is called, else false
     */
    @Override
    public boolean handlerMove(Model model, GodCardController controller, PlayerMove move) {
        if(model.getActualPlayer().getWorker(move.getWorkerId()).getCell().getLevel().getBlockId()-model.getBoard().getCell(move.getRow(), move.getColumn()).getLevel().getBlockId()>=2){
            model.victory(move.getPlayer());
            return true;
        }
        return false;
    }
}
