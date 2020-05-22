package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.List;

public abstract class GodCard implements Serializable {

    protected final Gods card;
    private boolean active = false;
    protected final Phase phase;

    public GodCard(Gods card, Phase phase){
        this.card = card;
        this.phase = phase;
    }

    public Gods getCardGod() {
        return this.card;
    }

    public String getName() {
        return card.toString();
    }

    @SuppressWarnings("EmptyMethod")
    public abstract void usePower(List<Object> objectList);

    public Phase getPhase(){
        return phase;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof GodCard){
            return ((GodCard) obj).card.getSimpleGodId() == this.card.getSimpleGodId();
        }
        return false;
    }

    public void setActive(boolean active){
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }
}
