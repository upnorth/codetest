package com.pierceecom.blog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@RestController
public class BlogWebController {

    @Autowired
    private BlogWebService blogWebService;

    @RequestMapping("/posts")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllPosts() {
        return blogWebService.getAllPosts();
    }
}
