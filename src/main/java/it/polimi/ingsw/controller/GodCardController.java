package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.messageModel.*;
import it.polimi.ingsw.model.simplegod.Prometheus;
import it.polimi.ingsw.utils.PlayerMessage;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class complete the controller class for using
 */
public class GodCardController extends Controller {

    /**
     * {@inheritDoc}
     */
    public GodCardController(Model model) {
        super(model);
    }

    /**
     * Modified set worker function, if prometheus card exists it modify the game flow, insted start with moving phase it start with asking
     * for using the power, only if the next player in turn has that card.
     * @param playerWorker the Message subclass containing the information about player, and the cell chosen for the worker
     * First check if the player who has sent the message is the player in turn
     * If he is, the worker is set in the board throw model setWorker function
     * Then:
     *      - If the Phase is SETWORKER1 update the model phase to SETWORKER2 but not the turn
     *      - If the phase is SETWORKER2 we can have 2 different cases:
     *                     - The player is not the last -> update turn and set the phase to SETWORKER1 again
     *                     - The player is the last -> update turn and set model to MOVE phase or ask power if prometheus
     *
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
                        if(model.getActualPlayer()==model.getGCPlayer(Gods.PROMETHEUS) && !((Prometheus)model.getGCPlayer(Gods.PROMETHEUS).getGodCard()).hasBuilt()){
                            model.setNextPhase(Phase.WAIT_GOD_ANSWER);
                            model.setNextPlayerMessage(PlayerMessage.USE_POWER);
                            model.setNextMessageType(MessageType.USE_POWER);
                        }
                        else{
                            model.updatePhase();
                            model.setNextMessageType(MessageType.MOVE);
                            model.setNextPlayerMessage(PlayerMessage.MOVE);
                        }

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
     * This method let the first player choosing 2 or 3 god cards (depending on the number of players in the game)
     * For each selected card the model add god function is called.
     * If the selected curd number not corresponding the number of players an error is reported and te action asked again
     * @param drawedCards the message sent by the view containing the drawed cards
     *
     *  After saved the card the phase is changed to Pick a card in order to let the other player to choose their god card
     */
    public synchronized void drawedCards(DrawedCards drawedCards){
        if(drawedCards.getFirst() == drawedCards.getSecond() || drawedCards.getFirst()==drawedCards.getThird() || drawedCards.getSecond()==drawedCards.getThird()){
            if(drawedCards.getThird()!=-1) {
                drawedCards.getView().reportError("Please select 2 different cards");
            }else{
                drawedCards.getView().reportError("Please select 3 different cards");
            }
            return;
        }

        if(drawedCards.getThird() != -1 && model.getNumOfPlayers() == 3){
            model.addGod(Gods.getGod(drawedCards.getFirst()));
            model.addGod(Gods.getGod(drawedCards.getSecond()));
            model.addGod(Gods.getGod(drawedCards.getThird()));
        }
        else if(model.getNumOfPlayers() == 2 && drawedCards.getThird() != -1){
            drawedCards.getView().reportError("Insert 2 god cards only");
            return;
        }
        else{
            model.addGod(Gods.getGod(drawedCards.getFirst()));
            model.addGod(Gods.getGod(drawedCards.getSecond()));
        }
        model.notifyMessage(Gods.getGod(drawedCards.getFirst()).getName());
        model.notifyMessage(Gods.getGod(drawedCards.getSecond()).getName());
        if(model.getNumOfPlayers() == 3){
            model.notifyMessage(Gods.getGod(drawedCards.getThird()).getName());
        }
        model.updatePhase();
        model.updateTurn();
        model.setNextMessageType(MessageType.PICK_CARD);

        if(model.getNumOfPlayers()==3){
            model.setNextPlayerMessage("Pick a God between: \n0 - " + Gods.getGod(drawedCards.getFirst()).getName() + "\n1 - " + Gods.getGod(drawedCards.getSecond()).getName() + "\n2 - " + Gods.getGod(drawedCards.getThird()).getName());
        }
        else{
            model.setNextPlayerMessage("Pick a God between: \n0 - " + Gods.getGod(drawedCards.getFirst()).getName() + "\n1 - " + Gods.getGod(drawedCards.getSecond()).getName());
        }
        model.notifyChanges();
    }

