package com.pierceecom.blog.provider;

import com.pierceecom.blog.model.Post;
import com.pierceecom.blog.model.PostsResponse;
import com.pierceecom.blog.service.BlogWebServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@RestController
public class BlogWebProvider {

    @Autowired
    private BlogWebServiceInterface service;

    @GetMapping("/posts")
    @Produces(MediaType.APPLICATION_JSON)
    public PostsResponse getAllPosts() {
        return new PostsResponse(service.getAllPosts());
    }

    @PostMapping("/posts")
    public void addPost(@RequestBody Post post) {
        service.addPost(post);
    }
}
