package com.pierceecom.blog.service;

import com.pierceecom.blog.model.Post;

import java.util.List;

public interface BlogWebServiceInterface {
    List<Post> getAllPosts();

    void addPost(Post post);

    void updatePost(Post post);

    void deletePost(String postId);
}
