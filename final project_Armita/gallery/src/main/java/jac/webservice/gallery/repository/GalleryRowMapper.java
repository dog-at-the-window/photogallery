package jac.webservice.gallery.repository;

import jac.webservice.gallery.model.Photo;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GalleryRowMapper implements RowMapper<Photo> {
    @Override
    public Photo mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Photo(rs.getInt("id"),rs.getString("name"), rs.getString("title"),
                rs.getString("description"),rs.getString("updatedAt"));
    }
}
