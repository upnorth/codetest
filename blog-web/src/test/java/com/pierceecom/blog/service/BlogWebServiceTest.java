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

        assertEquals(2, allPosts.size());
        assertEquals(postOneId, allPosts.get(0).getId());
        assertEquals(postOneTitle, allPosts.get(0).getTitle());
        assertEquals(postOneContent, allPosts.get(0).getContent());
        assertEquals(postTwoId, allPosts.get(1).getId());
        assertEquals(postTwoTitle, allPosts.get(1).getTitle());
        assertEquals(postTwoContent, allPosts.get(1).getContent());
    }

    @Test
    void addPostTest() {
        String postOneId = "1";
        String postOneTitle = "Post Title";
        String postOneContent = "Content";

        service.addPost(new Post(postOneId, postOneTitle, postOneContent));

        List<Post> allPosts = service.getAllPosts();
        assertEquals(1, allPosts.size());
        assertEquals(postOneId, allPosts.get(0).getId());
        assertEquals(postOneTitle, allPosts.get(0).getTitle());
        assertEquals(postOneContent, allPosts.get(0).getContent());
    }

    @Test
    void updatePost(){
        String postOneId = "1";
        String postOneTitle = "Post Title";
        String postOneContent = "Content";
        service.addPost(new Post(postOneId, postOneTitle, postOneContent));

        List<Post> allPosts = service.getAllPosts();
        assertEquals(1, allPosts.size());
        assertEquals(postOneId, allPosts.get(0).getId());
        assertEquals(postOneTitle, allPosts.get(0).getTitle());
        assertEquals(postOneContent, allPosts.get(0).getContent());

        String updatePostId = "1";
        String updatePostTitle = "Updated Post Title";
        String updatePostContent = "Updated Post Content";

        service.updatePost(new Post(updatePostId, updatePostTitle, updatePostContent));

        List<Post> allUpdatedPosts = service.getAllPosts();
        assertEquals(1, allPosts.size());
        assertEquals(updatePostId, allUpdatedPosts.get(0).getId());
        assertEquals(updatePostTitle, allUpdatedPosts.get(0).getTitle());
        assertEquals(updatePostContent, allUpdatedPosts.get(0).getContent());
    }

    @Test
    void deletePost(){
        String postOneId = "1";
        String postOneTitle = "Post Title";
        String postOneContent = "Content";
        service.addPost(new Post(postOneId, postOneTitle, postOneContent));

        List<Post> allPosts = service.getAllPosts();
        assertEquals(allPosts.size(), 1);

        service.deletePost(postOneId);

        List<Post> noPosts = service.getAllPosts();
        assertEquals(noPosts.size(), 0);
    }
}