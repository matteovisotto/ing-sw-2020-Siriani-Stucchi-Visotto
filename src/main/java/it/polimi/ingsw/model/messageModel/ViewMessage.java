package it.polimi.ingsw.model.messageModel;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;

public class ViewMessage {

    private final Player player;
    private final Board board;

    public ViewMessage(Board board, Player player){
        this.player=player;
        this.board=board;
    }

    public Board getBoard(){
        return board;
    }

    public Player getPlayer(){
        return player;
    }
}
