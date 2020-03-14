package it.polimi.ingsw.model;

public enum SimpleGods {
    APOLLO(1),
    ARTHEMIS(2),
    ATHENA(3),
    ATLAS(4),
    DEMETER(5),
    HEPHAESTUS(6),
    MINOTAUR(7),
    PAN(8),
    PROMETHEUS(9)
    ;

    private int simplegod;

    SimpleGods(int simplegod) {
        this.simplegod = simplegod;
    }

    public int getSimpleGod() {
        return simplegod;
    }
}
