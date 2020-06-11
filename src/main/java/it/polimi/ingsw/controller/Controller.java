package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.messageModel.*;
import it.polimi.ingsw.observer.Observer;
import it.polimi.ingsw.server.ClientConnection;
import it.polimi.ingsw.utils.PlayerMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Main Controller class, define some abstract method used by subclasses and implements common methods
 */
public abstract class Controller implements Observer<Message> {
    protected final Model model;
    protected final ArrayList<String> playersName = new ArrayList<>();
    protected final ArrayList<ClientConnection> clientConnections = new ArrayList<>();
    protected int counter = 0;
    protected int answers = 0;
    protected final Map<Player, ClientConnection> activeClients = new LinkedHashMap<>();

    /**
     * Class constructor
     * @param model model instance generated for the play
     */
    public Controller(Model model){
        super();
        this.model = model;
    }

    /**
     *
     * @return the model
     */
    public Model getModel(){
        return model;
    }

    /**
     * Check if the player who asked a command is the one who is in turn
     * @param message the message recived from the view
     * @return true if is the player's turn
     */
    public synchronized boolean turnCheck(Message message){
        if(!model.isPlayerTurn(message.getPlayer())){
            message.getView().reportError(PlayerMessage.TURN_ERROR);
            return false;
        }
        return true;
    }

    /**
     * Default checker for a move
     * @param x the x value of the cell
     * @param y the y value of the cell
     * @param actualWorker the worker who is performing the move
     * @param maxUpDifference the max step the worker can move up, normal 1, with some gods could be heigher
     * @return true if it can perform the asked move
     * @throws IllegalArgumentException if cell values are not between 0 and 4
     */
    public boolean checkCell (int x, int y, Worker actualWorker, int maxUpDifference) throws IllegalArgumentException{
        Cell nextCell = model.getBoard().getCell(x,y);
        Cell actualCell = actualWorker.getCell();
        return nextCell.isFree() && !nextCell.equals(actualCell) && (nextCell.getLevel().getBlockId() - actualCell.getLevel().getBlockId() < maxUpDifference) && nextCell.getLevel().getBlockId() != 4;
    }

