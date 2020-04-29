package it.polimi.ingsw.model.messageModel;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.View;

enum MessageType{
    PLAYER_NAME,
    JOIN_OR_CREATE_LOBBY,
    LOBBY_SELECTOR,
    LOBBY_NAME,
    NUMBER_OF_PLAYERS,
    SIMPLE_OR_NOT,
    WAIT_FOR_START,

    DRAW_CARD,
    SET_WORKER_1,
    SET_WORKER_2,

    OPPONENT_TURN,
    MOVE,
    BUILD
    ;


}
public class ViewMessage {
    private final MessageType messageType;
    private final String message;

    public ViewMessage(MessageType messageType, String message){
        this.messageType=messageType;
        this.message=message;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public String getMessage() {
        return message;
    }
}
