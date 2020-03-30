package it.polimi.ingsw.model;

public class PlayerWorker {
    private final Player player;
    private final int x;
    private final int y;

    public PlayerWorker(Player player, int x, int y) {
        this.player = player;
        this.x = x;
        this.y = y;
    }

    public Player getPlayer() {
        return player;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}


