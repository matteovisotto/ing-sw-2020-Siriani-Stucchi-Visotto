package it.polimi.ingsw.model.messageModel;
import it.polimi.ingsw.model.Phase;

import java.io.Serializable;


public class ViewMessage implements Serializable {
    private final MessageType messageType;
    private final String message;
    private final Phase phase;

    public ViewMessage(MessageType messageType, String message, Phase phase){
        this.messageType=messageType;
        this.message=message;
        this.phase=phase;
    }


    public Phase getPhase() {
        return phase;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public String getMessage() {
        return message;
    }
}
