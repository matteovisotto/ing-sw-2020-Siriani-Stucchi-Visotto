package it.polimi.ingsw.model;

import it.polimi.ingsw.model.simplegod.*;

public enum SimpleGods {
    APOLLO(1),
    ARTHEMIS(2),
    ATHENA(3),
    ATLAS(4),
    DEMETER(5),
    HEPHAESTUS(6),
    MINOTAUR(7),
    PAN(8),
    PROMETHEUS(9);

    private final int simplegod;

    SimpleGods(int simplegod) {
        this.simplegod = simplegod;
    }

    public static GodCard getGod(int id) throws IllegalArgumentException{
        switch(id) {
            case 1:
                return new Apollo();
            case 2:
                return new Arthemis();
            case 3:
                return new Athena();
            case 4:
                return new Atlas();
            case 5:
                return new Demeter();
            case 6:
                return new Hephaestus();
            case 7:
                return new Minotaur();
            case 8:
                return new Pan();
            case 9:
                return new Prometheus();
            default:
                throw new IllegalArgumentException();
        }
    }

    public int getSimpleGodId() {
        return simplegod;
    }


}
