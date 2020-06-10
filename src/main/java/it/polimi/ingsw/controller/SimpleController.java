package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.messageModel.*;
import it.polimi.ingsw.utils.PlayerMessage;

import java.util.HashMap;

/**
 * This class represent the controller for the simple play mode
 */
public class SimpleController extends Controller {

    /**
     * {@inheritDoc}
     */
    public SimpleController(Model model){
        super(model);
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
    @Override
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
    @Override
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
    @Override
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

            super.increaseLevel(level.getBlockId(), buildingCell);
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
    @Override
    protected synchronized HashMap<Cell, Boolean> checkCellsAround (Worker actualWorker){
        HashMap<Cell, Boolean> availableCells = new HashMap<>();
        Cell actualWorkerCell = actualWorker.getCell();
        Board board = model.getBoard();
        for (int x = actualWorkerCell.getX() - 1; x <= actualWorkerCell.getX() + 1; x++) {
            for (int y = actualWorkerCell.getY() - 1; y <= actualWorkerCell.getY() + 1; y++) {
                try{
                    availableCells.put(board.getCell(x,y), board.checkCell(x,y,actualWorker,2));
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
