package it.polimi.ingsw.model.messageModel;

/**
 * This enum contains all the possible message type for a play. This is used in particular
 * in the GUI for changing elements dynamically
 */
public enum MessageType{
    PLAYER_NAME,
    JOIN_OR_CREATE_LOBBY,
    LOBBY_SELECTOR,
    LOBBY_NAME,
    NUMBER_OF_PLAYERS,
    SIMPLE_OR_NOT,
    WAIT_FOR_START,

    VOID_MESSAGE,

    DRAW_CARD,
    PICK_CARD,
    SET_WORKER_1,
    SET_WORKER_2,

    OPPONENT_TURN,
    BEGINNING,
    MOVE,
    BUILD,
    USE_POWER,
    PROMETHEUS,
    VICTORY,
    LOSE,
    END_GAME
}