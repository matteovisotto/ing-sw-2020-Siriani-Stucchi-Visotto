package it.polimi.ingsw.model;

public class PlayerWorker {
    private final Player player;
    private final int x1;
    private final int x2;
    private final int y1;
    private final int y2;

    public PlayerWorker (Player player, int x1, int y1, int x2, int y2) {
        this.player = player;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public PlayerWorker (Player player, int x1, int y1) {
        this.player = player;
        this.x1 = x1;
        this.y1 = y1;
        x2 = -1;
        y2 = -1;
    }

    public Player getPlayer() {
        return player;
    }

    public int getX1() {
        return x1;
    }

    public int getX2() {
        return x2;
    }

    public int getY1() {
        return y1;
    }

    public int getY2() {
        return y2;
    }
}
