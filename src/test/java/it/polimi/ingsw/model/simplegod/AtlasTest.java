package it.polimi.ingsw.model.simplegod;

import it.polimi.ingsw.model.Cell;
import it.polimi.ingsw.model.GodCard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Worker;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class AtlasTest {
    @Test
    public void testUsePower() {
        Cell cell = new Cell(1,2);
        List<Object> cellList = new ArrayList<>();
        cellList.add(cell);
        GodCard godCard = new Atlas();
        godCard.usePower(cellList);
        assertTrue(cell.getLevel().getBlockId()==4);
    }
}
