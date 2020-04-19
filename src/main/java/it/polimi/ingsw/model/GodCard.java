package it.polimi.ingsw.model;

import java.util.List;

public abstract class GodCard {

    protected final SimpleGods card;
    private boolean active = false;

    public GodCard(SimpleGods card){
        this.card = card;
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

    public abstract void hasMoved(boolean moved);

    public abstract Phase getPhase();

    public abstract Cell getFirstBuilt();

    public abstract void setFirstBuilt(Cell firstBuilt);

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof GodCard){
            return ((GodCard) obj).card.getSimpleGodId() == this.card.getSimpleGodId();
        }
        return false;
    }

    public boolean isActive() {
        return active;
    }
}
