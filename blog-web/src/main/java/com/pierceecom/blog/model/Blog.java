package com.pierceecom.blog.model;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

public class Blog {
    // TODO: Persist with local or cloud database
    private List<Post> posts;
    private int nextPostId;

    public Blog() {
        this.posts = new ArrayList<>();
        this.nextPostId = 1;
    }

    public void updatePost(Post updatedPost) {
        updatedPost.validate();
        Post existingPost = findPost(updatedPost.getId());
        posts.set(posts.indexOf(existingPost), updatedPost);
    }

    public void deletePost(String postId) {
        Post existingPost = findPost(postId);
        posts.remove(existingPost);
    }

    public Post findPost(String id) {
        // TODO: Possibly add validation of id string
        return this.posts.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
    }

    public Post addPost(Post post) {
        post.validate();
        // TODO: Should have generated GUIDs or auto incremented ids from database.
        post.setId(String.valueOf(nextPostId));
        posts.add(post);
        nextPostId++;

        return post;
    }

    public List<Post> getPosts() {
        return posts;
    }
}
