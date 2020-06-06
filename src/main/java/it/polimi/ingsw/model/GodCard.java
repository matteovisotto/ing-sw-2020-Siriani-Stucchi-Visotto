package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.List;

public abstract class GodCard implements Serializable {

    protected final Gods card;
    private boolean active = false;
    protected final Phase phase;

    /**
     * Constructor of the class
     * @param card the Gods enum instance for creating the model card
     * @param phase the phase in which the gods power should be activated
     */
    public GodCard(Gods card, Phase phase){
        this.card = card;
        this.phase = phase;
    }

    /**
     *
     * @return the Gods enum instance of the card created in this class
     */
    public Gods getCardGod() {
        return this.card;
    }

    /**
     *
     * @return the name of the god
     */
    public String getName() {
        return card.toString();
    }

    /**
     * This function is used to perform actions with the god power
     * Have to be overridden in subclasses to define the behavior
     * @param objectList a generic list of Objects
     */
    public abstract void usePower(List<Object> objectList);

    /**
     *
     * @return the phase in which control if god power can be used
     */
    public Phase getPhase(){
        return phase;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof GodCard){
            return ((GodCard) obj).card.getSimpleGodId() == this.card.getSimpleGodId();
        }
        return false;
    }

    /**
     *
     * @param active a flag that rappresent if the god card is active in the turn
     */
    public void setActive(boolean active){
        this.active = active;
    }

    /**
     *
     * @return true if the god card has been activated in the turn
     */
    public boolean isActive() {
        return active;
    }
}
