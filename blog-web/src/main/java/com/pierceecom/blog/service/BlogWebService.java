package com.pierceecom.blog.service;

import com.pierceecom.blog.model.Post;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

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

    @Override
    public void updatePost(Post updatedPost) {
        Post existingPost = posts.stream()
                .filter(p -> p.getId().equals(updatedPost.getId()))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

        posts.set(posts.indexOf(existingPost),
                new Post(updatedPost.getId(), updatedPost.getTitle(), updatedPost.getContent()));
    }
}
