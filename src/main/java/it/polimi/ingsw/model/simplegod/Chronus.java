package it.polimi.ingsw.model.simplegod;

import it.polimi.ingsw.controller.GodCardController;
import it.polimi.ingsw.model.GodCard;
import it.polimi.ingsw.model.Gods;
import it.polimi.ingsw.model.Model;
import it.polimi.ingsw.model.Phase;

public class Chronus extends GodCard {
    /**
     * {@inheritDoc}
     */
    public Chronus() {
        super(Gods.CHRONUS, Phase.BEGINNING);
    }

    /**
     * This method change winning condition. Chronus win if the board there are 5 full towers (built from 0 to DOME)
     * When called, counts the tower and if there are 5 or more call the victory for the player
     * @param model the play model
     * @param controller the play controller
     */
    @Override
    public void checkVictory(Model model, GodCardController controller) {
        if(!model.getGCPlayer(Gods.CHRONUS).hasWon() && !model.getGCPlayer(Gods.CHRONUS).getHasLost()){
            if(controller.countTowers()>=5){
                model.victory(model.getGCPlayer(Gods.CHRONUS));
            }
        }
    }
}
