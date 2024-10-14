package GBFH.GBFH_BE.exception;

public class FileSizeIsNotAllowedException extends RuntimeException{
    public FileSizeIsNotAllowedException(String message) {
        super(message);
    }
}
