package it.polimi.ingsw.exceptions;

public class WorkerException extends RuntimeException{
    public WorkerException(String errorMessage, Throwable err){
        super(errorMessage, err);
    }

    public WorkerException(String errorMessage){
        super(errorMessage);
    }


}
