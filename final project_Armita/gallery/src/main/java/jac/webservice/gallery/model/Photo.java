package jac.webservice.gallery.model;


import com.fasterxml.jackson.annotation.JsonCreator;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Photo {
    private int id;
    private String name;
    String title;
    private String description;
    private String updatedAt;


    //to update the DB
    @JsonCreator
    public Photo(String name, String title, String description) {
        this.name = name;
        this.title = title;
        this.description = description;
        setUpdatedAt();
    }

    //to read from DB
    public Photo(int id, String name, String title, String description, String updatedAt) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.description = description;
        this.updatedAt = updatedAt;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUpdatedAt() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        this.updatedAt = LocalDateTime.now().format(dtf);
    }
}
