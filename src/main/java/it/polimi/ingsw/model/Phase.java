package it.polimi.ingsw.model;

/**
 * This class define the different phases of the game in a normal match.
 */
public enum Phase {
    WAIT_PLAYERS(-5),
    DRAWCARD(-4),
    PICK_CARD(-3),
    SETWORKER1(-2),
    SETWORKER2(-1),
    BEGINNING(0),
    MOVE(1),
    BUILD(2),
    END_GAME(10);

    private int phaseId;
    Phase(int phaseId) {
        this.phaseId = phaseId;
    }

    public int getPhaseId() {
        return phaseId;
    }

    /**
     * This method found the next phase of the game.
     * @param p define the id of the actual phase. The value accepted are: -4 -> DRAWCARD, -3 -> SETWORKER1, -2 -> SETWORKER2, -1 || 1 -> MOVE, 0 -> BUILD.
     * @return the next phase.
     * @throws IllegalArgumentException if the id is less then -4 or higher then 1.
     */
    public static Phase next(Phase p) throws IllegalArgumentException{
        int id = p.getPhaseId();
        switch (id){
            case -5:
                return Phase.DRAWCARD;
            case -4:
                return Phase.PICK_CARD;
            case -3:
                return Phase.SETWORKER1;
            case -2:
                return Phase.SETWORKER2;
            case -1:
            case 0:
            case 2:
                return Phase.MOVE;
            case 1:
                return Phase.BUILD;
            case 10:
                return Phase.END_GAME;
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * This method assign a value to it's specific phase.
     * @param id define the id of the phase. The value accepted are: -4 -> WAIT_PLAYERS, -3 -> DRAWCARD, -2 -> SETWORKER1, -1 -> SETWORKER2, 0 -> MOVE, 1 -> BUILD.
     * @return the phase assigned to a determined id.
     * @throws IllegalArgumentException if the id is less then -4 or higher then 1.
     */
    public static Phase getPhase(int id) throws IllegalArgumentException {
        switch (id){
            case -5:
                return Phase.WAIT_PLAYERS;
            case -4:
                return Phase.DRAWCARD;
            case -3:
                return Phase.PICK_CARD;
            case -2:
                return Phase.SETWORKER1;
            case -1:
                return Phase.SETWORKER2;
            case 0:
                return Phase.BEGINNING;
            case 1:
                return Phase.MOVE;
            case 2:
                return Phase.BUILD;
            case 10:
                return Phase.END_GAME;
            default:
                throw new IllegalArgumentException();
        }
    }



}
