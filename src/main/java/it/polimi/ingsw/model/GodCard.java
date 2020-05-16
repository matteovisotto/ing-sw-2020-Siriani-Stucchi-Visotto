package it.polimi.ingsw.model;

import java.io.Serializable;
import java.util.List;

public abstract class GodCard implements Serializable {

    protected final SimpleGods card;
    private boolean active = false;
    protected final Phase phase;

    public GodCard(SimpleGods card, Phase phase){
        this.card = card;
        this.phase=phase;
    }

    public SimpleGods getCardGod() {
        return this.card;
    }

    public String getName() {
        return card.toString();
    }

    @SuppressWarnings("EmptyMethod")
    public abstract void usePower(List<Object> objectList);

    public abstract void reset();

    public abstract boolean isMoved();

    public abstract boolean hasBuilt();

    public abstract void setBuild(boolean built);

    public abstract void hasMoved(boolean moved);

    public Phase getPhase(){
        return phase;
    }

    public abstract Cell getFirstBuilt();

    public abstract void setFirstBuilt(Cell firstBuilt);

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
