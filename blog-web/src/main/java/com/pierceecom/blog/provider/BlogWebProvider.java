package com.pierceecom.blog.provider;

import com.pierceecom.blog.model.Post;
import com.pierceecom.blog.service.BlogWebServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@RestController
public class BlogWebProvider {

    @Autowired
    private BlogWebServiceInterface service;

    @PostMapping("/posts")
    public ResponseEntity<Post> addPost(@RequestBody Post post) {
        return new ResponseEntity<>(service.addPost(post), HttpStatus.CREATED);
    }

    @GetMapping("/posts/{id}")
    public Post getPost(@PathVariable String id) {
        return service.getPost(id);
    }

    @GetMapping("/posts")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Post> getAllPosts() {
        return service.getAllPosts();
    }

    @PutMapping("/posts")
    public ResponseEntity<Void> updatePost(@RequestBody Post updatedPost) {
        service.updatePost(updatedPost);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/posts/{id}")
    public void deletePost(@PathVariable String id) {
        service.deletePost(id);
    }
}
