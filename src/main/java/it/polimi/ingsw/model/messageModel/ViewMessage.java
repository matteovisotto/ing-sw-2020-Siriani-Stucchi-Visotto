package it.polimi.ingsw.model.messageModel;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.View;


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
