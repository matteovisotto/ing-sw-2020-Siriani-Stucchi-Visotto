package it.polimi.ingsw.model.messageModel;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;

public class MessageEveryPlayer extends ViewMessage{
    private final Board board;


    public MessageEveryPlayer(Board board, Player player, String message) {//questo invia un messaggio a tutti
        super(player, message);
        this.board=board;
    }
    public MessageEveryPlayer(Board board, Player player) { //Questo invia la board
        super(player, null);
        this.board=board;
    }

    public Board getBoard() {
        return board;
    }
}
