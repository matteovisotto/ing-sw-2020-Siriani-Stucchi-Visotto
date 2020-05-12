package it.polimi.ingsw.model.messageModel;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.View;

public class PlayerMove extends Message{
    private final int x;
    private final int y;
    private final int workerId;

    public PlayerMove(Player player, int workerId, int x, int y, View view) {
        super(player, view);
        this.x = x;
        this.y = y;
        this.workerId = workerId;
    }

    public int getWorkerId(){
        return workerId;
    }

    public int getRow() {
        return x;
    }

    public int getColumn() {
        return y;
    }


    @Override
    public void handler(Controller controller) {
        controller.move(this);
    }
}