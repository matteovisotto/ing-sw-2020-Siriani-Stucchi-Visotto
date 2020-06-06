package it.polimi.ingsw.model.messageModel;
import it.polimi.ingsw.model.Phase;

import java.io.Serializable;

/**
 * This class represent the superclass of messagges sended from model to views
 */
public class ViewMessage implements Serializable {
    private final MessageType messageType;
    private final String message;
    private final Phase phase;

    /**
     *
     * @param messageType the MessageType enum instance that represent the type of message
     * @param message string containing printable message
     * @param phase the Phase enum instance that generated this message
     */
    public ViewMessage(MessageType messageType, String message, Phase phase){
        this.messageType = messageType;
        this.message = message;
        this.phase = phase;
    }

    /**
     *
     * @return the Phase enum instance
     */
    public Phase getPhase() {
        return phase;
    }

    /**
     *
     * @return the MessageType enum instance
     */
    public MessageType getMessageType() {
        return messageType;
    }

    /**
     *
     * @return the string printable message
     */
    public String getMessage() {
        return message;
    }
}
