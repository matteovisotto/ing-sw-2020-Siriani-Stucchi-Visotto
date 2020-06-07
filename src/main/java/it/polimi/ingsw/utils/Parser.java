package it.polimi.ingsw.utils;

/**
 * This class contain some utilities for parsing strings
 */
public class Parser {

    /**
     * This function return the capitalize version of the same stirng
     * @param string the string to be parsed
     * @return the string in capitalize form
     */
    public static String toCapitalize(String string){
        return string.substring(0,1).toUpperCase() + string.substring(1).toLowerCase();
    }

    /**
     * This function is used in Initialization GUI to transform the lobbies list string to a 2dimensional array
     * used to fill the table
     * @param inputMessage the lobbies string list
     * @return a matrix containing for each lobby the id, the name, number of players and the game mode
     */
    public static String[][] parseLobbies(String inputMessage){
        String[] splitted = inputMessage.split("\n");
        String[][] lobbies = new String[splitted.length-2][4];
        for(int i=2; i<splitted.length; i++){
            String lobby = splitted[i];
            lobby = lobby.replace('\t', ' ');
            String id = lobby.substring(0, lobby.indexOf('-')).trim();
            lobby = lobby.substring(lobby.indexOf('-')+2);
            String name = lobby.substring(0, lobby.indexOf('[')).trim();
            lobby = lobby.substring(lobby.indexOf('[')+1);
            String players = lobby.substring(0, lobby.indexOf(']'));
            lobby = lobby.substring(lobby.indexOf(']')+1);
            String type = lobby.trim();
            lobbies[i-2][0] = id;
            lobbies[i-2][1] = name;
            lobbies[i-2][2] = players;
            lobbies[i-2][3] = type;
        }
        return lobbies;
    }

}
