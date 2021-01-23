package com.pierceecom.blog.provider;

import com.pierceecom.blog.model.Post;
import com.pierceecom.blog.model.PostsResponse;
import com.pierceecom.blog.service.BlogWebServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@RestController
public class BlogWebProvider {

    @Autowired
    private BlogWebServiceInterface service;

    @PostMapping("/posts")
    public void addPost(@RequestBody Post post) {
        service.addPost(post);
    }

    @GetMapping("/posts/{id}")
    public Post getPost(@PathVariable String id) {
        return service.getPost(id);
    }

    @GetMapping("/posts")
    @Produces(MediaType.APPLICATION_JSON)
    public PostsResponse getAllPosts() {
        return new PostsResponse(service.getAllPosts());
    }

    @PutMapping("/posts")
    public void updatePost(@RequestBody Post updatedPost) {
        service.updatePost(updatedPost);
    }

    @DeleteMapping("/posts/{id}")
    public void deletePost(@PathVariable String id) {
        service.deletePost(id);
    }
}
