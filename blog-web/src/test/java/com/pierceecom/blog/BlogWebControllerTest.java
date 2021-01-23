package com.pierceecom.blog;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.ws.rs.core.Response;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BlogWebControllerTest {

    @Mock
    private BlogWebService blogWebService;

    @InjectMocks
    private BlogWebController resource;

    @Test
    public void testGetAllPosts() {
        when(blogWebService.getAllPosts()).thenReturn(Response.ok("{\"message\":\"Hello Pierce\"}").build());
        Response resourceAllPosts = resource.getAllPosts();
        String hello = (String)resourceAllPosts.getEntity();
        assertEquals("{\"message\":\"Hello Pierce\"}", hello);
    }
    
}
