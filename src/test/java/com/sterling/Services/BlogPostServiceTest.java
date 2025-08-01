package com.sterling.Services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.sterling.Interfaces.BlogPostDAOInterface;
import com.sterling.Models.BlogPost;

public class BlogPostServiceTest {
    BlogPostDAOInterface blogPostDAO;
    BlogPostService blogPostService;

    @BeforeEach
    public void setUp(){
        blogPostDAO = mock(BlogPostDAOInterface.class);
        blogPostService = new BlogPostService(blogPostDAO);
    }

    @Test
    void testCreatePost() {
        BlogPost post = new BlogPost();

        post.setUserId(1);
        post.setContent("Test Content");
        post.setMediaUrl("http://example.com/image.jpg");

        blogPostService.createPost(post);

        verify(blogPostDAO, times(1)).createPost(post);
    }

    @Test
    void testDeletePost() {
        int postId = 2;

        when(blogPostDAO.deletePost(postId)).thenReturn(true);

        boolean result = blogPostService.deletePost(postId);

        assertTrue(result);
        verify(blogPostDAO, times(1)).deletePost(postId);
    }

    @Test
    void testGetAllBlogPosts() {
        List<BlogPost> posts = List.of(
            new BlogPost(1, 2, "First Post", null, null, null),
            new BlogPost(2, 3, "Second Post", null, null, null)
        );

        when(blogPostDAO.getAllBlogPosts()).thenReturn(posts);

        List<BlogPost> result = blogPostService.getAllBlogPosts();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("First Post", result.get(0).getContent());
        assertEquals("Second Post", result.get(1).getContent());
        verify(blogPostDAO, times(1)).getAllBlogPosts();
    }

    @Test
    void testGetPostById() {
        BlogPost post = new BlogPost();
        post.setUserId(1);
        post.setPostId(2);
        post.setContent("Sample blog post");

        when(blogPostDAO.getPostById(2)).thenReturn(post);

        BlogPost result = blogPostService.getPostById(2);

        assertNotNull(result);
        assertEquals("Sample blog post", result.getContent());
        verify(blogPostDAO, times(1)).getPostById(2);
    }

    @Test
    void testGetPostsByUserId() {
        int userId = 2;
        List<BlogPost> posts = List.of(
            new BlogPost(1, userId, "First Post", null, null, null),
            new BlogPost(2, userId, "Second Post", null, null, null)
            );


        when(blogPostDAO.getPostsByUserId(userId)).thenReturn(posts);

        List<BlogPost> result = blogPostService.getPostsByUserId(userId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(userId, result.get(0).getUserId());
        assertEquals(userId, result.get(1).getUserId());
        assertEquals("First Post", result.get(0).getContent());
        assertEquals("Second Post", result.get(1).getContent());
        verify(blogPostDAO, times(1)).getPostsByUserId(2);
    }

    @Test
    void testUpdatePost() {
        BlogPost updatedPost = new BlogPost();

        updatedPost.setPostId(1);
        updatedPost.setUserId(4);
        updatedPost.setContent("Sample Post");

        when(blogPostDAO.updatePost(updatedPost)).thenReturn(true);

        boolean result = blogPostService.updatePost(updatedPost);

        assertTrue(result);
        verify(blogPostDAO, times(1)).updatePost(updatedPost);
    }
}
