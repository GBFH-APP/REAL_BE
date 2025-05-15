package GBFH.GBFH_BE.exception;

public class BeforeEndDateException extends RuntimeException{
    public BeforeEndDateException(String message) {
        super(message);
    }
}
