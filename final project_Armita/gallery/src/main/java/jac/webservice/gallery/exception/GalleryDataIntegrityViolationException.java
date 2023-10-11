package jac.webservice.gallery.exception;

import org.springframework.dao.DataIntegrityViolationException;

public class GalleryDataIntegrityViolationException extends RuntimeException {
    public GalleryDataIntegrityViolationException(String message) {
        super(message);
    }
}
