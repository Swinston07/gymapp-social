package com.sterling.Interfaces;

import java.util.List;

import com.sterling.Models.Review;

public interface ReviewDAOInterface {
    Review createReview(Review reciew);
    List<Review> getReviewsForUser(int reviewedUserId);
    double getAverageRatingForUser(int reviewedUserId);
    boolean deleteReview(int reviewId);
}
