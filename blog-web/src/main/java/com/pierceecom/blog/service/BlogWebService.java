package com.pierceecom.blog.service;

import com.pierceecom.blog.model.Post;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class BlogWebService implements BlogWebServiceInterface {
    // TODO: Persist with local or cloud database
    List<Post> posts = new ArrayList<>();

    @Override
    public List<Post> getAllPosts() {
        return posts;
    }

    @Override
    public void addPost(Post post) {
        // TODO: Should have generated GUIDs or auto incremented ids from database.
        // As the API spec doens't require id, current implementation depends on proper input
        validate(post);
        posts.add(post);
    }

    @Override
    public void updatePost(Post updatedPost) {
        validate(updatedPost);
        Post existingPost = findPost(updatedPost.getId());
        posts.set(posts.indexOf(existingPost), updatedPost);
    }

    @Override
    public void deletePost(String postId) {
        Post existingPost = findPost(postId);
        posts.remove(existingPost);
    }

    @Override
    public Post getPost(String postId) {
        return findPost(postId);
    }

    private void validate(Post post) {
        // TODO: Possibly use @NotBlank annotation in Post class on fields instead, cleaner
        if(hasNotValidTitle(post) || hasNotValidContent(post)){
            // TODO: Possibly change to 400 Bad Request in API spec?
            // TODO: Log error
            throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED);
        }
    }

    private boolean hasNotValidContent(Post post) {
        return post.getContent() == null || post.getContent().trim().equals("");
    }

    private boolean hasNotValidTitle(Post post) {
        return post.getTitle() == null || post.getTitle().trim().equals("");
    }

    private Post findPost(String id) {
        // TODO: Possibly add validation of id string
        return posts.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
    }
}