    /**
     * Thid method control and save the card chosen by a player, if the card exists in the array containing the ones drawed,
     * the card is set and the phase updated to repeat the same action for the third player, else it assign the left card to the first player
     * and set the game phase to the SETWORKER1
     * @param pickedCard the message sent by the view containing the selected card
     */
    public synchronized void pickACard(PickedCard pickedCard){
        if(!turnCheck(pickedCard)){
            return;
        }

        if(pickedCard.getCardId() > model.getNumOfPlayers() - 1){
            pickedCard.getView().reportError("Insert a valid input");
            return;
        }
        if(model.isGodAvailable(pickedCard.getCardId())){
            String godName = model.assignCard(pickedCard.getPlayer(), pickedCard.getCardId()).getName();
            model.notifyMessage("You selected " + godName + ", good luck!");
        }
        else{
            pickedCard.getView().reportError("The selected god isn't available");
            return;
        }
        if(model.getLeftCards() == 1){
            model.updateTurn();
            String godName = model.assignCard(model.getActualPlayer(),0).getName();
            model.notifyMessage("Only " + godName + " was left, that's your card, good luck!");
            model.updatePhase();
            model.setNextMessageType(MessageType.SET_WORKER_1);
            model.setNextPlayerMessage(PlayerMessage.PLACE_FIRST_WORKER);
            model.notifyChanges();
        }
        else{
            model.updateTurn();
            model.setNextMessageType(MessageType.PICK_CARD);
            model.setNextPlayerMessage(PlayerMessage.PICK_CARD + "\n0 - " + model.getGods().get(0).getName() + "\n1 - " + model.getGods().get(1).getName());
            model.notifyChanges();
        }
    }

