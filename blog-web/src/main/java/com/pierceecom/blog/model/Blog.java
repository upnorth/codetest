package com.pierceecom.blog.model;

import java.util.ArrayList;
import java.util.List;

public class Blog {
    // TODO: Persist with local or cloud database
    public List<Post> posts;
    public int nextPostId;

    public Blog(){
        this.posts = new ArrayList<>();
        this.nextPostId = 1;
    }
}
