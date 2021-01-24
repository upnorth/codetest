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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BlogIntegrationTest {

    private static final String VALID_POST_1 = "{\"id\":\"1\",\"title\":\"First title\",\"content\":\"First content\"}";
    private static final String VALID_UPDATE_POST_1 = "{\"id\":\"1\",\"title\":\"First updated title\",\"content\":\"First updated content\"}";
    private static final String VALID_POST_2 = "{\"id\":\"2\",\"title\":\"Second title\",\"content\":\"Second content\"}";
    private static final String INVALID_POST_MISSING_TITLE = "{\"id\":\"1\",\"content\":\"First content\"}";
    private static final String INVALID_POST_MISSING_CONTENT = "{\"id\":\"1\",\"title\":\"Title\"}";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String getBlogURI() {
        // TODO: Refactor as class member, had problems with port initialization but remember using cleaner solutions
        return "http://localhost:" + port + "/blog-web/posts/";
    }

    void clearPosts() {
        // Resets blog server state to make each test independent from the rest
        // Doesn't work with @BeforeEach annotation, possibly due to port and restTemplate not working as static
        restTemplate.delete(getBlogURI() + "1");
        restTemplate.delete(getBlogURI() + "2");
    }

    @Test
    public void shouldAddPostWhenValidPostIsPosted() throws JsonProcessingException {
        clearPosts();
        Post post = objectMapper.readValue(VALID_POST_1, Post.class);
        ResponseEntity<Void> addPostResponse = restTemplate.postForEntity(getBlogURI(), post, Void.class);
        ResponseEntity<String> getPostsResponse = restTemplate.getForEntity(getBlogURI(), String.class);

        assertEquals(201, addPostResponse.getStatusCode().value());
        assertEquals("[" + VALID_POST_1 + "]", getPostsResponse.getBody());
    }

    @Test
    public void shouldNotAddPostWhenPostWithoutTitleIsPosted() throws JsonProcessingException {
        clearPosts();
        Post postNoTitle = objectMapper.readValue(INVALID_POST_MISSING_TITLE, Post.class);

        ResponseEntity<Void> addPostNoTitleResponse = restTemplate.postForEntity(getBlogURI(), postNoTitle, Void.class);
        ResponseEntity<String> getPostsResponse = restTemplate.getForEntity(getBlogURI(), String.class);

        assertEquals(405, addPostNoTitleResponse.getStatusCode().value());
        // TODO: Add tests of custom reason phrase from API spec. Seems to require GlobalExceptionHandler, will skip for now
        assertEquals("[]", getPostsResponse.getBody());
    }

    @Test
    public void shouldNotAddPostWhenPostWithoutContentIsPosted() throws JsonProcessingException {
        clearPosts();
        Post postNoContent = objectMapper.readValue(INVALID_POST_MISSING_CONTENT, Post.class);
        ResponseEntity<Void> addPostNoContentResponse = restTemplate.postForEntity(getBlogURI(), postNoContent, Void.class);
        ResponseEntity<String> getPostsResponse = restTemplate.getForEntity(getBlogURI(), String.class);

        assertEquals(405, addPostNoContentResponse.getStatusCode().value());
        assertEquals("[]", getPostsResponse.getBody());
    }

    @Test
    public void shouldReturnPostWhenExistingIsRequestedById() throws JsonProcessingException {
        clearPosts();
        Post post1 = objectMapper.readValue(VALID_POST_1, Post.class);
        restTemplate.postForEntity(getBlogURI(), post1, Void.class);
        String post1id = post1.getId();

        ResponseEntity<String> responseEntity = restTemplate.getForEntity(getBlogURI() + post1id, String.class);
        assertEquals(200, responseEntity.getStatusCode().value());
        assertEquals(VALID_POST_1, responseEntity.getBody());
    }

    @Test
    public void shouldReturnNotFoundIfNotExistingPostIsRequested() throws JsonProcessingException {
        clearPosts();
        Post post = objectMapper.readValue(VALID_POST_1, Post.class);
        restTemplate.postForEntity(getBlogURI(), post, Void.class);
        String notPostId = post.getId() + "1";

        ResponseEntity<String> getPostResponse = restTemplate.getForEntity(getBlogURI() + notPostId, String.class);

        assertEquals(404, getPostResponse.getStatusCode().value());
    }

    @Test
    public void shouldReturnEmptyArrayWhenBlogHasNoPosts() {
        clearPosts();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(getBlogURI(), String.class);

        assertEquals(200, responseEntity.getStatusCode().value());
        assertEquals("[]", responseEntity.getBody());
    }

    @Test
    public void shouldReturnAllPostsWhenBlogHasPosts() throws JsonProcessingException {
        clearPosts();
        Post post1 = objectMapper.readValue(VALID_POST_1, Post.class);
        restTemplate.postForEntity(getBlogURI(), post1, Void.class);
        Post post2 = objectMapper.readValue(VALID_POST_2, Post.class);
        restTemplate.postForEntity(getBlogURI(), post2, Void.class);

        ResponseEntity<String> getPostsResponse = restTemplate.getForEntity(getBlogURI(), String.class);

        assertEquals(200, getPostsResponse.getStatusCode().value());
        assertNotNull(getPostsResponse.getBody());
        List<Post> posts = objectMapper.readValue(getPostsResponse.getBody(), new TypeReference<List<Post>>(){});
        assertEquals(2, posts.size());
        assertEquals(post1.getId(), posts.get(0).getId());
        assertEquals(post1.getTitle(), posts.get(0).getTitle());
        assertEquals(post1.getContent(), posts.get(0).getContent());
        assertEquals(post2.getId(), posts.get(1).getId());
        assertEquals(post2.getTitle(), posts.get(1).getTitle());
        assertEquals(post2.getContent(), posts.get(1).getContent());
    }

    @Test
    public void shouldUpdateExistingPostIfValidUpdateIsSent() throws JsonProcessingException {
        clearPosts();
        Post post = objectMapper.readValue(VALID_POST_1, Post.class);
        restTemplate.postForEntity(getBlogURI(), post, Void.class);
        Post updated = objectMapper.readValue(VALID_UPDATE_POST_1, Post.class);
        HttpEntity<Post> updatePost = new HttpEntity<>(updated);
        ResponseEntity<Void> updatePostResponse = restTemplate.exchange(getBlogURI(), HttpMethod.PUT, updatePost, Void.class);

        assertEquals(201, updatePostResponse.getStatusCode().value());
        ResponseEntity<String> getPostsResponse = restTemplate.getForEntity(getBlogURI(), String.class);
        assertEquals("[" + VALID_UPDATE_POST_1 + "]", getPostsResponse.getBody());
    }

    @Test
    public void shouldNotUpdateExistingPostIfInvalidUpdateIsSent() throws JsonProcessingException {
        clearPosts();
        Post post = objectMapper.readValue(VALID_POST_1, Post.class);
        restTemplate.postForEntity(getBlogURI(), post, Void.class);
        Post updated = objectMapper.readValue(INVALID_POST_MISSING_TITLE, Post.class);
        HttpEntity<Post> updatePost = new HttpEntity<>(updated);
        ResponseEntity<Void> updatePostResponse =
                restTemplate.exchange(getBlogURI(), HttpMethod.PUT, updatePost, Void.class);

        assertEquals(405, updatePostResponse.getStatusCode().value());
        ResponseEntity<String> getPostsResponse = restTemplate.getForEntity(getBlogURI(), String.class);
        assertEquals("[" + VALID_POST_1 + "]", getPostsResponse.getBody());
    }

    @Test
    public void shouldReturnNotFoundIfUpdatedPostDoesNotExist() throws JsonProcessingException {
        clearPosts();
        Post post = objectMapper.readValue(VALID_POST_1, Post.class);
        restTemplate.postForEntity(getBlogURI(), post, Void.class);
        Post updated = objectMapper.readValue(VALID_POST_2, Post.class);
        HttpEntity<Post> updatePost = new HttpEntity<>(updated);
        ResponseEntity<Void> updatePostResponse =
                restTemplate.exchange(getBlogURI(), HttpMethod.PUT, updatePost, Void.class);

        assertEquals(404, updatePostResponse.getStatusCode().value());
        ResponseEntity<String> getPostsResponse = restTemplate.getForEntity(getBlogURI(), String.class);
        assertEquals("[" + VALID_POST_1 + "]", getPostsResponse.getBody());
    }

    @Test
    public void shouldDeletePostIfItExist() throws JsonProcessingException {
        clearPosts();
        Post post = objectMapper.readValue(VALID_POST_1, Post.class);
        restTemplate.postForEntity(getBlogURI(), post, Void.class);

        ResponseEntity<Void> deletePostResponse =
                restTemplate.exchange(getBlogURI() + post.getId(), HttpMethod.DELETE, null, Void.class);

        assertEquals(200, deletePostResponse.getStatusCode().value());
        ResponseEntity<String> getPostsResponse = restTemplate.getForEntity(getBlogURI(), String.class);
        assertEquals("[]", getPostsResponse.getBody());
    }

    @Test
    public void shouldReturnNotFoundIfDeletedPostDoesNotExist() throws JsonProcessingException {
        clearPosts();
        Post post = objectMapper.readValue(VALID_POST_1, Post.class);
        restTemplate.postForEntity(getBlogURI(), post, Void.class);
        String notAvailablePostId = post.getId() + "1";

        ResponseEntity<Void> deletePostResponse =
                restTemplate.exchange(getBlogURI() + notAvailablePostId, HttpMethod.DELETE, null, Void.class);

        assertEquals(404, deletePostResponse.getStatusCode().value());
        ResponseEntity<String> getPostsResponse = restTemplate.getForEntity(getBlogURI() + post.getId(), String.class);
        assertEquals(VALID_POST_1, getPostsResponse.getBody());
    }
}
