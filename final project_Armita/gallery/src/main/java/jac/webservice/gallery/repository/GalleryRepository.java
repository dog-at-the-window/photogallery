package jac.webservice.gallery.repository;

import jac.webservice.gallery.exception.GalleryDataAccessException;
import jac.webservice.gallery.exception.GalleryDataIntegrityViolationException;
import jac.webservice.gallery.exception.GalleryResourceNotFoundException;
import jac.webservice.gallery.model.Photo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class GalleryRepository {
    @Value("${tbl_name}")
    String tbl;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /*fetch all photos of the gallery*/
    public List<Photo> getAllPhotos() {
        try {
            String query = "select * from ".concat(tbl);
            return jdbcTemplate.query(query, new GalleryRowMapper());
        } catch (DataAccessException dae) {
            throw new GalleryDataAccessException("Failed to retrieve photos from the database.");
        }
    }

    /*fetch a photo of the gallery by id*/
    public Photo getPhotoById(int id) {

        try {
            String query = "select * from ".concat(tbl).concat(" where id=?");
            return jdbcTemplate.queryForObject(query, new GalleryRowMapper(), id);
        } catch (EmptyResultDataAccessException ex) {
            throw new GalleryResourceNotFoundException("photo with id " + id + " does not exist!", 1);
        } catch (DataAccessException dae) {
            throw new GalleryDataAccessException("Failed to retrieve photos from the database.");
        }
    }

    /* update a photo of the gallery by id*/
    public int updatePhotoById(int id, Map<String, String> requestBodyMap) {
        int rowsAffected = 0;
        try {
            ArrayList<Object> values = new ArrayList<>();
            StringBuilder query = new StringBuilder("update ".concat(tbl).concat(" set "));
            if (!requestBodyMap.isEmpty()) {
                for (Map.Entry<String, String> entry : requestBodyMap.entrySet()) {
                    if (entry.getKey().equals("updatedAt") || entry.getKey().equals("id")) continue;
                    query.append(entry.getKey()).append("=? , ");
                    values.add(entry.getValue());
                }
                //update the updatedAdd filed
                query.append("updatedAt =?");
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                values.add(LocalDateTime.now().format(dtf));
                query.append(" where id=?");
                values.add(id);
                rowsAffected = jdbcTemplate.update(query.toString(), values.toArray());
            }
        } catch (DataAccessException dae) {
            throw new GalleryDataAccessException(dae.getMessage());
        }
        return rowsAffected;
    }

    /* update a photo from the gallery by id*/
    public int removePhotoById(int id) {
        try {
            String query = "delete from ".concat(tbl).concat(" where id=?");
            return jdbcTemplate.update(query, id);
        } catch (DataAccessException dae) {
            throw new GalleryDataAccessException(dae.getMessage());
        }

    }

    /* add a photo to the gallery*/
    public void addPhoto(Photo photo) {

        try {
            String query = "insert into ".concat(tbl).concat("(name,title,description,updatedAt) values(?,?,?,?)");
            jdbcTemplate.
                    update(query, photo.getName(), photo.getTitle(), photo.getDescription(), photo.getUpdatedAt());
        } catch (DataIntegrityViolationException ex) {
            throw new GalleryDataIntegrityViolationException(ex.getMessage());
        }catch (DataAccessException dae) {
            throw new GalleryDataAccessException(dae.getMessage());
        }
    }
}
