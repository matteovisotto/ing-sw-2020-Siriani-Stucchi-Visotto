package it.polimi.ingsw.model.messageModel;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;

public class ViewMessage {

    private Player player;
    private Board board;

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
