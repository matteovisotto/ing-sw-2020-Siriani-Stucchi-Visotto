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
        if(!model.isPlayerTurn(playerWorker.getPlayer())){
            playerWorker.getView().reportError(PlayerMessage.TURN_ERROR);
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
                        if(model.getActualPlayer()==model.getGCPlayer(Gods.PROMETHEUS) && !model.getGCPlayer(Gods.PROMETHEUS).getGodCard().hasBuilt()){
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
            }
            else{
                playerWorker.getView().reportError("The cell is busy.");
            }
        }catch (IllegalArgumentException e){
            playerWorker.getView().reportError("Cell index must be between 0 and 4 (included)");
        }

    }

    public synchronized boolean checkPhase(){//deve controllare che la fase attuale sia la stessa del godpower
        Player actualPlayer = model.getActualPlayer();
        Phase actualPhase = actualPlayer.getGodCard().getPhase();
        return actualPhase == model.getPhase();
    }

    public synchronized void drawedCards(DrawedCards drawedCards){
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
            model.notifyMessage(PlayerMessage.PICK_CARD + "\n0 - " + model.getGods().get(0).getName() + "\n1 - " + model.getGods().get(1).getName());
        }
    }
    @Override
    protected synchronized boolean canMove(Worker actualWorker){
        Cell actualCell = actualWorker.getCell();
        for (int x = actualCell.getX() - 1; x <= actualCell.getX() + 1; x++) {
            for (int y = actualCell.getY() - 1; y <= actualCell.getY() + 1; y++) {
                if(x >= 0 && y >= 0 && x < 5 && y < 5){
                    Cell nextCell = model.getBoard().getCell(x,y);
                    if(nextCell.isFree() && !nextCell.equals(actualCell) && (nextCell.getLevel().getBlockId() -  actualCell.getLevel().getBlockId() < 2) && nextCell.getLevel().getBlockId() != 4){
                        return true;
                    }
                }

            }
        }
        return false;
    }
    @Override
    public synchronized void move(PlayerMove move) {
        if(!model.isPlayerTurn(move.getPlayer())){//se non è il turno del giocatore
            move.getView().reportError(PlayerMessage.TURN_ERROR);
            return;
        }

        //qua fa la mossa
        if(!move.getPlayer().getWorker(move.getWorkerId()).getStatus()){
            move.getView().reportError("This worker can't move anywhere");
            return;
        }
        HashMap<Cell, Boolean> availableCells = checkCellsAround(move.getPlayer().getWorker(move.getWorkerId()));

        try{
            if (availableCells.get(model.getBoard().getCell(move.getRow(), move.getColumn())) != null && availableCells.get(model.getBoard().getCell(move.getRow(), move.getColumn()))) {
                try {
                    model.setNextMessageType(MessageType.BUILD);
                    model.setNextPlayerMessage(PlayerMessage.BUILD);
                    model.setNextPhase(Phase.BUILD);
                    if(move.getPlayer().getGodCard().getCardGod() == Gods.APOLLO && !model.getBoard().getCell(move.getRow(), move.getColumn()).isFree()){
                        List<Object> objectList = new ArrayList<>();
                        //primo worker di quello che vuole muovere
                        objectList.add(move.getPlayer().getWorker(move.getWorkerId()));
                        for(int i = 0; i < model.getNumOfPlayers(); i++){
                            if(model.getPlayer(i).getGodCard().getCardGod() != Gods.APOLLO){
                                if(model.getPlayer(i).getWorker(0).getCell() == model.getBoard().getCell(move.getRow(), move.getColumn())){
                                    objectList.add(model.getPlayer(i).getWorker(0));
                                }
                                else if(model.getPlayer(i).getWorker(1).getCell() == model.getBoard().getCell(move.getRow(), move.getColumn())){
                                    objectList.add(model.getPlayer(i).getWorker(1));
                                }
                            }
                        }
                        move.getPlayer().getGodCard().usePower(objectList);
                        model.getActualPlayer().setUsedWorker(move.getWorkerId());
                        model.notifyChanges();
                    }
                    else if(move.getPlayer().getGodCard().getCardGod() == Gods.MINOTAUR && !model.getBoard().getCell(move.getRow(), move.getColumn()).isFree()){
                        List<Object> objectList = new ArrayList<>();
                        //primo worker di quello che vuole muovere
                        objectList.add(move.getPlayer().getWorker(move.getWorkerId()));
                        for(int i = 0; i < model.getNumOfPlayers(); i++){
                            if(model.getPlayer(i).getGodCard().getCardGod() != Gods.MINOTAUR){
                                //se uno dei due worker nemici sono coinvolti
                                if(model.getPlayer(i).getWorker(0).getCell() == model.getBoard().getCell(move.getRow(), move.getColumn())){
                                    objectList.add(model.getPlayer(i).getWorker(0));
                                }
                                else if(model.getPlayer(i).getWorker(1).getCell() == model.getBoard().getCell(move.getRow(), move.getColumn())){
                                    objectList.add(model.getPlayer(i).getWorker(1));
                                }
                            }
                        }
                        objectList.add(model.getBoard().getCell((((Worker)objectList.get(1)).getCell().getX() - ((Worker)objectList.get(0)).getCell().getX()) + ((Worker)objectList.get(1)).getCell().getX(), (((Worker)objectList.get(1)).getCell().getY() - ((Worker)objectList.get(0)).getCell().getY()) + ((Worker)objectList.get(1)).getCell().getY()));
                        move.getPlayer().getGodCard().usePower(objectList);
                        model.getActualPlayer().setUsedWorker(move.getWorkerId());
                        model.notifyChanges();
                    }
                    else if(model.getGCPlayer(Gods.PAN) == move.getPlayer()){// se è il turno del player con pan
                        if(model.getActualPlayer().getWorker(move.getWorkerId()).getCell().getLevel().getBlockId()-model.getBoard().getCell(move.getRow(), move.getColumn()).getLevel().getBlockId()==2){
                            model.victory(model.getActualPlayer());
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
                    else if(model.getGCPlayer(Gods.ARTHEMIS) == move.getPlayer()){
                        if(((Arthemis)move.getPlayer().getGodCard()).hasUsedPower()){
                            if(((Arthemis)move.getPlayer().getGodCard()).getPreviousWorker() != move.getPlayer().getWorker(move.getWorkerId())){
                                move.getView().reportError("you have to move the same worker");
                            }
                            else if(move.getPlayer().getGodCard().getFirstBuilt() == model.getBoard().getCell(move.getRow(), move.getColumn())){
                                move.getView().reportError("you can't move into the previous cell");
                            }
                            else{
                                ((Arthemis)move.getPlayer().getGodCard()).setUsedPower(false);
                                model.move(move);
                            }
                        }
                        else{
                            move.getPlayer().getGodCard().setFirstBuilt(model.getActualPlayer().getWorker(move.getWorkerId()).getCell());
                            ((Arthemis)move.getPlayer().getGodCard()).setPreviousWorker(model.getActualPlayer().getWorker(move.getWorkerId()));
                            model.setNextPhase(Phase.WAIT_GOD_ANSWER);
                            model.setNextPlayerMessage(PlayerMessage.USE_POWER);
                            model.setNextMessageType(MessageType.USE_POWER);
                            model.move(move);
                        }
                    }
                    else if(model.getGCPlayer(Gods.PROMETHEUS) == move.getPlayer()){// se è il turno del player con athena
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
                    else{
                        model.move(move);
                        if(model.getGCPlayer(Gods.ATLAS) == move.getPlayer()){
                            model.setNextPhase(Phase.WAIT_GOD_ANSWER);
                            model.setNextPlayerMessage(PlayerMessage.USE_POWER);
                            model.setNextMessageType(MessageType.USE_POWER);
                            model.notifyChanges();
                        }
                    }
                    if(model.getBoard().getCell(move.getRow(), move.getColumn()).getLevel().getBlockId() == 3){
                        model.victory(move.getPlayer());
                        if(model.getNumOfPlayers() == 2){
                            model.endGame();
                        }
                    }
                    else if (model.getGCPlayer(Gods.MINOTAUR) == move.getPlayer()){
                        Player[] players = model.getPlayers();
                        for (Player winnerPlayer : players) {
                            //controllo che il player non sia quello del turno
                            if (!winnerPlayer.getGodCard().getCardGod().equals(Gods.MINOTAUR)) {
                                for (int winnerWorker = 0; winnerWorker < 2; winnerWorker++) {
                                    if (winnerPlayer.getWorker(winnerWorker).getCell().getLevel().getBlockId() == 3) {
                                        model.victory(winnerPlayer);
                                        if (model.getNumOfPlayers() == 2) {
                                            model.endGame();
                                        }
                                    }
                                }
                            }
                        }
                    }
                    checkVictory();
                    //checkCantBuild(move);
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

    /*public synchronized void checkCantBuild(PlayerMove move){
        Cell cell=model.getBoard().getCell(move.getRow(), move.getColumn());
        Board board=model.getBoardClone();
        for (int x = cell.getX() - 1; x <= cell.getX() + 1; x++) {
            for (int y = cell.getY() - 1; y <= cell.getY() + 1; y++) {
                if((x>=0 && x<=4) && (y>=0 && y<=4)){
                    if(board.getCell(x,y).getLevel().getBlockId()!=4){
                        return;
                    }
                }
            }
        }
        model.loose(move.getPlayer());
    }*/

    @Override
    public synchronized void increaseLevel(PlayerBuild playerBuild) throws IllegalArgumentException {
        if(!model.isPlayerTurn(playerBuild.getPlayer())){//se non è il turno del giocatore
            playerBuild.getView().reportError(PlayerMessage.TURN_ERROR);
            return;
        }

        Cell buildingCell = this.model.getBoard().getCell(playerBuild.getX(), playerBuild.getY()); //ottengo la cella sulla quale costruire
        Blocks level = buildingCell.getLevel();//ottengo l'altezza della cella
        //qui devo fare i controlli

        if(     Math.abs(buildingCell.getX() - (playerBuild.getPlayer().getWorker(playerBuild.getWorkerId()).getCell().getX())) <= 1 &&
                Math.abs(buildingCell.getY() - (playerBuild.getPlayer().getWorker(playerBuild.getWorkerId()).getCell().getY())) <= 1 &&
                (playerBuild.getPlayer().getWorker(playerBuild.getWorkerId()).getCell() != buildingCell) &&
                (buildingCell.getX() >= 0 && buildingCell.getX() < 5) &&
                (buildingCell.getY() >= 0 && buildingCell.getY() < 5) &&
                (buildingCell.getLevel().getBlockId() <= 3) &&
                (buildingCell.isFree())
        ){
            if(model.getGCPlayer(Gods.ATLAS) == playerBuild.getPlayer() && ((Atlas)playerBuild.getPlayer().getGodCard()).hasUsedPower()){
                ((Atlas)playerBuild.getPlayer().getGodCard()).setUsedPower(false);
                model.increaseLevel(buildingCell, Blocks.DOME);
            }
            else if(model.getGCPlayer(Gods.DEMETER) == playerBuild.getPlayer()){
                if(((Demeter)playerBuild.getPlayer().getGodCard()).hasUsedPower()){
                    if(playerBuild.getPlayer().getGodCard().getFirstBuilt() == model.getBoard().getCell(playerBuild.getX(), playerBuild.getY())){
                        playerBuild.getView().reportError("You can't build into the previous cell");
                        return;
                    }
                    else{
                        ((Demeter)playerBuild.getPlayer().getGodCard()).setUsedPower(false);
                        model.setNextMessageType(MessageType.MOVE);
                        model.setNextPlayerMessage(PlayerMessage.MOVE);
                        model.updatePhase();
                        model.updateTurn();
                        build(level.getBlockId(), buildingCell);
                    }
                }
                else{
                    playerBuild.getPlayer().getGodCard().setFirstBuilt(model.getBoard().getCell(playerBuild.getX(), playerBuild.getY()));
                    model.setNextPhase(Phase.WAIT_GOD_ANSWER);
                    model.setNextPlayerMessage(PlayerMessage.USE_POWER);
                    model.setNextMessageType(MessageType.USE_POWER);
                    build(level.getBlockId(), buildingCell);
                }
            }
            else if(model.getGCPlayer(Gods.HEPHAESTUS)==playerBuild.getPlayer()) {
                if(model.getBoard().getCell(playerBuild.getX(), playerBuild.getY()).getLevel().getBlockId()<2){
                    playerBuild.getPlayer().getGodCard().setFirstBuilt(model.getBoard().getCell(playerBuild.getX(), playerBuild.getY()));
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
                build(level.getBlockId(), buildingCell);

            }
            else if(model.getGCPlayer(Gods.PROMETHEUS)==playerBuild.getPlayer()) {
                if(((Prometheus)playerBuild.getPlayer().getGodCard()).hasUsedPower()){
                    if(((Prometheus)playerBuild.getPlayer().getGodCard()).getWorkerID() != playerBuild.getWorkerId()){
                        playerBuild.getView().reportError("Utilizzare il worker precedentemente selezionato");
                        return;
                    }
                    if(!playerBuild.getPlayer().getGodCard().hasBuilt()){
                        model.setNextMessageType(MessageType.MOVE);
                        model.setNextPlayerMessage(PlayerMessage.MOVE);
                        model.setNextPhase(Phase.MOVE);
                        playerBuild.getPlayer().getGodCard().setBuild(true);
                    }
                    else{
                        ((Prometheus)playerBuild.getPlayer().getGodCard()).setUsedPower(false);
                        playerBuild.getPlayer().getGodCard().setBuild(false);
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
                build(level.getBlockId(), buildingCell);
            }
            else{
                model.setNextMessageType(MessageType.MOVE);
                model.setNextPlayerMessage(PlayerMessage.MOVE);
                model.updatePhase();
                model.updateTurn();
                build(level.getBlockId(), buildingCell);
            }
        }
        else{
            throw new IllegalArgumentException();
        }
        checkVictory();

    }

    private void build(int blockId, Cell buildingCell) {
        if(model.getActualPlayer() == model.getGCPlayer(Gods.PROMETHEUS) && !model.getGCPlayer(Gods.PROMETHEUS).getGodCard().hasBuilt()){
            model.setNextPhase(Phase.WAIT_GOD_ANSWER);
            model.setNextPlayerMessage(PlayerMessage.USE_POWER);
            model.setNextMessageType(MessageType.USE_POWER);
        }
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

    @Override
    protected synchronized HashMap<Cell, Boolean> checkCellsAround (Worker actualWorker){
        HashMap<Cell, Boolean> availableCells = new HashMap<>();
        Cell actualWorkerCell = actualWorker.getCell();
        Board board = model.getBoard();
        Player actualPlayer = model.getActualPlayer();

        if(model.getGCPlayer(Gods.APOLLO) == actualPlayer){
            for (int x = actualWorkerCell.getX() - 1; x <= actualWorkerCell.getX() + 1; x++) {
                for (int y = actualWorkerCell.getY() - 1; y <= actualWorkerCell.getY() + 1; y++) {
                    try{
                        if (model.isMovedUp()){
                            availableCells.put(board.getCell(x,y), board.checkCellApollo(x,y,actualWorker,1));
                        }
                        else {
                            availableCells.put(board.getCell(x,y), board.checkCellApollo(x,y,actualWorker));
                        }
                    }
                    catch (IllegalArgumentException e){
                        Cell notAvailableCell = new Cell(x,y);
                        availableCells.put(notAvailableCell, false);
                    }
                }
            }
        }
        else if(model.getGCPlayer(Gods.MINOTAUR) == actualPlayer){
            for (int x = actualWorkerCell.getX() - 1; x <= actualWorkerCell.getX() + 1; x++) {
                for (int y = actualWorkerCell.getY() - 1; y <= actualWorkerCell.getY() + 1; y++) {
                    try{
                        if (model.isMovedUp()){
                            availableCells.put(board.getCell(x,y), board.checkCellMinotaur(x,y,actualWorker,1));
                        }
                        else {
                            availableCells.put(board.getCell(x,y), board.checkCellMinotaur(x,y,actualWorker));
                        }
                    }
                    catch (IllegalArgumentException e){
                        Cell notAvailableCell = new Cell(x,y);
                        availableCells.put(notAvailableCell, false);
                    }
                }
            }
        }
        else{
            for (int x = actualWorkerCell.getX() - 1; x <= actualWorkerCell.getX() + 1; x++) {
                for (int y = actualWorkerCell.getY() - 1; y <= actualWorkerCell.getY() + 1; y++) {
                    try{
                        if (model.isMovedUp()){
                            availableCells.put(board.getCell(x,y), board.checkCell(x,y,actualWorker,1));
                        }
                        else{
                            availableCells.put(board.getCell(x,y), board.checkCell(x,y,actualWorker));
                        }
                    }
                    catch (IllegalArgumentException e){
                        Cell notAvailableCell = new Cell(x,y);
                        availableCells.put(notAvailableCell, false);
                    }
                }
            }
        }
        return availableCells;
    }

    @Override
    public synchronized void checkVictory(){
        int playerCantMove = 0;
        Player[] players = model.getPlayers();
        boolean[] playersBool = new boolean[model.getNumOfPlayers()];
        Arrays.fill(playersBool, false); //do per scontato che tutti i worker si possano muovere
        for(int i = 0; i < model.getNumOfPlayers(); i++){
            players[i].getWorker(0).setStatus(canMove(players[i].getWorker(0)));
            players[i].getWorker(1).setStatus(canMove(players[i].getWorker(1)));
            if(!players[i].getWorker(0).getStatus() && !players[i].getWorker(1).getStatus()){// controllo se nessun worker si può muovere
                playerCantMove++;
                playersBool[i] = true;
            }
        }
        if(playerCantMove == model.getNumOfPlayers() - 1){
            for(int i = 0; i < players.length; i++){
                if(!playersBool[i]){
                    model.victory(players[i]);
                    if(model.getLeftPlayers() == 2){
                        model.endGame();
                    }
                }
            }
        }
    }

    @Override
    public void update(Message msg) {//la update gestisce i messaggi
        msg.handler(this);
    }
}
