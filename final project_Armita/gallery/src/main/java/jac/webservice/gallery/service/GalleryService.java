package jac.webservice.gallery.service;

import jac.webservice.gallery.exception.GalleryResourceNotFoundException;
import jac.webservice.gallery.model.Photo;
import jac.webservice.gallery.repository.GalleryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class GalleryService {
    @Autowired
    GalleryRepository galleryRepository;

    /*fetch all photos of the gallery*/
    public List<Photo> getAllPhotos() {
        return galleryRepository.getAllPhotos();
    }

    /*fetch a photo of the gallery by id*/
    public Photo getPhotoById(int id) {
        return galleryRepository.getPhotoById(id);
    }

    /* update a photo of the gallery by id*/
    public void updatePhotoById(int id, Map<String, String> requestBodyMap) {
        int rowsAffected = galleryRepository.updatePhotoById(id, requestBodyMap);
        if (rowsAffected == 0) {
            throw new GalleryResourceNotFoundException("Photo not found with id: " + id, 1);
        }

    }

    /* update a photo from the gallery by id*/
    public void removePhotoById(int id) {
        int rowsAffected = galleryRepository.removePhotoById(id);
        if (rowsAffected == 0) {
            throw new GalleryResourceNotFoundException("Photo not found with id: " + id, 1);
        }
    }

    /* add a photo to the gallery*/
    public void addPhoto(Photo photo) {
        galleryRepository.addPhoto(photo);
    }
}
