package jac.webservice.gallery.exception;

import org.springframework.dao.DataAccessException;

public class GalleryDataAccessException extends DataAccessException {
    public GalleryDataAccessException(String msg) {
        super(msg);
    }
}
