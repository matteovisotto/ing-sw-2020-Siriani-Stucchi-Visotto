package it.polimi.ingsw.utils;

import it.polimi.ingsw.model.Phase;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.messageModel.*;
import it.polimi.ingsw.view.RemoteView;

public class CommandParser {
    private final Phase phase;
    private String string;
    private final Player player;
    private final RemoteView view;

    public CommandParser(Phase phase, String string, Player player, RemoteView view){
        this.phase = phase;
        this.string = string;
        this.player = player;
        this.view = view;
    }

    public Message parse() throws IllegalArgumentException, IndexOutOfBoundsException{
        String[] s;
        switch(phase){
            case DRAWCARD:
                string = string.replaceAll(" ", "");
                s = string.split(",");//x,y,z
                if(s.length==2){
                    if(Integer.parseInt(s[0])>=0 && Integer.parseInt(s[1])>=0 && Integer.parseInt(s[0])<=8 && Integer.parseInt(s[1])<=8){
                        return new DrawedCards(player, Integer.parseInt(s[0]), Integer.parseInt(s[1]), view);
                    }

                }
                else if(s.length==3){
                    if(Integer.parseInt(s[0])>=0 && Integer.parseInt(s[1])>=0 && Integer.parseInt(s[0])<=8 && Integer.parseInt(s[1])<=8 && Integer.parseInt(s[2])>=0 && Integer.parseInt(s[2])<=8) {
                        return new DrawedCards(player, Integer.parseInt(s[0]), Integer.parseInt(s[1]), Integer.parseInt(s[2]), view);
                    }
                }
                throw new IllegalArgumentException("Wrong input");
            case PICK_CARD:
                int p=Integer.parseInt(String.valueOf(string.charAt(0)));
                if(p<=2 && p>=0){
                    return new PickedCard(this.player, this.view, p);
                }
                throw new IllegalArgumentException("Wrong index input");

            case SETWORKER1: case SETWORKER2:
                string = string.replaceAll(" ", "");
                s = string.split(",");//x,y
                if(s.length>2){
                    throw new IllegalArgumentException("Wrong input, please insert x,y");
                }
                return new PlayerWorker(player, Integer.parseInt(s[0]), Integer.parseInt(s[1]), view);
            case MOVE:
                string = string.replaceAll(" ", "");
                s = string.split(",");//workerID, x, y
                if(s.length>3){
                    throw new IllegalArgumentException("Wrong input, please insert w,x,y");
                }
                return new PlayerMove(player, Integer.parseInt(s[0]), Integer.parseInt(s[1]), Integer.parseInt(s[2]), view);
            case BUILD:
                string = string.replaceAll(" ", "");
                s = string.split(",");//x,y
                if(s.length>2){
                    throw new IllegalArgumentException("Wrong input, please insert x,y");
                }
                return new PlayerBuild(player, player.getUsedWorker() , Integer.parseInt(s[0]), Integer.parseInt(s[1]), view);
            case WAIT_GOD_ANSWER:
                char ch = string.toLowerCase().charAt(0);
                if(ch!='y' && ch!='n'){
                    throw new IllegalArgumentException("Please insert only y or n");
                }
                return new UseGodPower(player, view, ch);
            case END_GAME:
                char c=string.toLowerCase().charAt(0);
                if(c!='y' && c!='n'){
                    throw new IllegalArgumentException("Please insert only y or n");
                }
                return new NewGameMessage(player, view, c, view.getConnection());
            default: throw new IllegalArgumentException("Can't do it now");
        }
    }
}
