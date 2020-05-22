
package it.polimi.ingsw.model.simplegod;

import it.polimi.ingsw.model.*;

import java.util.List;
/**
 This class is intended to represent the Apollo's GodCard
 */
public class Apollo extends GodCard {

    public Apollo(){
        super(Gods.APOLLO, Phase.MOVE);
    }

    /**
     * This method switches the position of two different workers; it could be used only if the player decide to activate his power
     * @param objectList contain the positions of the two different workers; objectList.get(0) = Apollo's worker; objectList.get(1) = other worker
     */
    @Override
    public void usePower(List<Object> objectList) {
        Worker apolloWorker = (Worker)objectList.get(0);
        Worker enemyWorker = (Worker)objectList.get(1);
        Cell cell;
        //scambia le celle dei worker
        cell = enemyWorker.getCell();
        enemyWorker.setCell(apolloWorker.getCell());
        apolloWorker.setCell(cell);
    }
}
