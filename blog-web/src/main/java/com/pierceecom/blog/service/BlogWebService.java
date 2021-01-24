package com.pierceecom.blog.service;

import com.pierceecom.blog.model.Post;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class BlogWebService implements BlogWebServiceInterface {
    List<Post> posts = new ArrayList<>(); //TODO: Persist with local or cloud database

    @Override
    public List<Post> getAllPosts() {
        return posts;
    }

    @Override
    public void addPost(Post post) {
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
        if(post.getTitle() == null || post.getTitle().trim().equals("")
                || post.getContent() == null || post.getContent().trim().equals("")){
            // TODO: Possibly change to 400 Bad Request in API spec?
            // TODO: Log error
            throw new ResponseStatusException(HttpStatus.METHOD_NOT_ALLOWED);
        }
    }

    private Post findPost(String id) {
        return posts.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
    }
}
