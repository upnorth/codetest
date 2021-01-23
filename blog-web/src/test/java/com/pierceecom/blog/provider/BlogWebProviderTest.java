package com.pierceecom.blog.provider;

import com.pierceecom.blog.model.Post;
import com.pierceecom.blog.model.PostsResponse;
import com.pierceecom.blog.service.BlogWebServiceInterface;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BlogWebProviderTest {

    @Mock
    private BlogWebServiceInterface blogWebService;

    @InjectMocks
    private BlogWebProvider resource;

    @Test
    public void getAllPostsTest() {
        List<Post> posts = new ArrayList<>();
        posts.add(new Post("1", "Välkommen!", "Välkommen till vår nya blogg."));
        posts.add(new Post("2", "Post nr 2!", "Hej Hopp."));
        posts.add(new Post("3", "Post nr 3!", "Jamtland, Jamtland, Jamt å ständut!"));
        when(blogWebService.getAllPosts()).thenReturn(posts);

        PostsResponse postsResponse = resource.getAllPosts();

        assertEquals(postsResponse.getPosts(), posts);
    }
    
}
