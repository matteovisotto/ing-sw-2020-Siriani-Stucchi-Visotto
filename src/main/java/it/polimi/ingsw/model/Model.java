package it.polimi.ingsw.model;

import sun.text.normalizer.NormalizerBase;

import java.util.Observable;

public class Model extends Observable {
    private Player[] players;
    private Board board = new Board();

    public Model(Player[] players){
        this.players = players;
    }

    public Player getPlayer(int id) throws ArrayIndexOutOfBoundsException{
        return this.players[id];
    }

    public Board getBoard() {
        return board;
    }
}
