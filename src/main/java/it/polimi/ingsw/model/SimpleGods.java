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

    public static SimpleGods getGod(int id) {
        switch(id) {
            case 1:
                return SimpleGods.APOLLO;
            case 2:
                return SimpleGods.ARTHEMIS;
            case 3:
                return SimpleGods.ATHENA;
            case 4:
                return SimpleGods.ATLAS;
            case 5:
                return SimpleGods.DEMETER;
            case 6:
                return SimpleGods.HEPHAESTUS;
            case 7:
                return SimpleGods.MINOTAUR;
            case 8:
                return SimpleGods.PAN;
            case 9:
                return SimpleGods.PROMETHEUS;
            default:
                throw new IllegalArgumentException();
        }
    }

    public int getSimpleGod() {
        return simplegod;
    }


}
