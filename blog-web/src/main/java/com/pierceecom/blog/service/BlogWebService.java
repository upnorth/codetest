package com.pierceecom.blog.service;

import com.pierceecom.blog.model.Blog;
import com.pierceecom.blog.model.Post;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class BlogWebService implements BlogWebServiceInterface {
    private Blog blog;

    BlogWebService(){
        this.blog = new Blog();
    }

    @Override
    public List<Post> getAllPosts() {
        return this.blog.posts;
    }

    @Override
    public Post addPost(Post post) {
        post.validate();
        // TODO: Should have generated GUIDs or auto incremented ids from database.
        post.setId(String.valueOf(this.blog.nextPostId));
        this.blog.posts.add(post);
        this.blog.nextPostId++;

        return post;
    }

    @Override
    public void updatePost(Post updatedPost) {
        updatedPost.validate();
        Post existingPost = findPost(updatedPost.getId());
        this.blog.posts.set(this.blog.posts.indexOf(existingPost), updatedPost);
    }

    @Override
    public void deletePost(String postId) {
        Post existingPost = findPost(postId);
        this.blog.posts.remove(existingPost);
    }

    @Override
    public Post getPost(String postId) {
        return findPost(postId);
    }

    private Post findPost(String id) {
        // TODO: Possibly add validation of id string
        return this.blog.posts.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
    }
}
