package com.pierceecom.blog.service;

import com.pierceecom.blog.model.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
class BlogWebServiceTest {

    private BlogWebServiceInterface service;

    public static final String POST_ONE_ID = "1";
    public static final String POST_ONE_TITLE = "Post Title";
    public static final String POST_ONE_CONTENT = "Content";
    public static final String POST_TWO_ID = "2";
    public static final String POST_TWO_TITLE = "Post Title 2";
    public static final String POST_TWO_CONTENT = "Content 2";

    @BeforeEach
    void instantiateService() {
        service = new BlogWebService();
    }

    @Test
    void addValidPostTest() {
        service.addPost(new Post(POST_ONE_ID, POST_ONE_TITLE, POST_ONE_CONTENT));

        List<Post> allPosts = service.getAllPosts();

        assertEquals(1, allPosts.size());
        assertEquals(POST_ONE_ID, allPosts.get(0).getId());
        assertEquals(POST_ONE_TITLE, allPosts.get(0).getTitle());
        assertEquals(POST_ONE_CONTENT, allPosts.get(0).getContent());
    }

    @Test
    void correctIndexForPostsTest(){
        Post post1 = service.addPost(new Post(null, POST_ONE_TITLE, POST_ONE_CONTENT));
        Post post2 = service.addPost(new Post(null, POST_ONE_TITLE, POST_ONE_CONTENT));
        service.deletePost(post2.getId());
        Post post3 = service.addPost(new Post(null, POST_ONE_TITLE, POST_ONE_CONTENT));

        List<Post> allPosts = service.getAllPosts();

        assertEquals(2, allPosts.size());
        assertEquals("1", post1.getId());
        assertEquals("3", post3.getId());
    }

    @Test
    void addInvalidPostsTest(){
        boolean invalidTitle = false;
        try {
            service.addPost(new Post("1", "", "Content"));
        } catch (ResponseStatusException e) {
            invalidTitle = e.getStatus().equals(HttpStatus.METHOD_NOT_ALLOWED);
        }
        assertTrue(invalidTitle);

        boolean invalidContent = false;
        try {
            service.addPost(new Post("1", "Title", ""));
        } catch (ResponseStatusException e) {
            invalidContent = e.getStatus().equals(HttpStatus.METHOD_NOT_ALLOWED);
        }
        assertTrue(invalidContent);
    }

    @Test
    void getPostByIdTest(){
        service.addPost(new Post(POST_ONE_ID, POST_ONE_TITLE, POST_ONE_CONTENT));

        Post post = service.getPost(POST_ONE_ID);

        assertEquals(POST_ONE_ID, post.getId());
        assertEquals(POST_ONE_TITLE, post.getTitle());
        assertEquals(POST_ONE_CONTENT, post.getContent());
    }

    @Test
    void getPostByNonExistentIdTest(){
        service.addPost(new Post(POST_ONE_ID, POST_ONE_TITLE, POST_ONE_CONTENT));

        boolean postNotExist = false;
        try {
            service.getPost(POST_TWO_ID);
        } catch (ResponseStatusException e) {
            postNotExist = e.getStatus().equals(HttpStatus.NOT_FOUND);
        }
        assertTrue(postNotExist);
    }

    @Test
    void getAllPostsTest() {
        service.addPost(new Post(POST_ONE_ID, POST_ONE_TITLE, POST_ONE_CONTENT));
        service.addPost(new Post(POST_TWO_ID, POST_TWO_TITLE, POST_TWO_CONTENT));

        List<Post> allPosts = service.getAllPosts();

        assertEquals(2, allPosts.size());
        assertEquals(POST_ONE_ID, allPosts.get(0).getId());
        assertEquals(POST_ONE_TITLE, allPosts.get(0).getTitle());
        assertEquals(POST_ONE_CONTENT, allPosts.get(0).getContent());
        assertEquals(POST_TWO_ID, allPosts.get(1).getId());
        assertEquals(POST_TWO_TITLE, allPosts.get(1).getTitle());
        assertEquals(POST_TWO_CONTENT, allPosts.get(1).getContent());
    }

    @Test
    void updatePostByIdTest(){
        service.addPost(new Post(POST_ONE_ID, POST_ONE_TITLE, POST_ONE_CONTENT));

        String updatePostId = "1";
        String updatePostTitle = "Updated Post Title";
        String updatePostContent = "Updated Post Content";

        service.updatePost(new Post(updatePostId, updatePostTitle, updatePostContent));

        Post updatedPost = service.getPost(POST_ONE_ID);
        assertNotNull(updatedPost);
        assertEquals(updatePostId, updatedPost.getId());
        assertEquals(updatePostTitle, updatedPost.getTitle());
        assertEquals(updatePostContent, updatedPost.getContent());
    }

    @Test
    void updatePostByNonExistentIdTest(){
        service.addPost(new Post(POST_ONE_ID, POST_ONE_TITLE, POST_ONE_CONTENT));

        boolean postNotExist = false;
        try {
            service.updatePost(new Post(POST_TWO_ID, POST_ONE_TITLE, POST_ONE_CONTENT));
        } catch (ResponseStatusException e) {
            postNotExist = e.getStatus().equals(HttpStatus.NOT_FOUND);
        }
        assertTrue(postNotExist);
    }

    @Test
    void deletePostByIdTest(){
        service.addPost(new Post(POST_ONE_ID, POST_ONE_TITLE, POST_ONE_CONTENT));

        service.deletePost(POST_ONE_ID);

        List<Post> noPosts = service.getAllPosts();
        assertEquals(noPosts.size(), 0);
    }

    @Test
    void correctIndexingTest() {
        service.addPost(new Post(null, POST_ONE_TITLE, POST_ONE_CONTENT));
        service.addPost(new Post(null, POST_ONE_TITLE, POST_ONE_CONTENT));
        service.deletePost("2");
        service.addPost(new Post(null, POST_ONE_TITLE, POST_ONE_CONTENT));

        List<Post> allPosts = service.getAllPosts();

        assertEquals(2, allPosts.size());
        assertEquals("1", allPosts.get(0).getId());
        assertEquals("3", allPosts.get(1).getId());
    }

    @Test
    void deletePostByNonExistentIdTest(){
        service.addPost(new Post(POST_ONE_ID, POST_ONE_TITLE, POST_ONE_CONTENT));

        boolean postNotExist = false;
        try {
            service.deletePost(POST_TWO_ID);
        } catch (ResponseStatusException e) {
            postNotExist = e.getStatus().equals(HttpStatus.NOT_FOUND);
        }

        assertTrue(postNotExist);
    }
}