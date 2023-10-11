package jac.webservice.gallery.controller;

import jac.webservice.gallery.exception.*;
import jac.webservice.gallery.model.Photo;
import jac.webservice.gallery.service.GalleryService;
import org.apache.tika.mime.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.apache.tika.Tika;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/gallery")
@CrossOrigin(origins = "*")
public class GalleryController {
    @Autowired
    GalleryService galleryService;

    @GetMapping("/all")
    public ResponseEntity<List<Photo>> getAllPhotos() {
        try {
            List<Photo> photos = galleryService.getAllPhotos();
            return ResponseEntity.ok(photos);
        } catch (DatabaseConnectionFailure | DatabaseQueryFailure | GalleryDataAccessException |
                 GalleryRuntimeException ex) {
            return new ResponseEntity(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*fetch a photo of the gallery by id*/
    @GetMapping("/find/{id}")
    public ResponseEntity<Photo> getPhotoById(@PathVariable int id) {
        try {
            Photo photo = galleryService.getPhotoById(id);
            return ResponseEntity.ok(photo);
        } catch (DatabaseConnectionFailure | DatabaseQueryFailure | GalleryDataAccessException |
                 GalleryRuntimeException ex) {
            return new ResponseEntity(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (ResourceNotFoundException | GalleryEmptyResultDataAccessException rnfe) {
            return new ResponseEntity(rnfe.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    /* update a photo of the gallery by id*/
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updatePhotoById(@PathVariable int id, @RequestBody Map<String, String> requestBodyMap) {
        try {
            galleryService.updatePhotoById(id, requestBodyMap);
            return ResponseEntity.ok("Photo updated successfully.");
        } catch (DatabaseConnectionFailure | DatabaseQueryFailure | GalleryDataAccessException |
                 GalleryRuntimeException ex) {
            return new ResponseEntity(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (ResourceNotFoundException rnfe) {
            return new ResponseEntity(rnfe.getMessage(), HttpStatus.NOT_FOUND);
        } catch (DatabaseDuplicateEntry e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    /* update a photo from the gallery by id*/
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> removePhotoById(@PathVariable int id) {
        try {
            galleryService.removePhotoById(id);
            return ResponseEntity.ok("Photo deleted successfully.");
        } catch (ResourceNotFoundException ex) {
            return new ResponseEntity(ex.getMessage(), HttpStatus.NOT_FOUND);
        } catch (DatabaseConnectionFailure | DatabaseQueryFailure | GalleryDataAccessException |
                 GalleryRuntimeException ex) {
            return new ResponseEntity(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /* add a photo to the gallery*/
    @PostMapping("/add")
    public ResponseEntity<String> addPhoto(@RequestParam("title") String title,
                                           @RequestParam("description") String description,
                                           @RequestParam("name") MultipartFile imageFile) {

        try {
            String imagesDirectory = System.getProperty("user.dir") + "/src/main/resources/static/images/";
            // Check the file extension to ensure it's an image
            String originalFilename = imageFile.getOriginalFilename();
            if (!originalFilename.toLowerCase().matches(".*\\.(jpg|jpeg|png|gif|bmp)$")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid image file format.");
            }

            // Verify that the content matches an image format
            Tika tika = new Tika();
            String contentType = null;
            try {
                contentType = tika.detect(imageFile.getInputStream());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (!MediaType.parse(contentType).getType().equals("image")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid image content type.");
            }
            File file = new File(imagesDirectory + imageFile.getOriginalFilename());
            //save photo to gallery
            try {
                imageFile.transferTo(file);
            } catch (IOException e) {
                String err = e.getMessage();
                System.out.println(err);
                throw new RuntimeException(e);
            }
            //save photo to DB
            Photo photo = new Photo(imageFile.getOriginalFilename(), title, description);
            galleryService.addPhoto(photo);
            return ResponseEntity.ok("Photo added successfully.");
        } catch (GalleryDataIntegrityViolationException | DatabaseDuplicateEntry ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (DatabaseConnectionFailure | DatabaseQueryFailure | GalleryDataAccessException |
                 GalleryRuntimeException ex) {
            return new ResponseEntity(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }
}
