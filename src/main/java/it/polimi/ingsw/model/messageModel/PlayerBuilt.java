package it.polimi.ingsw.model.messageModel;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.View;

public class PlayerBuilt extends Message {

    private final int x;
    private final int y;
    private final int levelId;

    public PlayerBuilt(Player player, View view, int x, int y, int levelId) {
        super(player, view);
        this.x = x;
        this.y = y;
        this.levelId = levelId;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getLevelId(){
        return levelId;
    }

}