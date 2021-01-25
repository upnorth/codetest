package com.pierceecom.blog.service;

import com.pierceecom.blog.model.Blog;
import com.pierceecom.blog.model.Post;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogWebService implements BlogWebServiceInterface {
    private Blog blog;

    BlogWebService() { this.blog = new Blog(); }

    @Override
    public List<Post> getAllPosts() { return blog.getPosts(); }

    @Override
    public Post addPost(Post post) {
        return blog.addPost(post);
    }

    @Override
    public void updatePost(Post updatedPost) {
        blog.updatePost(updatedPost);
    }

    @Override
    public void deletePost(String postId) {
        blog.deletePost(postId);
    }

    @Override
    public Post getPost(String postId) {
        return blog.findPost(postId);
    }
}
