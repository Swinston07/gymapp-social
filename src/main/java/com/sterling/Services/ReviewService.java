package com.sterling.Services;

import java.util.List;

import com.sterling.Interfaces.ReviewDAOInterface;
import com.sterling.Models.Review;

public class ReviewService {
    private final ReviewDAOInterface reviewDao;

    public ReviewService(ReviewDAOInterface reviewDao) {
        this.reviewDao = reviewDao;
    }

    public Review createReview(Review review) {
        return reviewDao.createReview(review);
    }

    public List<Review> getReviewsForUser(int reviewedUserId){
        return reviewDao.getReviewsForUser(reviewedUserId);
    }

    public List<Review> getReviewsWrittenByUser(int reviewerId) {
        return reviewDao.getReviewsWrittenByUser(reviewerId);
    }

    public double getAverageRatingForUser(int reviewedUserId) {
        return reviewDao.getAverageRatingForUser(reviewedUserId);
    }

    public boolean deleteReview(int reviewId) {
        return reviewDao.deleteReview(reviewId);
    }
}
