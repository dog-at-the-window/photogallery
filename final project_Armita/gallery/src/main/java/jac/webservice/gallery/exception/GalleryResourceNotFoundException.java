package jac.webservice.gallery.exception;

import org.springframework.dao.EmptyResultDataAccessException;

public class GalleryResourceNotFoundException extends EmptyResultDataAccessException {

    public GalleryResourceNotFoundException(String msg, int expectedSize) {
        super(msg, expectedSize);
    }
}