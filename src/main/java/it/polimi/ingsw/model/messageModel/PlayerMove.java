package it.polimi.ingsw.model.messageModel;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.View;

public class PlayerMove extends Message{
    private final int row;
    private final int column;
    private final int workerId;

    public PlayerMove(Player player, int workerId, int row, int column, View view) {
        super(player, view);
        this.row = row;
        this.column = column;
        this.workerId = workerId;
    }

    public int getWorkerId(){
        return workerId;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }


    @Override
    public void handler(Controller controller) {
        controller.move(this);
    }
}