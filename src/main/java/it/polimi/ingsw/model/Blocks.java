package it.polimi.ingsw.model;

/**
 * This class define the different level of blocks in the game.
 */
public enum Blocks {
    EMPTY(0),
    LEVEL1(1),
    LEVEL2(2),
    LEVEL3(3),
    DOME(4)
    ;

    private final int block;

    /**
     * Constructor of the class
     * @param block is the level of a cell.
     */
    Blocks(int block) {
        this.block = block;
    }

    /**
     * @return an int the represents the actual level of a cell.
     */
    public int getBlockId() {
        return block;
    }

    /**
     * This method assign a value to it's specific block.
     * @param id define the id of the block. The value accepted are: 0 -> EMPTY, 1 -> LEVEL1, 2 -> LEVEL2, 3 -> LEVEL3, 4 -> DOME.
     * @return the block assigned to a determined id.
     * @throws IllegalArgumentException if the id is less then 0 or higher then 4.
     */
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
