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
 * This is the main Controller class. It defines some abstract methods used by subclasses and implements common methods
 */
public class Controller implements Observer<Message> {
    protected final Model model;
    protected final ArrayList<String> playersName = new ArrayList<>();
    protected final ArrayList<ClientConnection> clientConnections = new ArrayList<>();
    protected int counter = 0;
    protected int answers = 0;
    protected final Map<Player, ClientConnection> activeClients = new LinkedHashMap<>();

    /**
     * Class constructor
     * @param model isd the model instance generated for the game
     */
    public Controller(Model model){
        super();
        this.model = model;
    }

    /**
     *
     * @return the game model
     */
    public Model getModel(){
        return model;
    }

    /**
     * This function checks if the player who tried to do anything is actually allowed to,
     *  by checking if it's its turn
     * @param message is the message received from the view
     * @return true if now it's the given player's turn
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
     * @param x is the x value of the cell
     * @param y is the y value of the cell
     * @param actualWorker is the worker which is performing the move
     * @param maxUpDifference represents the higher gap the worker can move upward. Is usually is set to 1, but this value can be changed by some god's powers.
     * @return true if the player can perform the desired move
     * @throws IllegalArgumentException if the cell values (x and y) are not between 0 and 4
     */
    public boolean checkCell (int x, int y, Worker actualWorker, int maxUpDifference) throws IllegalArgumentException{
        Cell nextCell = model.getBoard().getCell(x,y);
        Cell actualCell = actualWorker.getCell();
        return nextCell.isFree() && !nextCell.equals(actualCell) && (nextCell.getLevel().getBlockId() - actualCell.getLevel().getBlockId() < maxUpDifference) && nextCell.getLevel().getBlockId() != 4;
    }

    /**
     * This function updates the model giving it a new cell level
     * @param blockId is the integer id representing the Blocks enum instance
     * @param buildingCell the cell which level needs to be increased
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
     * It checks if the given building action can be performed.
     * It checks:
     *  1) if the given cell is next to the worker
     *  2) if it is not the cell where the player's worker is
     *  3) if the cell's coordinates are within the board range
     *  4) if the cell's level is not greater than level 3 (it can build the last level -> DOME)
     *  5) if the cell is free
     * @param buildingCell is the cell where the player wants to build
     * @param playerBuild The Message subclass containing the information for this action
     * @return true if the player could build in the given cell
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
     * This method checks if any player has won
     * This method uses the function canMove to decide whether a worker is able to move or not
     * If any player's workers can't move, it calls the model's loose method, giving it the player who looses as parameter
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
     * This method is called when the game is over
     * @param newGameMessage contains the player's reply after the following question has been given: "Would you like to play again?"
     * When the number of received answers is equal to the number of players in the game it:
     *  CLEARS THE MODEL: it happens only if every player answered 'yes', then the model is cleared and a new game starts
     *  SENDS A MESSAGE TO THE SERVER: when the last player has answered and the number of 'yes' are less than
     *                       the number of players in the game, a EndGameServerMessage is sent to the server, containing the following information:
     *                       -  The name of the previous lobby, to create a new one using the old name.
     *                       -  The name of the last player which answered 'yes' (and he becomes the new first player of the game)
     *                       -  The name of the second player (if it exists) who answered yes
     *                       -  The instance of ClientSocketConnection of every player
     *                       -  The actual game mode (hard or simple)
     * CLOSES THE CONNECTION of every players who answered 'no'
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
     * This method recive a placement for a worker
     * @param playerWorker the Message subclass containing the information about player, and the cell chosen for the worker
     * First check if the player who has sent the message is the player in turn
     * If he is, the worker is set in the board throw model setWorker function
     * Then:
     *      - If the Phase is SETWORKER1 update the model phase to SETWORKER2 but not the turn
     *      - If the phase is SETWORKER2 we can have 2 different cases:
     *                     - The player is not the last -> update turn and set the phase to SETWORKER1 again
     *                     - The player is the last -> update turn and set model to MOVE phase
     *
     * If an error is catch, it is sent only to che client who have generated it
     */
    public synchronized void setPlayerWorker(PlayerWorker playerWorker){
        //Check for right turn
        if(!turnCheck(playerWorker)){
            return;
        }
        try{
            if(model.getBoard().getCell(playerWorker.getX(), playerWorker.getY()).isFree()){
                if(model.getPhase() == Phase.SETWORKER2){
                    if(model.getActualPlayerId() != model.getNumOfPlayers() - 1){
                        model.updateTurn();
                        model.setNextPhase(Phase.SETWORKER1);
                        model.setNextMessageType(MessageType.SET_WORKER_1);
                        model.setNextPlayerMessage(PlayerMessage.PLACE_FIRST_WORKER);
                    }
                    else{
                        model.updateTurn();
                        model.updatePhase();
                        model.setNextMessageType(MessageType.MOVE);
                        model.setNextPlayerMessage(PlayerMessage.MOVE);
                    }
                }
                else{
                    model.updatePhase();
                    model.setNextMessageType(MessageType.SET_WORKER_2);
                    model.setNextPlayerMessage(PlayerMessage.PLACE_SECOND_WORKER);
                }
                model.setPlayerWorker(playerWorker);
                checkVictory();
            }
            else{
                playerWorker.getView().reportError("The cell is busy.");
            }
        }catch (IllegalArgumentException e){
            playerWorker.getView().reportError("Cell index must be between 0 and 4 (included)");
        }

    }


