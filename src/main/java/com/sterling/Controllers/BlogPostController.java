package com.sterling.Controllers;

import java.util.List;

import com.sterling.Models.BlogPost;
import com.sterling.Services.BlogPostService;

import io.javalin.http.Context;

public class BlogPostController {
    private final BlogPostService blogPostService;

    public BlogPostController(BlogPostService blogPostService){
        this.blogPostService = blogPostService;
    }

    public void createPost(Context ctx){
        int requesterId = ctx.attribute("userId");
        int userId = Integer.parseInt(ctx.pathParam("userId"));
        BlogPost post = ctx.bodyAsClass(BlogPost.class);

        System.out.println("Requester ID: " + requesterId);
        System.out.println("Path User ID: " + userId);
        System.out.println("Post Content: " + post.getContent());
        System.out.println("Post Media URL: " + post.getMediaUrl());

        if(requesterId!=userId){
                ctx.status(403).result("You are not authorized to create a post for this user");
                return;
        }

        post.setUserId(userId);

        if(post.getContent() == null || post.getContent().trim().isEmpty()){
            ctx.status(400).result("Content body cannot be empty");
            return;
        }

        try{
            blogPostService.createPost(post);
            ctx.status(201).result("Blog post created successfully");
        } catch(Exception e){
            e.printStackTrace();
            ctx.status(500).result("Failed to create blog post: " + e.getMessage());
        }
    }

    public void getPostById(Context ctx){
        int postId = Integer.parseInt(ctx.pathParam("postId"));

        BlogPost post = blogPostService.getPostById(postId);

        if(post == null){
            ctx.status(404).result("Post not found");
        } else {
            ctx.json(post);
        }
    }

    public void getAllBlogPosts(Context ctx){
        try{
            List<BlogPost> posts = blogPostService.getAllBlogPosts();
            ctx.json(posts);
        } catch (Exception e){
            e.printStackTrace();
            ctx.status(500).result("Failed to retrieve blog posts");
        }
    }

    public void getPostsByUserId(Context ctx){
        int userId = Integer.parseInt(ctx.pathParam("userId"));

        try{
            List<BlogPost> posts = blogPostService.getPostsByUserId(userId);
            ctx.json(posts);
        } catch (Exception e){
            e.printStackTrace();
            ctx.status(500).result("Failed to retrieve blog posts");
        }
    }

    public void updatePost(Context ctx){
        int requesterId = ctx.attribute("userId");
        int postId = Integer.parseInt(ctx.pathParam("postId"));
        BlogPost existingPost = blogPostService.getPostById(postId);

        if(existingPost == null){
            ctx.status(404).result("Post not found");
            return;
        }

        if(existingPost.getUserId() != requesterId){
            ctx.status(403).result("You are not authorized to update this post");
            return;
        }


        BlogPost updatedPost = ctx.bodyAsClass(BlogPost.class);
        updatedPost.setPostId(postId);
        updatedPost.setUserId(requesterId);

        if(updatedPost.getContent() == null || updatedPost.getContent().trim().isEmpty()){
            ctx.status(400).result("Content body cannot be empty");
            return;
        } 

        boolean success = blogPostService.updatePost(updatedPost);

        if(success){
            ctx.status(200).result("Post updated successfully");
        } else{
            ctx.status(500).result("Failed to update post");
        }
    }

    public void deletePost(Context ctx){
        int requesterId = ctx.attribute("userId");
        int postId = Integer.parseInt(ctx.pathParam("postId"));
        BlogPost post = blogPostService.getPostById(postId);

        if(post == null){
            ctx.status(404).result("Post does not exist");
            return;
        }

        if(post.getUserId() != requesterId){
            ctx.status(403).result("You are not authorized to delete this post");
            return;
        }

        boolean deleted = blogPostService.deletePost(postId);

        if(deleted){
            ctx.status(200).result("Post deleted successfully");
        }
        else{
            ctx.status(500).result("Failed to delete post");
        }
    }
}
