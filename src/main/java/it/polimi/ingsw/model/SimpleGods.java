package it.polimi.ingsw.model;

import it.polimi.ingsw.model.simplegod.*;

/**
 * This class define the different simpleGod of the game.
 */
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

    /**
     * This method assign a value to it's specific simpleGod.
     * @param id define the id of the simpleGod. The value accepted are: 1 -> new Apollo(), 2 -> new Arthemis(), 3 -> new Athena(), 4 -> new Atlas(), 5 -> new Demeter(), 6 -> new Hephaestus(), 7 -> new Minotaur(), 8 -> new Pan(), 9 -> new Prometheus().
     * @return the simpleGod assigned to a determined id.
     * @throws IllegalArgumentException if the id is less then 1 or higher then 9.
     */
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
