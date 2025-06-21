package com.sterling.Services;

import java.util.List;

import com.sterling.Interfaces.BlogPostDAOInterface;
import com.sterling.Models.BlogPost;

public class BlogPostService {
    private final BlogPostDAOInterface blogPostDAO;

    public BlogPostService(BlogPostDAOInterface blogPostDAO){
        this.blogPostDAO = blogPostDAO;
    }

    public void createPost(BlogPost post){
        blogPostDAO.createPost(post);
    }

    public BlogPost getPostById(int postId){
        return blogPostDAO.getPostById(postId);
    }

    public List<BlogPost> getAllBlogPosts(){
        return blogPostDAO.getAllBlogPosts();
    }

    public List<BlogPost> getPostsByUserId(int userId){
        return blogPostDAO.getPostsByUserId(userId);
    }

    public boolean updatePost(BlogPost post){
        return blogPostDAO.updatePost(post);
    }

    public boolean deletePost(int postId){
        return blogPostDAO.deletePost(postId);
    }
}
