package com.sterling.DAO;

import com.sterling.Connection.DBConnection;
import com.sterling.Interfaces.ReviewDAOInterface;
import com.sterling.Models.Review;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAO implements ReviewDAOInterface {

    @Override
    public Review createReview(Review review) {
        String sql = "INSERT INTO reviews (session_id, reviewer_id, reviewed_id, rating, comment) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, review.getSessionId());
            stmt.setInt(2, review.getReviewerId());
            stmt.setInt(3, review.getReviewedId());
            stmt.setInt(4, review.getRating());
            stmt.setString(5, review.getComment());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet keys = stmt.getGeneratedKeys();
                if (keys.next()) {
                    review.setReviewId(keys.getInt(1));
                }
                return review;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Review> getReviewsForUser(int reviewedUserId) {
        String sql = "SELECT * FROM reviews WHERE reviewed_id = ? ORDER BY created_at DESC";
        List<Review> reviews = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, reviewedUserId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Review review = new Review(
                        rs.getInt("review_id"),
                        rs.getInt("session_id"),
                        rs.getInt("reviewer_id"),
                        rs.getInt("reviewed_id"),
                        rs.getInt("rating"),
                        rs.getString("comment"),
                        rs.getTimestamp("created_at")
                );
                reviews.add(review);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reviews;
    }

    @Override
    public List<Review> getReviewsWrittenByUser(int reviewerId) {
        String sql = "SELECT * FROM reviews WHERE reviewer_id = ?";
        List<Review> reviews = new ArrayList<>();

        try(Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, reviewerId);

            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                reviews.add(
                    new Review(
                        rs.getInt("review_id"),
                        rs.getInt("session_id"),
                        rs.getInt("reviewer_id"),
                        rs.getInt("reviewed_id"),
                        rs.getInt("rating"),
                        rs.getString("comment"),
                        rs.getTimestamp("created_at")
                    )
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reviews;
    }

    @Override
    public double getAverageRatingForUser(int reviewedUserId) {
        String sql = "SELECT AVG(rating) AS avg_rating FROM reviews WHERE reviewed_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, reviewedUserId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getDouble("avg_rating");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0.0;
    }

    @Override
    public boolean deleteReview(int reviewId) {
        String sql = "DELETE FROM reviews WHERE review_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, reviewId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
