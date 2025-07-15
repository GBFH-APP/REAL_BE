package GBFH.GBFH_BE.exception;

public class IdOrPasswordUnmatchException extends RuntimeException {
    public IdOrPasswordUnmatchException(String message) {
        super(message);
    }
}
