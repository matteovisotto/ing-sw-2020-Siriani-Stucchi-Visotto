package it.polimi.ingsw.model;

public enum Blocks {
    EMPTY(0),
    LEVEL1(1),
    LEVEL2(2),
    LEVEL3(3),
    DOME(4)
    ;

    private int block;

    Blocks(int block) {
        this.block = block;
    }

    public int getBlock() {
        return block;
    }
}
