package com.sterling.Interfaces;

import java.util.List;

import com.sterling.Models.BlogPost;

public interface BlogPostDAOInterface {
    void createPost(BlogPost post);
    BlogPost getPostById(int postId);
    List<BlogPost> getAllBlogPosts();
    List<BlogPost> getPostsByUserId(int userId);
    boolean updatePost(BlogPost post);
    boolean deletePost(int postId);
}