    /**
     * Modified method, add a initial function for god card class used to reset some internal data if needed, then call
     * an other god card method to handler the move action and controls, if the result is false means that no different action in needed and so
     * continue with the normal move, else the normal control is skipped and each god card modify the game flow as the power has defined.
     * Before confirming the normal move action, an other function is called, the normalMoveModifier can change same model parameter, for example
     * Atlas card make a normal move but set next phase to ask god power.
     * @param move the Message subclass containing the whole information
     * First check if the player who has sent the message is the player in turn
     * Then:
     *      - Check if the selected worker can move calling canMove
     *      - Call checkCellAround and check if the selected cell is available in the map and it results true
     * If all controls are positive set the next model phase to BUILT, update messages and MessageType and updae the turn
     * Then uodate the model with the new board configuration and notify clients of the uodate
     * At the end check if someone won
     */
    @Override
    public synchronized void move(PlayerMove move) {
        int oldLevel=model.getActualPlayer().getWorker(move.getWorkerId()).getCell().getLevel().getBlockId();
        if(!turnCheck(move)){
            return;
        }

        move.getPlayer().getGodCard().beforeMoveHandler(model, this, move);

        HashMap<Cell, Boolean> availableCells = checkCellsAround(move.getPlayer().getWorker(move.getWorkerId()));

        try{
            if (availableCells.get(model.getBoard().getCell(move.getRow(), move.getColumn())) != null && availableCells.get(model.getBoard().getCell(move.getRow(), move.getColumn()))) {
                try {
                    model.setNextMessageType(MessageType.BUILD);
                    model.setNextPlayerMessage(PlayerMessage.BUILD);
                    model.setNextPhase(Phase.BUILD);
                    if(!move.getPlayer().getGodCard().handlerMove(model,this , move)) {
                        move.getPlayer().getGodCard().normalMoveModifier(model, this, move);
                        model.move(move);
                        model.notifyChanges();
                    }
                    if(model.getBoard().getCell(move.getRow(), move.getColumn()).getLevel().getBlockId() == 3 && oldLevel!=3){
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
     * This method is used to control if the worker used to build the first time can build again
     * In particular is useful with gods that can built two or more times.
     * If the player chooses to build again mut the worker can't the loose function is called.
     * @param playerBuild the built message received by the view
     */
    public synchronized void checkCantBuild(PlayerBuild playerBuild){
        Cell cell = model.getBoard().getCell(playerBuild.getX(), playerBuild.getY());
        Board board = model.getBoardClone();
        for (int x = cell.getX() - 1; x <= cell.getX() + 1; x++) {
            for (int y = cell.getY() - 1; y <= cell.getY() + 1; y++) {
                if((x >= 0 && x <= 4) && (y >= 0 && y <= 4)){
                    if(board.getCell(x,y).getLevel().getBlockId() != 4){
                        return;
                    }
                }
            }
        }
        model.loose(playerBuild.getPlayer());
    }

    /**
     * This method return the number of cells in which the checkCellAround return true. The returned cell
     * represent the possibility for a worker to move, if zero it can't do anything.
     * @param worker the worker the player has chosen to move
     * @param player the player who is asking the action
     * @return the number of cells in which the selected worker can move
     */
    @Override
    public synchronized int canMove(Worker worker, Player player){
        HashMap<Cell, Boolean> availableCells=checkCellsAround(worker);
        int numOfAvailableCells=0;
        for (Boolean can:availableCells.values()) {
            if(can){
                numOfAvailableCells++;
            }
        }
        return numOfAvailableCells;
    }

    /**
     *  First check if the player who has sent the message is the player in turn
     *      Then call checkBuilt to verify if the player can built there else generete a new IllegalArgumentException
     *      Then a god card built handler ic called in porder to check if the player card can modify the game flow or controls for that build
     *      If can, the model is being updated with the next phase, the next turn and the next message by the card controller else a normal build is done
     *      At the end an other god card action is called for cleaning some flag is needen
     *
     *      At the end check if someone won or loose
     * @param playerBuild the Message subclass containing the whole information
     * @throws IllegalArgumentException if the cell is out of range
     */
    @Override
    public synchronized void build(PlayerBuild playerBuild) throws IllegalArgumentException {
        if(!turnCheck(playerBuild)){
            return;
        }

        if(playerBuild.getPlayer().getGodCard().checkBuilt(this, model.getBoard().getCell(playerBuild.getX(), playerBuild.getY()), playerBuild)){
            if(!playerBuild.getPlayer().getGodCard().handlerBuild(model, this, playerBuild, model.getBoard().getCell(playerBuild.getX(), playerBuild.getY()))){
                model.setNextMessageType(MessageType.MOVE);
                model.setNextPlayerMessage(PlayerMessage.MOVE);
                model.updatePhase();
                model.updateTurn();
                godIncreaseLevel(model.getBoard().getCell(playerBuild.getX(), playerBuild.getY()).getLevel().getBlockId(), model.getBoard().getCell(playerBuild.getX(), playerBuild.getY()));
            }

            playerBuild.getPlayer().getGodCard().afterBuildHandler(model, this, playerBuild, model.getBoard().getCell(playerBuild.getX(), playerBuild.getY()));

        } else{
            throw new IllegalArgumentException();
        }
        checkVictory();
        checkCantBuild(playerBuild);
    }

    /**
     * This method check all the cell around the worker are free and with a level lower then a dome
     * If true the cell is add to the available cells list.
     * @param worker the worker to control
     * @return a list of cells where a build can be done
     */
    public ArrayList<Cell> checkCanBuild(Worker worker){
        int x=worker.getCell().getX();
        int y=worker.getCell().getY();
        Board board=model.getBoard();
        ArrayList<Cell> availableCells=new ArrayList<>();
        for(int i=x-1; i<=x+1; i++){
            for(int j=y-1; j<=y+1; j++){
                try{
                    if(board.getCell(i,j).getLevel() != Blocks.DOME && board.getCell(i,j).isFree()){
                        availableCells.add(board.getCell(i,j));
                    }
                }catch(Exception e){
                    //ignore
                }
            }
        }
        return availableCells;
    }


    /**
     * Modified increase level for gods card.
     * In this moment the turn is already updated, a funtion to configure the god card turn start is called
     * At the end the superclass increase level is called
     * @param blockId the level id of the cell
     * @param buildingCell the cell where build
     */
    public void godIncreaseLevel(int blockId, Cell buildingCell) {
        model.getActualPlayer().getGodCard().turnStartHandler(this, blockId, buildingCell);
        super.increaseLevel(blockId, buildingCell);
    }

    /**
     * This function return the number of completed tower built in the board.
     * A complete tower is a build made from level 0 to dome without using god power
     * @return the number in the board
     */
    public synchronized int countTowers(){
        int counter=0;
        Board board=model.getBoard();
        for(int i=0; i<5;i++){
            for(int j=0; j<5; j++){
                if(board.getCell(i,j).isFull()){
                    counter++;
                }
            }
        }
        return counter;
    }

    /**
     * This method check call for each god card in the play the checkVictory method.
     * If none of them change game flow, the default function is recalled
     */
    @Override
    protected synchronized void checkVictory() {
        for(GodCard godCard : model.getGodsInPlay()){
            godCard.checkVictory(model, this);
        }
        super.checkVictory();
    }

    /**
     * This method is used to check which cells are available for a worker
     * @param actualWorker the worker in the bord where to check
     * @return an HasMap containing the cells and a boolean flag with the check result
     * The check is made by a god card function, by default, if a god card don't have a different implementation
     * the GodCard.checkCell call the controller chack cell {@link GodCard}
     */
    @Override
    protected synchronized HashMap<Cell, Boolean> checkCellsAround (Worker actualWorker){
        HashMap<Cell, Boolean> availableCells = new HashMap<>();
        Cell actualWorkerCell = actualWorker.getCell();
        Board board = model.getBoard();
        Player actualPlayer = model.getActualPlayer();
        int maxUpDifference=2;
        if (model.isMovedUp()){
            maxUpDifference=1;
        }

        for (int x = actualWorkerCell.getX() - 1; x <= actualWorkerCell.getX() + 1; x++) {
            for (int y = actualWorkerCell.getY() - 1; y <= actualWorkerCell.getY() + 1; y++) {
                try{
                    availableCells.put(board.getCell(x,y), actualPlayer.getGodCard().checkCell(this,x,y,actualWorker,maxUpDifference));
                }
                catch (IllegalArgumentException e){
                    Cell notAvailableCell = new Cell(x,y);
                    availableCells.put(notAvailableCell, false);
                }
            }
        }
        return availableCells;
    }

    /**
     *{@inheritDoc}
     */
    @Override
    public void update(Message msg) {//la update gestisce i messaggi
        msg.handler(this);
    }
}