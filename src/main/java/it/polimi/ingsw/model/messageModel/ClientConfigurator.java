package it.polimi.ingsw.model.messageModel;

import it.polimi.ingsw.model.Player;

import java.io.Serializable;
import java.util.ArrayList;

public class ClientConfigurator implements Serializable {
    private final int numberOfPlayer;
    private final ArrayList<String> opponentsNames;
    private final Player me;

    public ClientConfigurator(int numberOfPlayer, ArrayList<String> opponentsNames, Player me) {
        this.numberOfPlayer = numberOfPlayer;
        this.opponentsNames = opponentsNames;
        this.me = me;
    }

    public int getNumberOfPlayer() {
        return numberOfPlayer;
    }

    public ArrayList<String> getOpponentsNames() {
        return opponentsNames;
    }

    public Player getMyself() {
        return me;
    }
}
