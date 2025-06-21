package com.sterling.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.sterling.Connection.DBConnection;
import com.sterling.Interfaces.BlogPostDAOInterface;
import com.sterling.Models.BlogPost;

public class BlogPostDAO implements BlogPostDAOInterface {
    @Override
    public void createPost(BlogPost post){
        String sql = "INSERT INTO blog_posts(user_id, content, media_url) VALUES(?, ?, ?)";

        try(Connection conn = DBConnection.getConnection()){
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, post.getUserId());
            ps.setString(2, post.getContent());
            ps.setString(3, post.getMediaUrl());

            ps.executeUpdate();

        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public BlogPost getPostById(int postId){
        String sql = "SELECT bp.*, u.username FROM blog_posts bp JOIN users u ON bp.user_id = u.id WHERE bp.post_id = ?";
            
        try(Connection conn = DBConnection.getConnection()){
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, postId);

            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                return new BlogPost(
                    rs.getInt("post_id"),
                    rs.getInt("user_id"),
                    rs.getString("content"),
                    rs.getTimestamp("created_at"),
                    rs.getString("media_url"),
                    rs.getString("username")
                );
            }

        } catch(SQLException e){
                e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<BlogPost> getAllBlogPosts(){
        String sql = "SELECT bp.*, u.username FROM blog_posts bp JOIN users u ON bp.user_id = u.id";
        List<BlogPost> posts = new ArrayList<>();

        try(Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                posts.add(
                    new BlogPost(
                        rs.getInt("post_id"),
                        rs.getInt("user_id"),
                        rs.getString("content"),
                        rs.getTimestamp("created_at"),
                        rs.getString("media_url"),
                        rs.getString("username")
                    )
                );
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
        return posts;
    }

    @Override
    public List<BlogPost> getPostsByUserId(int userId){
        String sql = "SELECT bp.*, u.username FROM blog_posts bp JOIN users u ON bp.user_id = u.id WHERE bp.user_id = ?";
        List<BlogPost> posts = new ArrayList<>();

        try(Connection conn = DBConnection.getConnection()){
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                posts.add(
                    new BlogPost(
                    rs.getInt("post_id"),
                    rs.getInt("user_id"),
                    rs.getString("content"),
                    rs.getTimestamp("created_at"),
                    rs.getString("media_url"),
                    rs.getString("username")
                    )
                );
            }
        } catch(SQLException e){
            e.printStackTrace();;
        }
        return posts;
    }

    @Override
    public boolean updatePost(BlogPost post){
        String sql = "UPDATE blog_posts SET content = ? WHERE post_id = ?";
        
        try(Connection conn = DBConnection.getConnection()){
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1, post.getContent());
            ps.setInt(2, post.getPostId());

            int rowsAffected = ps.executeUpdate();

            return rowsAffected > 0;
        } catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deletePost(int postId){
        String sql = "DELETE FROM blog_posts WHERE post_id = ?";

        try(Connection conn = DBConnection.getConnection()){
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, postId);

            int rowsAffected = ps.executeUpdate();

            return rowsAffected > 0;
        } catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