    /**
     * Update the model with a new cell level
     * @param blockId the integer id representing the Bloks enum instance
     * @param buildingCell the cell where increasing the level
     */
    public synchronized void increaseLevel(int blockId, Cell buildingCell){
        switch(blockId) {
            case 0:
                model.increaseLevel(buildingCell, Blocks.LEVEL1);
                break;
            case 1:
                model.increaseLevel(buildingCell, Blocks.LEVEL2);
                break;
            case 2:
                model.increaseLevel(buildingCell, Blocks.LEVEL3);
                break;
            case 3:
                model.increaseLevel(buildingCell, Blocks.DOME);
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * General checker for a built
     * Check if the selected cell is next to the worker
     * Check if it is not the same cell where the player worker is
     * Check if the coordinates of the cell are allowed
     * Check if the cell level is not up to level 3 (it can built the last level -> DOME)
     * Check if the cell is free
     * @param buildingCell The selected cell where built
     * @param playerBuild The Message subclass containing the information for this action
     * @return true if the player could built in the cell
     */
    public synchronized boolean checkBuild(Cell buildingCell, PlayerBuild playerBuild){
        return Math.abs(buildingCell.getX() - (playerBuild.getPlayer().getWorker(playerBuild.getWorkerId()).getCell().getX())) <= 1 &&
                Math.abs(buildingCell.getY() - (playerBuild.getPlayer().getWorker(playerBuild.getWorkerId()).getCell().getY())) <= 1 &&
                (playerBuild.getPlayer().getWorker(playerBuild.getWorkerId()).getCell() != buildingCell) &&
                (buildingCell.getX() >= 0 && buildingCell.getX() < 5) &&
                (buildingCell.getY() >= 0 && buildingCell.getY() < 5) &&
                (buildingCell.getLevel().getBlockId() <= 3) &&
                (buildingCell.isFree());
    }

    /**
     * This method check if a player is winning
     * This method use canMove to decide if a worker is free to move
     * If both worker can't move, call the model lose method for the player
     */
    protected synchronized void checkVictory(){
        Player[] players = model.getPlayers();
        try{
            for(int i = 0; i < model.getNumOfPlayers(); i++){
                players[i].getWorker(0).setStatus(canMove(players[i].getWorker(0), players[i])!=0);
                players[i].getWorker(1).setStatus(canMove(players[i].getWorker(1), players[i])!=0);
                //No Worker can move anywhere
                if(!players[i].getWorker(0).getStatus() && !players[i].getWorker(1).getStatus()){// controllo se nessun worker si può muovere
                    model.loose(players[i]);
                }
            }
        }catch(IndexOutOfBoundsException e){
            //Ignored: this happen only when the player is placing his worker
        }

    }

    /**
     * This method is called when the game is ended
     * @param newGameMessage contains the response of one player at the question "Would you like to play again?"
     * Using two global variable and a global counter when a number of answers equals to the num of players are been received
     * it decide if:
     *  CLEAR THE MODEL: only if all players answered 'yes', the model is cleared and a new play start
     *  SEND A MESSAGE TO THE SERVER: when last player have answered and the number of yes are minor then
     *                       the number of players a EndGameServerMessage is sended to the server with the follow information
     *                       -  The name of the actual lobby for creating a new one with the same name
     *                       -  The name of the last player who answered yes (and he become the new first player of the game)
     *                       -  The name of the second player (if exist) who answered yes too
     *                       -  The instance of ClientSocketConnection of players
     *                       -  The actual game mode between hard and simple
     * CLOSE CONNECTIONS of players who answered 'no'
     */
    public synchronized void endGame(NewGameMessage newGameMessage){
        answers++;
        if(newGameMessage.getChoice() == 'y'){
            this.counter++;
            activeClients.put(newGameMessage.getPlayer(), newGameMessage.getClientConnection());
            if(counter == model.getNumOfPlayers()){
                newGameMessage.getLobby().resetEndGame();
                counter=0;
                answers=0;
                activeClients.clear();
                playersName.clear();
                model.startOver();
            } else if(answers == model.getNumOfPlayers()){
                for (Map.Entry<Player, ClientConnection> names: activeClients.entrySet()) {
                    this.playersName.add(names.getKey().getPlayerName());
                    this.clientConnections.add(names.getValue());
                }
                EndGameServerMessage endGameServerMessage = new EndGameServerMessage(newGameMessage.getLobby(),clientConnections,playersName,answers, model.isSimplePlay());
                newGameMessage.getClientConnection().send(endGameServerMessage);
            }

        } else {
            if(answers == model.getNumOfPlayers() && counter != 0){
                for (Map.Entry<Player, ClientConnection> names: activeClients.entrySet()) {
                    this.playersName.add(names.getKey().getPlayerName());
                    this.clientConnections.add(names.getValue());
                }
                EndGameServerMessage endGameServerMessage = new EndGameServerMessage(newGameMessage.getLobby(),clientConnections,playersName,answers, model.isSimplePlay());
                newGameMessage.getClientConnection().send(endGameServerMessage);
            }

            newGameMessage.getClientConnection().closeConnection();
        }

    }

    /**
     * This method check if a worker can move around his cell
     * For each cell around it check:
     *  - If the cell is free
     *  - If the cell is not the same where the worker is
     *  - If the difference between the actual cell level and the next one is at least one
     *  - If a dome has't been built in the cell
     *  If the control for the single cell is positive, the counter is incremented
     * @param worker the worker the player has chosen to move
     * @param player the player who is asking the action
     * @return the number of cell which have passed the control
     */
    protected synchronized int canMove(Worker worker, Player player){
        Cell actualCell = worker.getCell();
        int numOfAvailableCells=0;
        for (int x = actualCell.getX() - 1; x <= actualCell.getX() + 1; x++) {
            for (int y = actualCell.getY() - 1; y <= actualCell.getY() + 1; y++) {
                if(x >= 0 && y >= 0 && x < 5 && y < 5){
                    Cell nextCell = model.getBoard().getCell(x,y);
                    if(nextCell.isFree() && !nextCell.equals(actualCell) && (nextCell.getLevel().getBlockId() -  actualCell.getLevel().getBlockId() < 2) && nextCell.getLevel().getBlockId() != 4){
                        numOfAvailableCells++;
                    }
                }
            }
        }
        return numOfAvailableCells;
    }

    /**
     * Function to check the cell around a worker
     * @param worker the worker for which check
     * @return a map with the board cell as key and a boolean result as value
     */
    protected abstract HashMap<Cell, Boolean> checkCellsAround (Worker worker);

    /**
     * Method used to set a new worker in the board
     * @param playerWorker the Message subclass containing the information about player, and the cell chosen for the worker
     */
    public abstract void setPlayerWorker(PlayerWorker playerWorker);

    /**
     * This method make controls for a move action
     * @param move the Message subclass containing the whole information
     */
    public abstract void move(PlayerMove move);

    /**
     * This method make controls for a built action
     * @param playerBuild the Message subclass containing the whole information
     * @throws IllegalArgumentException
     */
    public abstract void build(PlayerBuild playerBuild) throws IllegalArgumentException;


}
