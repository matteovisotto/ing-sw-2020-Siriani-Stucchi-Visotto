package it.polimi.ingsw.model.messageModel;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.View;

public class PlayerWorker {
    private final Player player;
    private final View view;
    private final int x;
    private final int y;

    public PlayerWorker(Player player, int x, int y, View view) {
        this.player = player;
        this.x = x;
        this.y = y;
        this.view = view;
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


