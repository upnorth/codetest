package com.pierceecom.blog.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pierceecom.blog.model.Post;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Objects;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BlogIntegrationTest {

    private static final String VALID_POST_1 = "{\"id\":\"1\",\"title\":\"First title\",\"content\":\"First content\"}";
    private static final String VALID_UPDATE_POST_1 = "{\"id\":\"1\",\"title\":\"First updated title\",\"content\":\"First updated content\"}";
    private static final String VALID_POST_2 = "{\"id\":\"2\",\"title\":\"Second title\",\"content\":\"Second content\"}";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void shouldAddPostWhenValidPostIsPosted() throws JsonProcessingException {
        clearPosts();
        Post post = objectMapper.readValue(VALID_POST_1, Post.class);

        ResponseEntity<Post> addPostResponse = addPostRequest(post);

        assertEquals(201, addPostResponse.getStatusCode().value());
        ResponseEntity<String> getPostsResponse = getPostsRequest();
        List<Post> addedPosts = toList(getPostsResponse.getBody());
        assertEquals(1, addedPosts.size());
        assertEquals(post.getTitle(), addedPosts.get(0).getTitle());
        assertEquals(post.getContent(), addedPosts.get(0).getContent());
    }

    @Test
    public void shouldNotAddPostWhenPostWithoutTitleIsPosted() throws JsonProcessingException {
        clearPosts();
        Post post = objectMapper.readValue(VALID_POST_1, Post.class);
        post.setTitle("");

        ResponseEntity<Void> addPostNoTitleResponse = addInvalidPostRequest(post);

        assertEquals(405, addPostNoTitleResponse.getStatusCode().value());
        // TODO: Add tests of custom reason phrase from API spec. Seems to require GlobalExceptionHandler, will skip for now
        ResponseEntity<String> getPostsResponse = getPostsRequest();
        assertEquals("[]", getPostsResponse.getBody());
    }

    @Test
    public void shouldNotAddPostWhenPostWithoutContentIsPosted() throws JsonProcessingException {
        clearPosts();
        Post post = objectMapper.readValue(VALID_POST_1, Post.class);
        post.setContent("");

        ResponseEntity<Void> addPostNoContentResponse = addInvalidPostRequest(post);

        assertEquals(405, addPostNoContentResponse.getStatusCode().value());
        ResponseEntity<String> getPostsResponse = getPostsRequest();
        assertEquals("[]", getPostsResponse.getBody());
    }

    @Test
    public void shouldReturnCorrectPostWhenExistingIsRequestedById() throws JsonProcessingException {
        clearPosts();
        Post post = objectMapper.readValue(VALID_POST_1, Post.class);
        Post postWithId = addPostRequest(post).getBody();

        ResponseEntity<String> responseEntity = getPostById(Objects.requireNonNull(postWithId).getId());

        assertEquals(200, responseEntity.getStatusCode().value());
        Post responsePost = objectMapper.readValue(Objects.requireNonNull(responseEntity.getBody()), Post.class);
        assertEquals(postWithId.getId(), responsePost.getId());
        assertEquals(postWithId.getTitle(), responsePost.getTitle());
        assertEquals(postWithId.getContent(), responsePost.getContent());
    }

    @Test
    public void shouldReturnNotFoundIfNotExistingPostIsRequested() throws JsonProcessingException {
        clearPosts();
        Post post = objectMapper.readValue(VALID_POST_1, Post.class);
        addPostRequest(post);
        String notPostId = post.getId() + "00";

        ResponseEntity<String> getPostResponse = getPostById(notPostId);

        assertEquals(404, getPostResponse.getStatusCode().value());
    }

    @Test
    public void shouldReturnEmptyArrayWhenBlogHasNoPosts() throws JsonProcessingException {
        clearPosts();

        ResponseEntity<String> responseEntity = getPostsRequest();

        assertEquals(200, responseEntity.getStatusCode().value());
        assertEquals("[]", responseEntity.getBody());
    }

    @Test
    public void shouldReturnAllPostsWhenBlogHasPosts() throws JsonProcessingException {
        clearPosts();
        Post post1 = objectMapper.readValue(VALID_POST_1, Post.class);
        addPostRequest(post1);
        Post post2 = objectMapper.readValue(VALID_POST_2, Post.class);
        addPostRequest(post2);

        ResponseEntity<String> getPostsResponse = getPostsRequest();

        assertEquals(200, getPostsResponse.getStatusCode().value());
        List<Post> posts = toList(getPostsResponse.getBody());
        assertEquals(2, posts.size());
        assertEquals(post1.getTitle(), posts.get(0).getTitle());
        assertEquals(post1.getContent(), posts.get(0).getContent());
        assertEquals(post2.getTitle(), posts.get(1).getTitle());
        assertEquals(post2.getContent(), posts.get(1).getContent());
    }

    @Test
    public void shouldUpdateExistingPostIfValidUpdateIsSent() throws JsonProcessingException {
        clearPosts();
        Post post = objectMapper.readValue(VALID_POST_1, Post.class);
        Post postWithIndex = addPostRequest(post).getBody();
        Post updated = objectMapper.readValue(VALID_UPDATE_POST_1, Post.class);
        updated.setId(Objects.requireNonNull(postWithIndex).getId());
        HttpEntity<Post> updatePost = new HttpEntity<>(updated);

        ResponseEntity<Void> updatePostResponse = updatePostRequest(updatePost);

        assertEquals(201, updatePostResponse.getStatusCode().value());
        List<Post> posts = toList(getPostsRequest().getBody());
        assertEquals(updated.getId(), posts.get(0).getId());
        assertEquals(updated.getTitle(), posts.get(0).getTitle());
        assertEquals(updated.getContent(), posts.get(0).getContent());
    }

    @Test
    public void shouldNotUpdateExistingPostIfInvalidUpdateIsSent() throws JsonProcessingException {
        clearPosts();
        Post post = objectMapper.readValue(VALID_POST_1, Post.class);
        Post postWithId = addPostRequest(post).getBody();
        Post updated = objectMapper.readValue(VALID_POST_1, Post.class);
        updated.setId(Objects.requireNonNull(postWithId).getId());
        updated.setTitle("");
        HttpEntity<Post> updatePost = new HttpEntity<>(updated);

        ResponseEntity<Void> updatePostResponse = updatePostRequest(updatePost);

        assertEquals(405, updatePostResponse.getStatusCode().value());
    }

    @Test
    public void shouldReturnNotFoundIfUpdatedPostDoesNotExist() throws JsonProcessingException {
        clearPosts();
        Post post = objectMapper.readValue(VALID_POST_1, Post.class);
        addPostRequest(post);
        Post updated = objectMapper.readValue(VALID_POST_2, Post.class);
        updated.setId("300");
        HttpEntity<Post> updatePost = new HttpEntity<>(updated);

        ResponseEntity<Void> updatePostResponse = updatePostRequest(updatePost);

        assertEquals(404, updatePostResponse.getStatusCode().value());
    }

    @Test
    public void shouldDeletePostIfItExist() throws JsonProcessingException {
        clearPosts();
        Post post = objectMapper.readValue(VALID_POST_1, Post.class);
        Post registeredPost = addPostRequest(post).getBody();

        ResponseEntity<Void> deletePostResponse = deletePostRequest(Objects.requireNonNull(registeredPost).getId());

        assertEquals(200, deletePostResponse.getStatusCode().value());
        ResponseEntity<String> getPostsResponse = getPostsRequest();
        assertEquals("[]", getPostsResponse.getBody());
    }

    @Test
    public void shouldReturnNotFoundIfDeletedPostDoesNotExist() throws JsonProcessingException {
        clearPosts();
        Post post = objectMapper.readValue(VALID_POST_1, Post.class);
        addPostRequest(post);
        String notAvailablePostId = post.getId() + "1";

        ResponseEntity<Void> deletePostResponse = deletePostRequest(notAvailablePostId);

        assertEquals(404, deletePostResponse.getStatusCode().value());
    }

    private ResponseEntity<Post> addPostRequest(Post post) {
        return restTemplate.postForEntity(getBlogURI(), post, Post.class);
    }

    private ResponseEntity<Void> addInvalidPostRequest(Post post) {
        return restTemplate.postForEntity(getBlogURI(), post, Void.class);
    }

    private ResponseEntity<Void> updatePostRequest(HttpEntity<Post> updatePost) {
        return restTemplate.exchange(getBlogURI(), HttpMethod.PUT, updatePost, Void.class);
    }

    private ResponseEntity<String> getPostsRequest() {
        return restTemplate.getForEntity(getBlogURI(), String.class);
    }

    private List<Post> toList(String postsAsJson) throws JsonProcessingException {
        return objectMapper.readValue((postsAsJson), new TypeReference<List<Post>>() {
        });
    }

    private ResponseEntity<String> getPostById(String id) {
        return restTemplate.getForEntity(getBlogURI() + id, String.class);
    }

    private String getBlogURI() {
        // TODO: Refactor as class member, had problems with port initialization but remember using cleaner solutions
        return "http://localhost:" + port + "/blog-web/posts/";
    }

    private ResponseEntity<Void> deletePostRequest(String id) {
        return restTemplate.exchange(getBlogURI() + id, HttpMethod.DELETE, null, Void.class);
    }

    void clearPosts() throws JsonProcessingException {
        // Resets blog server state to make each test independent from the rest
        ResponseEntity<String> getPostsResponse = getPostsRequest();
        List<Post> posts = toList(getPostsResponse.getBody());
        for (Post post : posts) {
            restTemplate.delete(getBlogURI() + post.getId());
        }
    }

}
