package it.polimi.ingsw.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class SimpleGodsTest {

    @Test
    public void getSimpleGodId() {
        assertEquals(1, SimpleGods.APOLLO.getSimpleGodId());
        assertEquals(2, SimpleGods.ARTHEMIS.getSimpleGodId());
        assertEquals(3, SimpleGods.ATHENA.getSimpleGodId());
        assertEquals(4, SimpleGods.ATLAS.getSimpleGodId());
        assertEquals(5, SimpleGods.DEMETER.getSimpleGodId());
        assertEquals(6, SimpleGods.HEPHAESTUS.getSimpleGodId());
        assertEquals(7, SimpleGods.MINOTAUR.getSimpleGodId());
        assertEquals(8, SimpleGods.PAN.getSimpleGodId());
        assertEquals(9, SimpleGods.PROMETHEUS.getSimpleGodId());
    }

}