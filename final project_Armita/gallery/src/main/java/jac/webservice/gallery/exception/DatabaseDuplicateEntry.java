package jac.webservice.gallery.exception;

public class DatabaseDuplicateEntry extends RuntimeException {
    public DatabaseDuplicateEntry(String message) {
        super(message);
    }
}
