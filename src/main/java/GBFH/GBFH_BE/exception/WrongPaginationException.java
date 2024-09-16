package GBFH.GBFH_BE.exception;

public class WrongPaginationException extends RuntimeException{
    public WrongPaginationException(String message){
        super(message);
    }
}
