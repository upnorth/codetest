package com.pierceecom.blog.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class PostsResponse {

    @JsonProperty("posts")
    List<Post> postList;

    public PostsResponse(List<Post> posts){
        this.postList = posts;
    }

    @JsonProperty("posts")
    public List<Post> getPosts(){
        return this.postList;
    }
}
