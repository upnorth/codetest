package com.pierceecom.blog;

import javax.ws.rs.core.Response;

public class BlogWebService {
    public Response getAllPosts() {
        return Response.ok("{\"message\":\"Hello Pierce\"}").build();
    }
}
