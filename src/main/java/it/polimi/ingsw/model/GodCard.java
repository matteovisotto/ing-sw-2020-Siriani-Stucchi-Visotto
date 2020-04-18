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

    public abstract Phase getPhase();

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
