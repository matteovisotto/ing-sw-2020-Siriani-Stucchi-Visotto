package it.polimi.ingsw.model;

import it.polimi.ingsw.model.simplegod.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

public class SimpleGodsTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void getSimpleGodId() {
        assertEquals(0, SimpleGods.APOLLO.getSimpleGodId());
        assertEquals(1, SimpleGods.ARTHEMIS.getSimpleGodId());
        assertEquals(2, SimpleGods.ATHENA.getSimpleGodId());
        assertEquals(3, SimpleGods.ATLAS.getSimpleGodId());
        assertEquals(4, SimpleGods.DEMETER.getSimpleGodId());
        assertEquals(5, SimpleGods.HEPHAESTUS.getSimpleGodId());
        assertEquals(6, SimpleGods.MINOTAUR.getSimpleGodId());
        assertEquals(7, SimpleGods.PAN.getSimpleGodId());
        assertEquals(8, SimpleGods.PROMETHEUS.getSimpleGodId());
    }

    @Test
    public void getGod() {
        assertEquals(SimpleGods.getGod(0), new Apollo());
        assertEquals(SimpleGods.getGod(1), new Arthemis());
        assertEquals(SimpleGods.getGod(2), new Athena());
        assertEquals(SimpleGods.getGod(3), new Atlas());
        assertEquals(SimpleGods.getGod(4), new Demeter());
        assertEquals(SimpleGods.getGod(5), new Hephaestus());
        assertEquals(SimpleGods.getGod(6), new Minotaur());
        assertEquals(SimpleGods.getGod(7), new Pan());
        assertEquals(SimpleGods.getGod(8), new Prometheus());
    }

    @Test
    public void getSimpleGodException() {
        exception.expect(IllegalArgumentException.class);
        SimpleGods.getGod(10);
    }
}