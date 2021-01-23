package com.pierceecom.blog.service;

import com.pierceecom.blog.model.Post;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BlogWebService implements BlogWebServiceInterface {
    List<Post> posts = new ArrayList<>(); //TODO: Persist with local or cloud database

    @Override
    public List<Post> getAllPosts() {
        return posts;
    }

    @Override
    public void addPost(Post post) {
        //TODO: validate post
        posts.add(post);
    }
}
