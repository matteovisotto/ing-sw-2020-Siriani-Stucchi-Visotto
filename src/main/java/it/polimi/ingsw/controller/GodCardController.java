package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.messageModel.*;
import it.polimi.ingsw.model.simplegod.Arthemis;
import it.polimi.ingsw.model.simplegod.Atlas;
import it.polimi.ingsw.model.simplegod.Demeter;
import it.polimi.ingsw.model.simplegod.Hephaestus;
import it.polimi.ingsw.utils.PlayerMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class GodCardController extends Controller{


    public GodCardController(Model model) {
        super(model);
    }

    public synchronized boolean checkPhase(){//deve controllare che la fase attuale sia la stessa del godpower
        Player p = model.getActualPlayer();
        Phase ph = p.getGodCard().getPhase();
        return ph == model.getPhase();
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
        if(model.getNumOfPlayers()==3){
            model.notifyMessage(Gods.getGod(drawedCards.getThird()).getName());
        }
        model.updatePhase();
        model.updateTurn();
        model.setNextMessageType(MessageType.PICK_CARD);

        if(model.getNumOfPlayers()==3){
            model.setNextPlayerMessage("Pick a God between: \n0 - "+ Gods.getGod(drawedCards.getFirst()).getName()+"\n1 - "+ Gods.getGod(drawedCards.getSecond()).getName()+"\n2 - "+ Gods.getGod(drawedCards.getThird()).getName());
        }
        else{
            model.setNextPlayerMessage("Pick a God between: \n0 - "+ Gods.getGod(drawedCards.getFirst()).getName()+"\n1 - "+ Gods.getGod(drawedCards.getSecond()).getName());
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
            model.notifyMessage("You selected "+godName+", good luck!");
        }
        else{
            pickedCard.getView().reportError("The selected god isn't available");
            return;
        }
        if(model.getLeftCards() == 1){
            model.updateTurn();
            String godName = model.assignCard(model.getActualPlayer(),0).getName();
            model.notifyMessage("Only "+godName+" was left, that's your card, good luck!");
            model.updatePhase();
            model.setNextMessageType(MessageType.SET_WORKER_1);
            model.setNextPlayerMessage(PlayerMessage.PLACE_FIRST_WORKER);
            model.notifyChanges();
        }
        else{
            model.updateTurn();
            model.notifyMessage(PlayerMessage.PICK_CARD+"\n0 - "+model.getGods().get(0).getName()+"\n1 - "+model.getGods().get(1).getName());
        }
    }
    @Override
    protected synchronized boolean canMove(Worker worker){
        Cell other_cell= worker.getCell();
        for (int x = other_cell.getX() - 1; x <= other_cell.getX() + 1; x++) {
            for (int y = other_cell.getY() - 1; y <= other_cell.getY() + 1; y++) {
                if(x>=0 && y>=0 && x<5 && y<5){
                    Cell cell=model.getBoard().getCell(x,y);
                    if(cell.isFree() && !cell.equals(other_cell) && (cell.getLevel().getBlockId() -  other_cell.getLevel().getBlockId()< 2) && cell.getLevel().getBlockId() != 4){
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
                    model.updatePhase();
                    if(move.getPlayer().getGodCard().getCardGod() == Gods.APOLLO && !model.getBoard().getCell(move.getRow(), move.getColumn()).isFree()){
                        List<Object> objectList= new ArrayList<>();
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
                        List<Object> objectList= new ArrayList<>();
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
                        objectList.add(model.getBoard().getCell((((Worker)objectList.get(1)).getCell().getX()-((Worker)objectList.get(0)).getCell().getX())+((Worker)objectList.get(1)).getCell().getX(), (((Worker)objectList.get(1)).getCell().getY()-((Worker)objectList.get(0)).getCell().getY())+((Worker)objectList.get(1)).getCell().getY()));
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
                        if(model.getBoard().getCell(move.getRow(), move.getColumn()).getLevel().getBlockId()>model.getActualPlayer().getWorker(move.getWorkerId()).getCell().getLevel().getBlockId()){
                            move.getPlayer().getGodCard().usePower(new ArrayList<Object>(Arrays.asList(model)));
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
                            else if(((Arthemis)move.getPlayer().getGodCard()).getFirstBuilt() == model.getBoard().getCell(move.getRow(), move.getColumn())){
                                move.getView().reportError("you can't move into the previous cell");
                            }
                            else{
                                ((Arthemis)move.getPlayer().getGodCard()).setUsedPower(false);
                                model.move(move);
                            }
                        }
                        else{
                            ((Arthemis)move.getPlayer().getGodCard()).setFirstBuilt(model.getActualPlayer().getWorker(move.getWorkerId()).getCell());
                            ((Arthemis)move.getPlayer().getGodCard()).setPreviousWorker(model.getActualPlayer().getWorker(move.getWorkerId()));
                            model.setNextPhase(Phase.WAIT_GOD_ANSWER);
                            model.setNextPlayerMessage(PlayerMessage.USE_POWER);
                            model.setNextMessageType(MessageType.USE_POWER);
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
                    if(model.getBoard().getCell(move.getRow(), move.getColumn()).getLevel().getBlockId()==3){
                        model.victory(move.getPlayer());
                        if(model.getNumOfPlayers()==2){
                            model.endGame();
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

        Cell cell=this.model.getBoard().getCell(playerBuild.getX(), playerBuild.getY()); //ottengo la cella sulla quale costruire
        Blocks level = cell.getLevel();//ottengo l'altezza della cella
        //qui devo fare i controlli
        if(     Math.abs(cell.getX() - (playerBuild.getPlayer().getWorker(playerBuild.getWorkerId()).getCell().getX())) <= 1 &&
                Math.abs(cell.getY() - (playerBuild.getPlayer().getWorker(playerBuild.getWorkerId()).getCell().getY())) <= 1 &&
                (playerBuild.getPlayer().getWorker(playerBuild.getWorkerId()).getCell()!=cell) &&
                (cell.getX()>=0 && cell.getX()<5) &&
                (cell.getY()>=0 && cell.getY()<5) &&
                (cell.getLevel().getBlockId()<=3) &&
                (cell.isFree())
        ){
            if(model.getGCPlayer(Gods.ATLAS)==playerBuild.getPlayer() && ((Atlas)playerBuild.getPlayer().getGodCard()).hasUsedPower()){
                ((Atlas)playerBuild.getPlayer().getGodCard()).setUsedPower(false);
                model.increaseLevel(cell, Blocks.DOME);
            }
            else if(model.getGCPlayer(Gods.DEMETER)==playerBuild.getPlayer()){
                if(((Demeter)playerBuild.getPlayer().getGodCard()).hasUsedPower()){
                    if(((Demeter)playerBuild.getPlayer().getGodCard()).getFirstBuilt() == model.getBoard().getCell(playerBuild.getX(), playerBuild.getY())){
                        playerBuild.getView().reportError("you can't build into the previous cell");
                        return;
                    }
                    else{
                        ((Demeter)playerBuild.getPlayer().getGodCard()).setUsedPower(false);
                        model.setNextMessageType(MessageType.MOVE);
                        model.setNextPlayerMessage(PlayerMessage.MOVE);
                        model.updatePhase();
                        model.updateTurn();
                        build(level.getBlockId(), cell);
                    }
                }
                else{
                    ((Demeter)playerBuild.getPlayer().getGodCard()).setFirstBuilt(model.getBoard().getCell(playerBuild.getX(), playerBuild.getY()));
                    model.setNextPhase(Phase.WAIT_GOD_ANSWER);
                    model.setNextPlayerMessage(PlayerMessage.USE_POWER);
                    model.setNextMessageType(MessageType.USE_POWER);
                    build(level.getBlockId(), cell);
                }
            }
            else if(model.getGCPlayer(Gods.HEPHAESTUS)==playerBuild.getPlayer()) {
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
                build(level.getBlockId(), cell);

            }
            else{
                model.setNextMessageType(MessageType.MOVE);
                model.setNextPlayerMessage(PlayerMessage.MOVE);
                model.updatePhase();
                model.updateTurn();
                build(level.getBlockId(), cell);
            }
        }
        else{
            throw new IllegalArgumentException();
        }
        checkVictory();

    }

    private void build(int blockId, Cell cell) {
        switch(blockId) {
            case 0:
                model.increaseLevel(cell, Blocks.LEVEL1);
                break;
            case 1:
                model.increaseLevel(cell, Blocks.LEVEL2);break;
            case 2:
                model.increaseLevel(cell, Blocks.LEVEL3);break;
            case 3:
                model.increaseLevel(cell, Blocks.DOME);break;
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    protected synchronized HashMap<Cell, Boolean> checkCellsAround (Worker worker){
        HashMap<Cell, Boolean> availableCells = new HashMap<>();
        Cell cell = worker.getCell();
        Board board = model.getBoard();
        Player player=model.getActualPlayer();

        if(model.getGCPlayer(Gods.APOLLO) == player){
            for (int x = cell.getX() - 1; x <= cell.getX() + 1; x++) {
                for (int y = cell.getY() - 1; y <= cell.getY() + 1; y++) {
                    try{
                        if (model.isMovedUp()){
                            availableCells.put(board.getCell(x,y), board.checkCellApollo(x,y,worker,1));
                        }
                        else {
                            availableCells.put(board.getCell(x,y), board.checkCellApollo(x,y,worker));
                        }
                    }
                    catch (IllegalArgumentException e){
                        Cell c= new Cell(x,y);
                        availableCells.put(c, false);
                    }
                }
            }
        }
        else if(model.getGCPlayer(Gods.MINOTAUR) == player){
            for (int x = cell.getX() - 1; x <= cell.getX() + 1; x++) {
                for (int y = cell.getY() - 1; y <= cell.getY() + 1; y++) {
                    try{
                        if (model.isMovedUp()){
                            availableCells.put(board.getCell(x,y), board.checkCellMinotaur(x,y,worker,1));
                        }
                        else {
                            availableCells.put(board.getCell(x,y), board.checkCellMinotaur(x,y,worker));
                        }
                    }
                    catch (IllegalArgumentException e){
                        Cell c= new Cell(x,y);
                        availableCells.put(c, false);
                    }
                }
            }
        }
        else{
            for (int x = cell.getX() - 1; x <= cell.getX() + 1; x++) {
                for (int y = cell.getY() - 1; y <= cell.getY() + 1; y++) {
                    try{
                        if (model.isMovedUp()){
                            availableCells.put(board.getCell(x,y), board.checkCell(x,y,worker,1));
                        }
                        else{
                            availableCells.put(board.getCell(x,y), board.checkCell(x,y,worker));
                        }
                    }
                    catch (IllegalArgumentException e){
                        Cell c= new Cell(x,y);
                        availableCells.put(c, false);
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
        if(playerCantMove == model.getNumOfPlayers()-1){
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
