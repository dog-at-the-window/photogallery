package jac.webservice.gallery.service;

import jac.webservice.gallery.exception.ResourceNotFoundException;
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
        galleryRepository.updatePhotoById(id, requestBodyMap);

    }

    /* update a photo from the gallery by id*/
    public void removePhotoById(int id) {
        int rowsAffected = galleryRepository.removePhotoById(id);
        if (rowsAffected == 0) {
            throw new ResourceNotFoundException("Photo not found with id: " + id);
        }
    }

    /* add a photo to the gallery*/
    public void addPhoto(Photo photo) {
        galleryRepository.addPhoto(photo);
    }
}
