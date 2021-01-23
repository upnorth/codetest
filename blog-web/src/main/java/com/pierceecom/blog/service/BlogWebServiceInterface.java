package com.pierceecom.blog.service;

import com.pierceecom.blog.model.Post;

import java.util.List;

public interface BlogWebServiceInterface {
    List<Post> getAllPosts();

    void addPost(Post post);
}
