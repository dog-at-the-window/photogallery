package jac.webservice.gallery.exception;

import org.springframework.dao.DataIntegrityViolationException;

public class GalleryDataIntegrityViolationException extends DataIntegrityViolationException {
    public GalleryDataIntegrityViolationException(String message) {
        super(message);
    }
}
