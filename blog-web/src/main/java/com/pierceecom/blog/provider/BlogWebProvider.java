package com.pierceecom.blog.provider;

import com.pierceecom.blog.model.PostsResponse;
import com.pierceecom.blog.service.BlogWebServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@RestController
public class BlogWebProvider {

    @Autowired
    private BlogWebServiceInterface blogWebService;

    @RequestMapping("/posts")
    @Produces(MediaType.APPLICATION_JSON)
    public PostsResponse getAllPosts() {
        return new PostsResponse(blogWebService.getAllPosts());
    }
}
