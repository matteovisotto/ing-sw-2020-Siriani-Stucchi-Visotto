package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.messageModel.*;
import it.polimi.ingsw.model.simplegod.*;
import it.polimi.ingsw.utils.PlayerMessage;

import java.util.*;

public class GodCardController extends Controller{


    public GodCardController(Model model) {
        super(model);
    }

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

    @Override
    public synchronized void move(PlayerMove move) {
        int oldLevel=model.getActualPlayer().getWorker(move.getWorkerId()).getCell().getLevel().getBlockId();
        if(!turnCheck(move)){
            return;
        }
        if(!canMove(move.getPlayer().getWorker(move.getWorkerId()),move.getPlayer()) && model.getGCPlayer(Gods.TRITON)==move.getPlayer()){
            if(((Triton)move.getPlayer().getGodCard()).getUsedWorkerID()!=-1)
                model.loose(move.getPlayer());
        }
        if(model.getGCPlayer(Gods.ATHENA) == move.getPlayer()){
            model.setMovedUp(false);
        }

        HashMap<Cell, Boolean> availableCells = checkCellsAround(move.getPlayer().getWorker(move.getWorkerId()));

        try{
            if (availableCells.get(model.getBoard().getCell(move.getRow(), move.getColumn())) != null && availableCells.get(model.getBoard().getCell(move.getRow(), move.getColumn()))) {
                try {
                    model.setNextMessageType(MessageType.BUILD);
                    model.setNextPlayerMessage(PlayerMessage.BUILD);
                    model.setNextPhase(Phase.BUILD);
                    if((move.getPlayer().getGodCard().getCardGod() == Gods.APOLLO || move.getPlayer().getGodCard().getCardGod() == Gods.MINOTAUR) && !model.getBoard().getCell(move.getRow(), move.getColumn()).isFree()){
                        List<Object> objectList = new ArrayList<>();
                        //primo worker di quello che vuole muovere
                        objectList.add(move.getPlayer().getWorker(move.getWorkerId()));
                        for(int i = 0; i < model.getNumOfPlayers(); i++){
                            if(model.getPlayer(i).getGodCard().getCardGod() != move.getPlayer().getGodCard().getCardGod()){
                                if(model.getPlayer(i).getWorker(0).getCell() == model.getBoard().getCell(move.getRow(), move.getColumn())){
                                    objectList.add(model.getPlayer(i).getWorker(0));
                                }
                                else if(model.getPlayer(i).getWorker(1).getCell() == model.getBoard().getCell(move.getRow(), move.getColumn())){
                                    objectList.add(model.getPlayer(i).getWorker(1));
                                }
                            }
                        }
                        //QUESTO IF AGGIUNGE LA BEHIND CELL SE HO MINOTAUR
                        if(move.getPlayer().getGodCard().getCardGod() == Gods.MINOTAUR){
                            objectList.add(model.getBoard().getCell((((Worker)objectList.get(1)).getCell().getX() - ((Worker)objectList.get(0)).getCell().getX()) + ((Worker)objectList.get(1)).getCell().getX(), (((Worker)objectList.get(1)).getCell().getY() - ((Worker)objectList.get(0)).getCell().getY()) + ((Worker)objectList.get(1)).getCell().getY()));
                        }
                        move.getPlayer().getGodCard().usePower(objectList);
                        model.getActualPlayer().setUsedWorker(move.getWorkerId());
                        model.notifyChanges();
                    }
                    else if(model.getGCPlayer(Gods.PAN) == move.getPlayer()){// se è il turno del player con pan
                        if(model.getActualPlayer().getWorker(move.getWorkerId()).getCell().getLevel().getBlockId()-model.getBoard().getCell(move.getRow(), move.getColumn()).getLevel().getBlockId()>=2){
                            model.victory(move.getPlayer());
                            return;
                        }
                        model.move(move);
                    }
                    else if(model.getGCPlayer(Gods.ATHENA) == move.getPlayer()){// se è il turno del player con athena
                        if(model.getBoard().getCell(move.getRow(), move.getColumn()).getLevel().getBlockId() > model.getActualPlayer().getWorker(move.getWorkerId()).getCell().getLevel().getBlockId()){
                            move.getPlayer().getGodCard().usePower(new ArrayList<Object>(Collections.singletonList(model)));
                        }
                        else{
                            model.setMovedUp(false);
                        }
                        model.move(move);
                    }
                    else if(model.getGCPlayer(Gods.ARTEMIS) == move.getPlayer()){
                        if(!canMove(move.getPlayer().getWorker(move.getWorkerId()), move.getPlayer())){
                            model.loose(move.getPlayer());
                        }
                        if(((Artemis)move.getPlayer().getGodCard()).hasUsedPower()){
                            if(((Artemis)move.getPlayer().getGodCard()).getPreviousWorker() != move.getPlayer().getWorker(move.getWorkerId())){
                                move.getView().reportError("You have to move the same worker");
                            }
                            else if(((Artemis)move.getPlayer().getGodCard()).getFirstMove() == model.getBoard().getCell(move.getRow(), move.getColumn())){
                                move.getView().reportError("You can't move into the previous cell");
                            }
                            else{
                                ((Artemis)move.getPlayer().getGodCard()).setUsedPower(false);
                                model.move(move);
                            }
                        }
                        else{
                            ((Artemis)move.getPlayer().getGodCard()).setFirstMove(model.getActualPlayer().getWorker(move.getWorkerId()).getCell());
                            ((Artemis)move.getPlayer().getGodCard()).setPreviousWorker(model.getActualPlayer().getWorker(move.getWorkerId()));
                            model.setNextPhase(Phase.WAIT_GOD_ANSWER);
                            model.setNextPlayerMessage(PlayerMessage.USE_POWER);
                            model.setNextMessageType(MessageType.USE_POWER);
                            model.move(move);
                        }
                    }
                    else if(model.getGCPlayer(Gods.PROMETHEUS) == move.getPlayer()){// se è il turno del player con Prometheus
                        if(((Prometheus)move.getPlayer().getGodCard()).hasUsedPower()){
                            if(((Prometheus)move.getPlayer().getGodCard()).getWorkerID() != move.getWorkerId()){
                                move.getView().reportError("you have to move the same worker");
                            }
                            else if(move.getPlayer().getWorker(move.getWorkerId()).getCell().getLevel().getBlockId() < model.getBoard().getCell(move.getRow(), move.getColumn()).getLevel().getBlockId()){
                                move.getView().reportError("you can't move up");
                            }
                            else{
                                model.setNextMessageType(MessageType.BUILD);
                                model.setNextPlayerMessage(PlayerMessage.BUILD);
                                model.setNextPhase(Phase.BUILD);
                                model.move(move);
                            }
                        }
                        else{
                            model.setNextMessageType(MessageType.BUILD);
                            model.setNextPlayerMessage(PlayerMessage.BUILD);
                            model.setNextPhase(Phase.BUILD);
                            model.move(move);
                        }
                    }
                    else if(model.getGCPlayer(Gods.TRITON) == move.getPlayer()){// se è il turno del player con Tritone
                        if(((Triton)move.getPlayer().getGodCard()).getUsedWorkerID()==-1){
                            ((Triton)move.getPlayer().getGodCard()).setUsedWorkerID(move.getWorkerId());
                        }
                        if(((Triton)move.getPlayer().getGodCard()).getUsedWorkerID() != move.getWorkerId()){
                            move.getView().reportError("you have to move the same worker");
                        }
                        else if(move.getRow()==0 || move.getRow()==4 || move.getColumn()==0 || move.getColumn()==4 ){
                            model.setNextPhase(Phase.WAIT_GOD_ANSWER);
                            model.setNextPlayerMessage(PlayerMessage.USE_POWER);
                            model.setNextMessageType(MessageType.USE_POWER);
                            model.move(move);
                        }
                        else{
                            ((Triton)move.getPlayer().getGodCard()).setUsedWorkerID(-1);
                            model.setNextMessageType(MessageType.BUILD);
                            model.setNextPlayerMessage(PlayerMessage.BUILD);
                            model.setNextPhase(Phase.BUILD);
                            model.move(move);
                        }

                    }
                    else{
                        if(model.getGCPlayer(Gods.ATLAS) == move.getPlayer()){
                            model.setNextPhase(Phase.WAIT_GOD_ANSWER);
                            model.setNextPlayerMessage(PlayerMessage.USE_POWER);
                            model.setNextMessageType(MessageType.USE_POWER);
                        }
                        model.move(move);
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

    @Override
    protected synchronized boolean canMove(Worker worker, Player player){
        HashMap<Cell, Boolean> availableCells=checkCellsAround(worker);
        for (Boolean can:availableCells.values()) {
            if(can){
                return true;
            }
        }
        return false;
    }

    @Override
    protected synchronized boolean checkBuild(Cell buildingCell, PlayerBuild playerBuild) {
        if(playerBuild.getPlayer().getGodCard().getCardGod()==Gods.HESTIA){
            if(((Hestia)playerBuild.getPlayer().getGodCard()).hasBuilt()){
                return Math.abs(buildingCell.getX() - (playerBuild.getPlayer().getWorker(playerBuild.getWorkerId()).getCell().getX())) <= 1 &&
                        Math.abs(buildingCell.getY() - (playerBuild.getPlayer().getWorker(playerBuild.getWorkerId()).getCell().getY())) <= 1 &&
                        (playerBuild.getPlayer().getWorker(playerBuild.getWorkerId()).getCell() != buildingCell) &&
                        (buildingCell.getX() > 0 && buildingCell.getX() < 4) &&
                        (buildingCell.getY() > 0 && buildingCell.getY() < 4) &&
                        (buildingCell.getLevel().getBlockId() <= 3) &&
                        (buildingCell.isFree());
            }
        }
        if(playerBuild.getPlayer().getGodCard().getCardGod()==Gods.ZEUS){
            return Math.abs(buildingCell.getX() - (playerBuild.getPlayer().getWorker(playerBuild.getWorkerId()).getCell().getX())) <= 1 &&
                    Math.abs(buildingCell.getY() - (playerBuild.getPlayer().getWorker(playerBuild.getWorkerId()).getCell().getY())) <= 1 &&
                    (buildingCell.getX() >= 0 && buildingCell.getX() < 5) &&
                    (buildingCell.getY() >= 0 && buildingCell.getY() < 5) &&
                    (buildingCell.getLevel().getBlockId() <= 2);
        }
        return super.checkBuild(buildingCell, playerBuild);

    }

    @Override
    public synchronized void build(PlayerBuild playerBuild) throws IllegalArgumentException {
        if(!turnCheck(playerBuild)){
            return;
        }

        Cell buildingCell = this.model.getBoard().getCell(playerBuild.getX(), playerBuild.getY()); //ottengo la cella sulla quale costruire
        Blocks level = buildingCell.getLevel();//ottengo l'altezza della cella

        if(checkBuild(buildingCell, playerBuild)){
            if(model.getGCPlayer(Gods.ATLAS) == playerBuild.getPlayer() && ((Atlas)playerBuild.getPlayer().getGodCard()).hasUsedPower()){
                ((Atlas)playerBuild.getPlayer().getGodCard()).setUsedPower(false);
                model.setNextMessageType(MessageType.MOVE);
                model.setNextPlayerMessage(PlayerMessage.MOVE);
                model.updatePhase();
                model.updateTurn();
                model.increaseLevel(buildingCell, Blocks.DOME);

            }
            else if(model.getGCPlayer(Gods.DEMETER) == playerBuild.getPlayer()){
                if(((Demeter)playerBuild.getPlayer().getGodCard()).hasUsedPower()){
                    if(((Demeter)playerBuild.getPlayer().getGodCard()).getFirstBuild() == model.getBoard().getCell(playerBuild.getX(), playerBuild.getY())){
                        playerBuild.getView().reportError("You can't build into the previous cell");
                        return;
                    }
                    else{
                        ((Demeter)playerBuild.getPlayer().getGodCard()).setUsedPower(false);
                        model.setNextMessageType(MessageType.MOVE);
                        model.setNextPlayerMessage(PlayerMessage.MOVE);
                        model.updatePhase();
                        model.updateTurn();
                        godIncreaseLevel(level.getBlockId(), buildingCell);
                    }
                }
                else{
                    ((Demeter)playerBuild.getPlayer().getGodCard()).setFirstBuilt(model.getBoard().getCell(playerBuild.getX(), playerBuild.getY()));
                    model.setNextPhase(Phase.WAIT_GOD_ANSWER);
                    model.setNextPlayerMessage(PlayerMessage.USE_POWER);
                    model.setNextMessageType(MessageType.USE_POWER);
                    godIncreaseLevel(level.getBlockId(), buildingCell);
                }
            }
            else if(model.getGCPlayer(Gods.HEPHAESTUS) == playerBuild.getPlayer()) {
                if(model.getBoard().getCell(playerBuild.getX(), playerBuild.getY()).getLevel().getBlockId()<2){
                    ((Hephaestus)playerBuild.getPlayer().getGodCard()).setFirstBuilt(model.getBoard().getCell(playerBuild.getX(), playerBuild.getY()));
                    model.setNextPhase(Phase.WAIT_GOD_ANSWER);
                    model.setNextPlayerMessage(PlayerMessage.USE_POWER);
                    model.setNextMessageType(MessageType.USE_POWER);
                }
                else{
                    model.setNextMessageType(MessageType.MOVE);
                    model.setNextPlayerMessage(PlayerMessage.MOVE);
                    model.updatePhase();
                    model.updateTurn();
                }
                godIncreaseLevel(level.getBlockId(), buildingCell);

            }
            else if(model.getGCPlayer(Gods.PROMETHEUS)==playerBuild.getPlayer()) {
                if(((Prometheus)playerBuild.getPlayer().getGodCard()).hasUsedPower()){
                    if(((Prometheus)playerBuild.getPlayer().getGodCard()).getWorkerID() != playerBuild.getWorkerId()){
                        playerBuild.getView().reportError("You have to use the same worker.");
                        return;
                    }
                    if(!((Prometheus)playerBuild.getPlayer().getGodCard()).hasBuilt()){
                        model.setNextMessageType(MessageType.MOVE);
                        model.setNextPlayerMessage(PlayerMessage.MOVE);
                        model.setNextPhase(Phase.MOVE);
                        ((Prometheus)playerBuild.getPlayer().getGodCard()).setBuild(true);
                    }
                    else{
                        ((Prometheus)playerBuild.getPlayer().getGodCard()).setUsedPower(false);
                        ((Prometheus)playerBuild.getPlayer().getGodCard()).setBuild(false);
                        model.setNextMessageType(MessageType.MOVE);
                        model.setNextPlayerMessage(PlayerMessage.MOVE);
                        model.setNextPhase(Phase.MOVE);
                        model.updateTurn();
                    }
                }
                else{
                    model.setNextMessageType(MessageType.MOVE);
                    model.setNextPlayerMessage(PlayerMessage.MOVE);
                    model.updatePhase();
                    model.updateTurn();
                }
                godIncreaseLevel(level.getBlockId(), buildingCell);
                if(!canMove(playerBuild.getPlayer().getWorker(playerBuild.getWorkerId()), playerBuild.getPlayer())){
                    if(playerBuild.getPlayer().getGodCard().getCardGod()==Gods.PROMETHEUS){
                        if(((Prometheus)playerBuild.getPlayer().getGodCard()).hasBuilt()){
                            model.loose(playerBuild.getPlayer());
                        }
                    }
                    playerBuild.getView().reportError("This worker can't move anywhere");
                    return;
                }
            }
            else if(model.getGCPlayer(Gods.HESTIA)==playerBuild.getPlayer()){


                if(!(((Hestia)model.getGCPlayer(Gods.HESTIA).getGodCard()).hasBuilt()) && checkHestiaCells(playerBuild)){
                    model.setNextPhase(Phase.WAIT_GOD_ANSWER);
                    model.setNextPlayerMessage(PlayerMessage.USE_POWER);
                    model.setNextMessageType(MessageType.USE_POWER);
                }
                else{
                    ((Hestia)playerBuild.getPlayer().getGodCard()).setHasBuilt(false);
                    model.setNextMessageType(MessageType.MOVE);
                    model.setNextPlayerMessage(PlayerMessage.MOVE);
                    model.updatePhase();
                    model.updateTurn();
                }
                godIncreaseLevel(level.getBlockId(), buildingCell);
            }
            else if(model.getGCPlayer(Gods.POSEIDON)==playerBuild.getPlayer()){
                Poseidon poseidon= (Poseidon) model.getGCPlayer(Gods.POSEIDON).getGodCard();
                if(poseidon.getMovedWorker()==null){
                    poseidon.setMovedWorker(playerBuild.getPlayer().getWorker(playerBuild.getWorkerId()));
                    poseidon.setUnusedWorker(playerBuild.getPlayer().getWorker((playerBuild.getWorkerId()+1)%2));
                    model.getGCPlayer(Gods.POSEIDON).setUsedWorker((playerBuild.getWorkerId()+1)%2);
                }
                //se non ho costruito già 3 volte e se il worker inutilizzato e' al livello 0 e se il worker inutilizzato puo costruire
                if(poseidon.getNumOfBuild()<3 && poseidon.getUnusedWorker().getCell().getLevel().getBlockId()==0 && checkCanBuild(poseidon.getUnusedWorker())){
                    model.setNextPhase(Phase.WAIT_GOD_ANSWER);
                    model.setNextPlayerMessage(PlayerMessage.USE_POWER);
                    model.setNextMessageType(MessageType.USE_POWER);
                }
                else{
                    ((Poseidon)model.getGCPlayer(Gods.POSEIDON).getGodCard()).setUnusedWorker(null);
                    ((Poseidon)model.getGCPlayer(Gods.POSEIDON).getGodCard()).setMovedWorker(null);
                    ((Poseidon)model.getGCPlayer(Gods.POSEIDON).getGodCard()).resetBuild();
                    model.setNextMessageType(MessageType.MOVE);
                    model.setNextPlayerMessage(PlayerMessage.MOVE);
                    model.updatePhase();
                    model.updateTurn();
                }
                godIncreaseLevel(level.getBlockId(), buildingCell);
            }
            else{
                model.setNextMessageType(MessageType.MOVE);
                model.setNextPlayerMessage(PlayerMessage.MOVE);
                model.updatePhase();
                model.updateTurn();
                godIncreaseLevel(level.getBlockId(), buildingCell);
            }
        }
        else{
            throw new IllegalArgumentException();
        }
        checkVictory();
        checkCantBuild(playerBuild);
    }

    private boolean checkCanBuild(Worker worker){
        int x=worker.getCell().getX();
        int y=worker.getCell().getY();
        Board board=model.getBoard();
        for(int i=x-1; i<=x+1; i++){
            for(int j=y-1; j<=y+1; j++){
                try{
                    if(board.getCell(i,j).getLevel() != Blocks.DOME && board.getCell(i,j).isFree()){
                        return true;
                    }
                }catch(Exception e){
                    //ignore
                }
            }
        }
        return false;
    }

    private boolean checkHestiaCells(PlayerBuild playerBuild){
        int x=playerBuild.getPlayer().getWorker(playerBuild.getWorkerId()).getCell().getX();
        int y=playerBuild.getPlayer().getWorker(playerBuild.getWorkerId()).getCell().getY();
        Board board=model.getBoard();
        for(int i=x-1; i<=x+1; i++){
            for(int j=y-1; j<=y+1; j++){
                try{
                    if(board.getCell(i,j).getLevel().getBlockId() < 4 && i!=0 && i!=4 && j!=0 && j!=4 && board.getCell(i,j).isFree()){
                        return true;
                    }
                }catch(Exception e){
                    //ignore
                }
            }
        }
        return false;
    }

    private void godIncreaseLevel(int blockId, Cell buildingCell) {
        if((model.getActualPlayer() == model.getGCPlayer(Gods.PROMETHEUS) && !((Prometheus)model.getGCPlayer(Gods.PROMETHEUS).getGodCard()).hasBuilt())) {
            model.setNextPhase(Phase.WAIT_GOD_ANSWER);
            model.setNextPlayerMessage(PlayerMessage.USE_POWER);
            model.setNextMessageType(MessageType.USE_POWER);
        }
        super.increaseLevel(blockId, buildingCell);
    }

    private synchronized int countTowers(){
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

    @Override
    protected synchronized void checkVictory() {
        if(model.getGCPlayer(Gods.CHRONUS)!=null && !model.getGCPlayer(Gods.CHRONUS).hasWon() && !model.getGCPlayer(Gods.CHRONUS).getHasLost()){
            if(countTowers()>=5){
                model.victory(model.getGCPlayer(Gods.CHRONUS));
            }
        }
        super.checkVictory();
    }

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
                    switch(actualPlayer.getGodCard().getCardGod()){
                        case APOLLO:
                            availableCells.put(board.getCell(x,y), board.checkCellApollo(x,y,actualWorker,maxUpDifference));
                            break;
                        case MINOTAUR:
                            availableCells.put(board.getCell(x,y), board.checkCellMinotaur(x,y,actualWorker,maxUpDifference));
                            break;
                        default:
                            availableCells.put(board.getCell(x,y), board.checkCell(x,y,actualWorker,maxUpDifference));
                    }
                }
                catch (IllegalArgumentException e){
                    Cell notAvailableCell = new Cell(x,y);
                    availableCells.put(notAvailableCell, false);
                }
            }
        }
        return availableCells;
    }

    @Override
    public void update(Message msg) {//la update gestisce i messaggi
        msg.handler(this);
    }
}
