package jac.webservice.gallery.exception;

public class DatabaseConnectionFailure extends RuntimeException {
    public DatabaseConnectionFailure(String message) {
        super(message);
    }
}
