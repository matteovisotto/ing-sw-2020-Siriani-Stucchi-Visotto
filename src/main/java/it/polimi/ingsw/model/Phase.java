package it.polimi.ingsw.model;

/**
 * This class define the different phases of the game in a normal match.
 */
public enum Phase {
    DRAWCARD(-3),
    SETWORKER1(-2),
    SETWORKER2(-1),
    MOVE(0),
    BUILD(1);

    private int phaseId;
    Phase(int phaseId) {
        this.phaseId = phaseId;
    }

    public int getPhaseId() {
        return phaseId;
    }

    /**
     * This method found the next phase of the game.
     * @param p define the id of the actual phase. The value accepted are: -3 -> SETWORKER1, -2 -> SETWORKER2, -1 || 0 -> WAIT, 0 -> BEGINNING, 1 -> MOVE, 2 -> BUILD.
     * @return the next phase.
     * @throws IllegalArgumentException if the id is less then -3 or higher then 3.
     */
    public static Phase next(Phase p) throws IllegalArgumentException{
        int id = p.getPhaseId();
        switch (id){
            case -3:
                return Phase.SETWORKER1;
            case -2:
                return Phase.SETWORKER2;
            case -1:
            case 1:
                return Phase.MOVE;
            case 0:
                return Phase.BUILD;
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * This method assign a value to it's specific phase.
     * @param id define the id of the phase. The value accepted are: -3 -> DRAWCARD, -2 -> SETWORKER1, -1 -> SETWORKER2, 0 -> WAIT, 1 -> BEGINNING, 2 -> MOVE, 3 -> BUILD.
     * @return the phase assigned to a determined id.
     * @throws IllegalArgumentException if the id is less then -3 or higher then 3.
     */
    public static Phase getPhase(int id) throws IllegalArgumentException {
        switch (id){
            case -3:
                return Phase.DRAWCARD;
            case -2:
                return Phase.SETWORKER1;
            case -1:
                return Phase.SETWORKER2;
            case 0:
                return Phase.MOVE;
            case 1:
                return Phase.BUILD;
            default:
                throw new IllegalArgumentException();
        }
    }



}
