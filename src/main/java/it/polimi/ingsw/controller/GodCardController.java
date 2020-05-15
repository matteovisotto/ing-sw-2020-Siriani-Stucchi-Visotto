package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.messageModel.*;
import it.polimi.ingsw.utils.PlayerMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class GodCardController extends Controller{


    public GodCardController(Model model) {
        super(model);
    }

    private synchronized boolean checkPhase(){//deve controllare che la fase attuale sia la stessa del godpower
        Player p = model.getActualPlayer();
        Phase ph = p.getGodCard().getPhase();
        return ph == model.getPhase();
    }

    public synchronized void drawedCards(DrawedCards drawedCards){
        if(drawedCards.getThird() != -1 && model.getNumOfPlayers() == 3){
            model.addGod(SimpleGods.getGod(drawedCards.getFirst()));
            model.addGod(SimpleGods.getGod(drawedCards.getSecond()));
            model.addGod(SimpleGods.getGod(drawedCards.getThird()));
        }
        else if(model.getNumOfPlayers() == 2 && drawedCards.getThird() != -1){
            drawedCards.getView().reportError("Insert 2 god cards only");
            return;
        }
        else{
            model.addGod(SimpleGods.getGod(drawedCards.getFirst()));
            model.addGod(SimpleGods.getGod(drawedCards.getSecond()));
        }
        model.notifyMessage(SimpleGods.getGod(drawedCards.getFirst()).getName());
        model.notifyMessage(SimpleGods.getGod(drawedCards.getSecond()).getName());
        if(model.getNumOfPlayers()==3){
            model.notifyMessage(SimpleGods.getGod(drawedCards.getThird()).getName());
        }
        model.updatePhase();
        model.updateTurn();
        model.setNextMessageType(MessageType.PICK_CARD);

        if(model.getNumOfPlayers()==3){
            model.setNextPlayerMessage("Pick a God between: \n0 - "+SimpleGods.getGod(drawedCards.getFirst()).getName()+"\n1 - "+SimpleGods.getGod(drawedCards.getSecond()).getName()+"\n2 - "+SimpleGods.getGod(drawedCards.getThird()).getName());
        }
        else{
            model.setNextPlayerMessage("Pick a God between: \n0 - "+SimpleGods.getGod(drawedCards.getFirst()).getName()+"\n1 - "+SimpleGods.getGod(drawedCards.getSecond()).getName());
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
                    if(move.getPlayer().getGodCard().getCardGod()==SimpleGods.APOLLO && !model.getBoard().getCell(move.getRow(), move.getColumn()).isFree()){
                        List<Object> objectList= new ArrayList<>();
                        //primo worker di quello che vuole muovere
                        objectList.add(move.getPlayer().getWorker(move.getWorkerId()));
                        for(int i=0; i<model.getNumOfPlayers(); i++){
                            if(model.getPlayer(i).getGodCard().getCardGod()!=SimpleGods.APOLLO){
                                if(model.getPlayer(i).getWorker(0).getCell()==model.getBoard().getCell(move.getRow(), move.getColumn())){
                                    objectList.add(model.getPlayer(i).getWorker(0));
                                }
                                else if(model.getPlayer(i).getWorker(1).getCell()==model.getBoard().getCell(move.getRow(), move.getColumn())){
                                    objectList.add(model.getPlayer(i).getWorker(1));
                                }
                            }
                        }
                        move.getPlayer().getGodCard().usePower(objectList);
                        model.getActualPlayer().setUsedWorker(move.getWorkerId());
                        model.notifyChanges();
                    }
                    else{
                        model.move(move);
                    }
                    if(model.getBoard().getCell(move.getRow(), move.getColumn()).getLevel().getBlockId()==3){
                        model.victory(move.getPlayer());
                        if(model.getNumOfPlayers()==2){
                            model.endGame();
                        }
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
            model.setNextMessageType(MessageType.MOVE);
            model.setNextPlayerMessage(PlayerMessage.MOVE);
            model.updatePhase();
            model.updateTurn();
            switch(level.getBlockId()) {
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
        else{
            throw new IllegalArgumentException();
        }
        checkVictory();

    }
    @Override
    protected synchronized HashMap<Cell, Boolean> checkCellsAround (Worker worker){
        HashMap<Cell, Boolean> availableCells = new HashMap<>();
        Cell cell = worker.getCell();
        Board board = model.getBoard();
        Player player=model.getActualPlayer();
        if(model.getGCPlayer(SimpleGods.APOLLO)== player){
            for (int x = cell.getX() - 1; x <= cell.getX() + 1; x++) {
                for (int y = cell.getY() - 1; y <= cell.getY() + 1; y++) {
                    try{
                        availableCells.put(board.getCell(x,y), board.checkCellApollo(x,y,worker));
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
                        availableCells.put(board.getCell(x,y), board.checkCell(x,y,worker));
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
        int playerCantMove=0;
        Player[] players =model.getPlayers();
        boolean[] playersBool = new boolean[model.getNumOfPlayers()];
        Arrays.fill(playersBool, false); //do per scontato che tutti i worker si possano muovere
        for(int i=0; i<model.getNumOfPlayers(); i++){
            players[i].getWorker(0).setStatus(canMove(players[i].getWorker(0)));
            players[i].getWorker(1).setStatus(canMove(players[i].getWorker(1)));
            if(!players[i].getWorker(0).getStatus() && !players[i].getWorker(1).getStatus()){// controllo se nessun worker si può muovere
                playerCantMove++;
                playersBool[i]=true;
            }
        }
        if(playerCantMove==model.getNumOfPlayers()-1){
            for(int i=0; i<players.length;i++){
                if(!playersBool[i]){
                    model.victory(players[i]);
                    if(model.getLeftPlayers()==2){
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
