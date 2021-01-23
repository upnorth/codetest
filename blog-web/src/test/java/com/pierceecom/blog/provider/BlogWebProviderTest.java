package com.pierceecom.blog.provider;

import com.pierceecom.blog.model.Post;
import com.pierceecom.blog.model.PostsResponse;
import com.pierceecom.blog.service.BlogWebServiceInterface;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BlogWebProviderTest {

    @Mock
    private BlogWebServiceInterface service;

    @InjectMocks
    private BlogWebProvider resource;

    @Test
    public void addPostTest() {
        Post post = new Post("1", "Välkommen!", "Välkommen till vår nya blogg.");
        resource.addPost(post);

        verify(service, times(1)).addPost(Mockito.eq(post));
    }

    @Test
    public void getPostTest(){
        String postId = "1";
        String postTitle = "Post Title";
        String postContent = "Content";
        when(service.getPost(postId)).thenReturn(new Post(postId, postTitle, postContent));

        Post post = resource.getPost(postId);

        verify(service, times(1)).getPost(postId);
        assertEquals(postId, post.getId());
        assertEquals(postTitle, post.getTitle());
        assertEquals(postContent, post.getContent());
    }

    @Test
    public void getAllPostsTest() {
        List<Post> posts = new ArrayList<>();
        posts.add(new Post("1", "Välkommen!", "Välkommen till vår nya blogg."));
        posts.add(new Post("2", "Post nr 2!", "Hej Hopp."));
        posts.add(new Post("3", "Post nr 3!", "Jamtland, Jamtland, Jamt å ständut!"));
        when(service.getAllPosts()).thenReturn(posts);

        PostsResponse postsResponse = resource.getAllPosts();

        verify(service, times(1)).getAllPosts();
        assertEquals(posts, postsResponse.getPosts());
    }

    @Test
    public void updatePostTest(){
        Post updatedPost = new Post("1", "Välkommen (igen)!", "Verkligen välkommen till vår nya blogg.");
        resource.updatePost(updatedPost);

        verify(service, times(1)).updatePost(Mockito.eq(updatedPost));
    }

    @Test
    public void deletePostTest() {
        String id = "1";
        resource.deletePost(id);

        verify(service, times(1)).deletePost(Mockito.eq(id));
    }
}
