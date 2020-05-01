package it.polimi.ingsw.model.messageModel;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Phase;
import it.polimi.ingsw.model.Player;

public class MessageEveryPlayer extends ViewMessage{
    private final Board board;
    private final Player player;

    public MessageEveryPlayer(Board board, Player player, String message, MessageType msg, Phase ph) {//questo invia un messaggio a tutti
        super(msg, message, ph);
        this.board = board;
        this.player=player;
    }
    public MessageEveryPlayer(Board board, Player player, MessageType msg, Phase ph) { //Questo invia la board
        super(msg, null, ph);
        this.board = board;
        this.player=player;
    }

    public Player getPlayer() {
        return player;
    }

    public Board getBoard() {
        return board;
    }
}
