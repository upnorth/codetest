package com.pierceecom.blog.service;

import com.pierceecom.blog.model.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
class BlogWebServiceTest {

    private BlogWebServiceInterface service;

    @BeforeEach
    void instantiateService() {
        service = new BlogWebService();
    }

    @Test
    void getAllPostsTest() {
        String postOneId = "1";
        String postOneTitle = "Post Title";
        String postOneContent = "Content";
        service.addPost(new Post(postOneId, postOneTitle, postOneContent));
        String postTwoId = "2";
        String postTwoTitle = "Post Title 2";
        String postTwoContent = "Content 2";
        service.addPost(new Post(postTwoId, postTwoTitle, postTwoContent));

        List<Post> allPosts = service.getAllPosts();

        assertEquals(allPosts.size(), 2);
        assertEquals(allPosts.get(0).getId(), postOneId);
        assertEquals(allPosts.get(0).getTitle(), postOneTitle);
        assertEquals(allPosts.get(0).getContent(), postOneContent);
        assertEquals(allPosts.get(1).getId(), postTwoId);
        assertEquals(allPosts.get(1).getTitle(), postTwoTitle);
        assertEquals(allPosts.get(1).getContent(), postTwoContent);
    }

    @Test
    void addPostTest() {
        String postOneId = "1";
        String postOneTitle = "Post Title";
        String postOneContent = "Content";

        service.addPost(new Post(postOneId, postOneTitle, postOneContent));

        List<Post> allPosts = service.getAllPosts();
        assertEquals(allPosts.size(), 1);
        assertEquals(allPosts.get(0).getId(), postOneId);
        assertEquals(allPosts.get(0).getTitle(), postOneTitle);
        assertEquals(allPosts.get(0).getContent(), postOneContent);
    }
}