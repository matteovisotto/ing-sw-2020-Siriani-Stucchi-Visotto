package it.polimi.ingsw.model;

import it.polimi.ingsw.model.simplegod.*;

/**
 * This class define the different simpleGod of the game.
 */
public enum Gods {
    APOLLO(0),
    ARTEMIS(1),
    ATHENA(2),
    ATLAS(3),
    DEMETER(4),
    HEPHAESTUS(5),
    MINOTAUR(6),
    PAN(7),
    PROMETHEUS(8);

    private final int simplegod;

    Gods(int simplegod) {
        this.simplegod = simplegod;
    }

    /**
     * This method assign a value to it's specific simpleGod.
     * @param id define the id of the simpleGod. The value accepted are: 1 -> new Apollo(), 2 -> new Artemis(), 3 -> new Athena(), 4 -> new Atlas(), 5 -> new Demeter(), 6 -> new Hephaestus(), 7 -> new Minotaur(), 8 -> new Pan(), 9 -> new Prometheus().
     * @return the simpleGod assigned to a determined id.
     * @throws IllegalArgumentException if the id is less then 1 or higher then 9.
     */
    public static GodCard getGod(int id) throws IllegalArgumentException{
        switch(id) {
            case 0:
                return new Apollo();
            case 1:
                return new Artemis();
            case 2:
                return new Athena();
            case 3:
                return new Atlas();
            case 4:
                return new Demeter();
            case 5:
                return new Hephaestus();
            case 6:
                return new Minotaur();
            case 7:
                return new Pan();
            case 8:
                return new Prometheus();
            default:
                throw new IllegalArgumentException();
        }
    }

    public int getSimpleGodId() {
        return simplegod;
    }

}
