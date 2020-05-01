package it.polimi.ingsw.utils;

import it.polimi.ingsw.model.Phase;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.messageModel.Message;
import it.polimi.ingsw.model.messageModel.PlayerBuild;
import it.polimi.ingsw.model.messageModel.PlayerMove;
import it.polimi.ingsw.model.messageModel.PlayerWorker;
import it.polimi.ingsw.view.View;

public class CommandParser {
    private final Phase phase;
    private String string;
    private final Player player;
    private final View view;

    public CommandParser(Phase phase, String string, Player player, View view){
        this.phase=phase;
        this.string=string;
        this.player=player;
        this.view=view;
    }

    public Message parse() throws IllegalArgumentException{
        Message message=null;
        String[] s;
        switch(phase){
            case SETWORKER1: case SETWORKER2:
                string=string.replaceAll(" ", "");
                s= string.split(",");//x,y
                return new PlayerWorker(player, Integer.parseInt(s[0]), Integer.parseInt(s[1]), view);
            case MOVE:
                string=string.replaceAll(" ", "");
                s= string.split(",");//workerID, x, y
                return new PlayerMove(player, Integer.parseInt(s[0]), Integer.parseInt(s[1]), Integer.parseInt(s[2]), view);
            case BUILD:
                string=string.replaceAll(" ", "");
                s= string.split(",");//x,y
                return new PlayerBuild(player, Integer.parseInt(s[0]), Integer.parseInt(s[1]), view);
            default: throw new IllegalArgumentException("Can't do it now");
        }
    }
}
