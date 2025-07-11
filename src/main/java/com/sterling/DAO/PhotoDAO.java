package com.sterling.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.sterling.Connection.DBConnection;
import com.sterling.Interfaces.PhotoDAOInterface;
import com.sterling.Models.Photo;

public class PhotoDAO implements PhotoDAOInterface {
    @Override
    public void addPhoto(Photo photo) {
        String sql = "INSERT INTO photos (user_id, image_url, uploaded_at) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection()){
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, photo.getUserId());
            ps.setString(2, photo.getImageUrl());
            ps.setTimestamp(3, photo.getUploadedAt());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Photo> getPhotosByUserId(int userId) {
        String sql = "SELECT * FROM photos WHERE user_id = ?";
        List<Photo> photos = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                photos.add(
                    new Photo(
                        rs.getInt("photo_id"),
                        rs.getInt("user_id"),
                        rs.getString("image_url"),
                        rs.getTimestamp("uploaded_at")
                    )
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return photos;
    }
    @Override
    public Photo getPhotoByPhotoId(int photoId) {
        String sql = "SELECT * FROM photos WHERE photo_id = ?";
        Photo photo = null;

        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, photoId);

            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                photo = new Photo(
                    rs.getInt("photo_id"),
                    rs.getInt("user_id"),
                    rs.getString("image_url"),
                    rs.getTimestamp("uploaded_at")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return photo;
    }

    @Override
    public boolean deletePhoto(int photoId) {
        String sql = "DELETE FROM photos WHERE photo_id = ?";

        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, photoId);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