    /**
     * This method receive a move action
     * @param move the Message subclass containing the whole information
     * First check if the player who has sent the message is the player in turn
     * Then:
     *      - Check if the selected worker can move calling canMove
     *      - Call checkCellAround and check if the selected cell is available in the map and it results true
     * If all controls are positive set the next model phase to BUILT, update messages and MessageType and updae the turn
     * Then uodate the model with the new board configuration and notify clients of the uodate
     * At the end check if someone won
     * If an error is caught, it is sent to the client which generated it
     */
    public synchronized void move(PlayerMove move) {
        boolean canMove;
        if(!turnCheck(move)){
            return;
        }
        canMove = move.getPlayer().getWorker(move.getWorkerId()).getStatus();
        if(!canMove){
            move.getView().reportError("This worker can't move anywhere");
            return;
        }
        HashMap<Cell, Boolean> availableCells = checkCellsAround(move.getPlayer().getWorker(move.getWorkerId()));

        try{
            if (availableCells.get(model.getBoard().getCell(move.getRow(), move.getColumn())) != null && availableCells.get(model.getBoard().getCell(move.getRow(), move.getColumn()))) {
                try {
                    model.setNextMessageType(MessageType.BUILD);
                    model.setNextPlayerMessage(PlayerMessage.BUILD);
                    model.updatePhase();
                    model.move(move);
                    model.notifyChanges();
                    if(model.getBoard().getCell(move.getRow(), move.getColumn()).getLevel().getBlockId()==3){
                        model.victory(move.getPlayer());
                    }
                    checkVictory();
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.err.println(e.getMessage());
                }
            } else {
                move.getView().reportError("Unable to move in selected position");
            }
        }catch(IllegalArgumentException e){
            move.getView().reportError("Cell index must be between 0 and 4 (included)");
        }

    }

    /**
     * This method receive a built action
     * @param playerBuild the Message subclass containing the whole information
     * @throws IllegalArgumentException if the player can't built in the selected cell
     * First check if the player who has sent the message is the player in turn
     * Then call checkBuilt to verify if the player can built there else generete a new IllegalArgumentException
     * If can, the model is being updated with the next phase, the next turn and the next message
     * The super increase level is called
     *
     * At the end check if someone won
     */
    public synchronized void build(PlayerBuild playerBuild) throws IllegalArgumentException {
        if(!turnCheck(playerBuild)){
            return;
        }

        Cell buildingCell = this.model.getBoard().getCell(playerBuild.getX(), playerBuild.getY()); //ottengo la cella sulla quale costruire
        Blocks level = buildingCell.getLevel();//ottengo l'altezza della cella

        //qui devo fare i controlli
        if(checkBuild(buildingCell, playerBuild)){
            model.setNextMessageType(MessageType.MOVE);
            model.setNextPlayerMessage(PlayerMessage.MOVE);
            model.updatePhase();
            model.updateTurn();

            increaseLevel(level.getBlockId(), buildingCell);
        }
        else{
            throw new IllegalArgumentException();
        }
        checkVictory();

    }

    /**
     *This method is used to determinate which cells are available for a worker
     * @param actualWorker the worker in the bord where to check
     * @return a map with the board cell as key and a boolean that represent if the cell is available for the worker
     *
     * The control call for all the cells around the worker the board check cell function and put in the map the result
     * The IllegalArgumentException is caught for the perimeter cells
     */
    protected synchronized HashMap<Cell, Boolean> checkCellsAround (Worker actualWorker){
        HashMap<Cell, Boolean> availableCells = new HashMap<>();
        Cell actualWorkerCell = actualWorker.getCell();
        Board board = model.getBoard();
        for (int x = actualWorkerCell.getX() - 1; x <= actualWorkerCell.getX() + 1; x++) {
            for (int y = actualWorkerCell.getY() - 1; y <= actualWorkerCell.getY() + 1; y++) {
                try{
                    availableCells.put(board.getCell(x,y), checkCell(x,y,actualWorker,2));
                }
                catch (IllegalArgumentException e){
                    Cell c= new Cell(x,y);
                    availableCells.put(c, false);
                }
            }
        }
        return availableCells;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(Message msg) {//la update gestisce i messaggi
        msg.handler(this);
    }


}
