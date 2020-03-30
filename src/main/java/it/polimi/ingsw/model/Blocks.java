package it.polimi.ingsw.model;

public enum Blocks {
    EMPTY(0),
    LEVEL1(1),
    LEVEL2(2),
    LEVEL3(3),
    DOME(4)
    ;

    private final int block;

    Blocks(int block) {
        this.block = block;
    }

    public int getBlockId() {
        return block;
    }

    public static Blocks getBlock(int id) throws IllegalArgumentException {
        switch (id){
            case 0:
                return Blocks.EMPTY;
            case 1:
                return Blocks.LEVEL1;
            case 2:
                return Blocks.LEVEL2;
            case 3:
                return Blocks.LEVEL3;
            case 4:
                return Blocks.DOME;
            default:
                throw new IllegalArgumentException();
        }
    }
}
