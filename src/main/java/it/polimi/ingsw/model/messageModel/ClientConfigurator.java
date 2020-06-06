package it.polimi.ingsw.model.messageModel;

import it.polimi.ingsw.model.Player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class ClientConfigurator implements Serializable {
    private final int numberOfPlayer;
    private final HashMap<String,String> opponentsNames;
    private final Player me;

    public ClientConfigurator(int numberOfPlayer, HashMap<String,String> opponentsNames, Player me) {
        this.numberOfPlayer = numberOfPlayer;
        this.opponentsNames = opponentsNames;
        this.me = me;
    }

    public int getNumberOfPlayer() {
        return numberOfPlayer;
    }

    public HashMap<String,String> getOpponentsNames() {
        return opponentsNames;
    }

    public Player getMyself() {
        return me;
    }
}
