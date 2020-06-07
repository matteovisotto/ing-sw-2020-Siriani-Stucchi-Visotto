package it.polimi.ingsw.model;

import it.polimi.ingsw.model.simplegod.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

public class GodsTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void getSimpleGodId() {
        assertEquals(0, Gods.APOLLO.getSimpleGodId());
        assertEquals(1, Gods.ARTEMIS.getSimpleGodId());
        assertEquals(2, Gods.ATHENA.getSimpleGodId());
        assertEquals(3, Gods.ATLAS.getSimpleGodId());
        assertEquals(4, Gods.DEMETER.getSimpleGodId());
        assertEquals(5, Gods.HEPHAESTUS.getSimpleGodId());
        assertEquals(6, Gods.MINOTAUR.getSimpleGodId());
        assertEquals(7, Gods.PAN.getSimpleGodId());
        assertEquals(8, Gods.PROMETHEUS.getSimpleGodId());
    }

    @Test
    public void getGod() {
        assertEquals(Gods.getGod(0), new Apollo());
        assertEquals(Gods.getGod(1), new Artemis());
        assertEquals(Gods.getGod(2), new Athena());
        assertEquals(Gods.getGod(3), new Atlas());
        assertEquals(Gods.getGod(4), new Demeter());
        assertEquals(Gods.getGod(5), new Hephaestus());
        assertEquals(Gods.getGod(6), new Minotaur());
        assertEquals(Gods.getGod(7), new Pan());
        assertEquals(Gods.getGod(8), new Prometheus());
    }

    @Test
    public void getSimpleGodException() {
        exception.expect(IllegalArgumentException.class);
        Gods.getGod(15);
    }
}