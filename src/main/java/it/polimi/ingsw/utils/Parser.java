package it.polimi.ingsw.utils;

public class Parser {

    public static String toCapitalize(String string){
        return string.substring(0,1).toUpperCase() + string.substring(1).toLowerCase();
    }
}
