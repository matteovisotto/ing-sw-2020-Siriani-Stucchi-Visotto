package it.polimi.ingsw.model.messageModel;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Phase;
import it.polimi.ingsw.model.Player;

/**
 * This class represent messages send in a player turn with an update for the board
 */
public class GameBoardMessage extends GameMessage{
    private final Board board;

    /**
     * {@inheritDoc}
     * @param board the bord cloned instance from the model
     */
    public GameBoardMessage(Board board, Player player, String message, MessageType msg, Phase phase) {
        super(player, message, msg, phase);//questo invia un messaggio a tutti
        this.board = board;

    }

    /**
     *
     * @return the board instance
     */
    public Board getBoard() {
        return board;
    }
}
