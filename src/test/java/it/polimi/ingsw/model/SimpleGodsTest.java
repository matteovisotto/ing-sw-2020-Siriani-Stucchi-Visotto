package it.polimi.ingsw.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class SimpleGodsTest {

    @Test
    public void getSimpleGodId() {
        assertTrue(1 == SimpleGods.APOLLO.getSimpleGodId());
        assertTrue(2 == SimpleGods.ARTHEMIS.getSimpleGodId());
        assertTrue(3 == SimpleGods.ATHENA.getSimpleGodId());
        assertTrue(4 == SimpleGods.ATLAS.getSimpleGodId());
        assertTrue(5 == SimpleGods.DEMETER.getSimpleGodId());
        assertTrue(6 == SimpleGods.HEPHAESTUS.getSimpleGodId());
        assertTrue(7 == SimpleGods.MINOTAUR.getSimpleGodId());
        assertTrue(8 == SimpleGods.PAN.getSimpleGodId());
        assertTrue(9 == SimpleGods.PROMETHEUS.getSimpleGodId());
    }

}