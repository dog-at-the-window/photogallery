package jac.webservice.gallery.exception;

import org.springframework.dao.EmptyResultDataAccessException;

public class GalleryEmptyResultDataAccessException extends EmptyResultDataAccessException {
    public GalleryEmptyResultDataAccessException(String err) {
        super(err,1);
    }
}
