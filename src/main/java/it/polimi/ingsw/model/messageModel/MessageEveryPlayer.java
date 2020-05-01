package it.polimi.ingsw.model.messageModel;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;

public class MessageEveryPlayer extends ViewMessage{
    private final Board board;
    private final Player player;

    public MessageEveryPlayer(Board board, Player player, String message, MessageType msg) {//questo invia un messaggio a tutti
        super(msg, message);
        this.board = board;
        this.player=player;
    }
    public MessageEveryPlayer(Board board, Player player, MessageType msg) { //Questo invia la board
        super(msg, null);
        this.board = board;
        this.player=player;
    }

    public Board getBoard() {
        return board;
    }
}
