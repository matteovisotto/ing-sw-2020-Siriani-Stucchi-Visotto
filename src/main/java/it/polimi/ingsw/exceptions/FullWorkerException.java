package it.polimi.ingsw.exceptions;

public class FullWorkerException extends WorkerException {

    public FullWorkerException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }

    public FullWorkerException(String errorMessage) {
        super(errorMessage);
    }
}
