package it.polimi.ingsw.model;

public abstract class GodCard {

    protected final SimpleGods card;

    public GodCard(SimpleGods card){
        this.card = card;
    }

    public String getName() {
        return card.toString();
    }

    @SuppressWarnings("EmptyMethod")
    public abstract void usePower();

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof GodCard){
            return ((GodCard) obj).card.getSimpleGodId() == this.card.getSimpleGodId();
        }
        return false;
    }
}
