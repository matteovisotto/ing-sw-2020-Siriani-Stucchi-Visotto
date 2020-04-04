package it.polimi.ingsw.model;

public enum Phase {
    DRAWCARD(-3),
    SETWORKER1(-2),
    SETWORKER2(-1),
    WAIT(0),
    BEGINNING(1),
    MOVE(2),
    BUILD(3);

    private int phaseId;
    Phase(int phaseId) {
        this.phaseId = phaseId;
    }

    public int getPhaseId() {
        return phaseId;
    }

    public static Phase getPhase(int id) throws IllegalArgumentException {
        switch (id){
            case -3:
                return Phase.DRAWCARD;
            case -2:
                return Phase.SETWORKER1;
            case -1:
                return Phase.SETWORKER2;
            case 0:
                return Phase.WAIT;
            case 1:
                return Phase.BEGINNING;
            case 2:
                return Phase.MOVE;
            case 3:
                return Phase.BUILD;
            default:
                throw new IllegalArgumentException();
        }
    }

}
