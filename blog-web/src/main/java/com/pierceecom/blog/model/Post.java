package com.pierceecom.blog.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class Post {

    // TODO: Separate models into API, Domain and Data with mapping between controllers, services and database
    // Using API model with external validation in services to simplify this task, cleaner in constructor and setters

    @JsonProperty("id")
    private String id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("content")
    private String content;

    public Post(){}

    public Post(String id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    @JsonProperty("content")
    public String getContent() {
        return content;
    }

    @JsonProperty("content")
    public void setContent(String content) {
        this.content = content;
    }

    // TODO: Possibly use @NotBlank annotation in Post class on fields instead
    public void validate() {
        if(hasNotValidTitle() || hasNotValidContent()){
            // TODO: Possibly change to 400 Bad Request in API spec?
            // TODO: Log error
            throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED);
        }
    }

    private boolean hasNotValidContent() {
        return this.getContent() == null || this.getContent().trim().equals("");
    }

    private boolean hasNotValidTitle() {
        return this.getTitle() == null || this.getTitle().trim().equals("");
    }
}
