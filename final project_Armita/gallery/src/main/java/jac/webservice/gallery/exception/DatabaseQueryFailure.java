package jac.webservice.gallery.exception;

public class DatabaseQueryFailure extends RuntimeException {
    public DatabaseQueryFailure(String message) {
        super(message);
    }
}
