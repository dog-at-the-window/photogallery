package jac.webservice.gallery.repository;

import jac.webservice.gallery.exception.*;
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
            String err = dae.getMessage();
            System.out.println(err);
            if (err.toLowerCase().contains("failed to obtain jdbc connection")) {
                throw new DatabaseConnectionFailure("connection to database failed");
            } else if (err.toLowerCase().contains("bad sql grammar")) {
                throw new DatabaseQueryFailure("bad SQL grammar, check the SQL query");
            } else {
                throw new GalleryDataAccessException(err);
            }
        } catch (RuntimeException e) {
            String err = e.getMessage();
            System.out.println(err);
            throw new GalleryRuntimeException(err);
        }
    }

    /*fetch a photo of the gallery by id*/
    public Photo getPhotoById(int id) {

        try {
            String query = "select * from ".concat(tbl).concat(" where id=?");
            return jdbcTemplate.queryForObject(query, new GalleryRowMapper(), id);
        } catch (EmptyResultDataAccessException ex) {
            String err = ex.getMessage();
            System.out.println(err);
            if (err.toLowerCase().contains("incorrect result size")) {
                throw new ResourceNotFoundException("photo with id " + id + " does not exist!");
            } else {
                throw new GalleryEmptyResultDataAccessException(err);
            }
        } catch (DataAccessException dae) {
            String err = dae.getMessage();
            System.out.println(err);
            if (err.toLowerCase().contains("failed to obtain jdbc connection")) {
                throw new DatabaseConnectionFailure("connection to database failed");
            } else if (err.toLowerCase().contains("bad sql grammar")) {
                throw new DatabaseQueryFailure("bad SQL grammar, check the SQL query");
            } else {
                throw new GalleryDataAccessException(err);
            }
        } catch (RuntimeException e) {
            String err = e.getMessage();
            System.out.println(err);
            throw new GalleryRuntimeException(err);
        }
    }

    /* update a photo of the gallery by id*/
    public void updatePhotoById(int id, Map<String, String> requestBodyMap) {
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
            String err = dae.getMessage();
            System.out.println(err);
            if (err.toLowerCase().contains("failed to obtain jdbc connection")) {
                throw new DatabaseConnectionFailure("connection to database failed");
            } else if (err.toLowerCase().contains("bad sql grammar")) {
                throw new DatabaseQueryFailure("bad SQL grammar, check the SQL query");
            } else if (err.toLowerCase().contains("duplicate entry")) {
                throw new DatabaseDuplicateEntry("photo with this name already exists in the DB");
            } else {
                throw new GalleryDataAccessException(err);
            }
        } catch (RuntimeException e) {
            String err = e.getMessage();
            System.out.println(err);
            throw new GalleryRuntimeException(err);
        }
        if (rowsAffected == 0) {
            throw new ResourceNotFoundException("Photo not found with id: " + id);
        }
    }

    /* update a photo from the gallery by id*/
    public int removePhotoById(int id) {
        try {
            String query = "delete from ".concat(tbl).concat(" where id=?");
            return jdbcTemplate.update(query, id);
        } catch (DataAccessException dae) {
            String err = dae.getMessage();
            System.out.println(err);
            if (err.toLowerCase().contains("failed to obtain jdbc connection")) {
                throw new DatabaseConnectionFailure("connection to database failed");
            } else if (err.toLowerCase().contains("bad sql grammar")) {
                throw new DatabaseQueryFailure("bad SQL grammar, check the SQL query");
            } else {
                throw new GalleryDataAccessException(err);
            }
        } catch (RuntimeException e) {
            String err = e.getMessage();
            System.out.println(err);
            throw new GalleryRuntimeException(err);
        }
    }

    /* add a photo to the gallery*/
    public void addPhoto(Photo photo) {

        try {
            String query = "insert into ".concat(tbl).concat("(name,title,description,updatedAt) values(?,?,?,?)");
            jdbcTemplate.update(query, photo.getName(), photo.getTitle(), photo.getDescription(), photo.getUpdatedAt());
        } catch (DataIntegrityViolationException ex) {
            String err = ex.getMessage();
            System.out.println(err);
            if (err.toLowerCase().contains("duplicate entry")) {
                throw new DatabaseDuplicateEntry("photo with this name already exists in the DB");
            } else {
                throw new GalleryDataIntegrityViolationException(err);
            }
        } catch (DataAccessException dae) {
            String err = dae.getMessage();
            System.out.println(err);
            if (err.toLowerCase().contains("failed to obtain jdbc connection")) {
                throw new DatabaseConnectionFailure("connection to database failed");
            } else if (err.toLowerCase().contains("bad sql grammar")) {
                throw new DatabaseQueryFailure("bad SQL grammar, check the SQL query");
            } else if (err.toLowerCase().contains("duplicate entry")) {
                throw new DatabaseDuplicateEntry("photo with this name already exists in the DB");
            } else {
                throw new GalleryDataAccessException(err);
            }
        } catch (RuntimeException e) {
            String err = e.getMessage();
            System.out.println(err);
            throw new GalleryRuntimeException(err);
        }
    }
}
