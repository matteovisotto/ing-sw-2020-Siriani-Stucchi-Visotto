package it.polimi.ingsw.model.messageModel;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Phase;
import it.polimi.ingsw.model.Player;

public class GameBoardMessage extends GameMessage{
    private final Board board;


    public GameBoardMessage(Board board, Player player, String message, MessageType msg, Phase ph) {
        super(player, message, msg, ph);//questo invia un messaggio a tutti
        this.board = board;

    }

    public Board getBoard() {
        return board;
    }
}
