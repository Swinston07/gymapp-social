package com.sterling.Controllers;

import java.util.List;

import com.sterling.Models.Review;
import com.sterling.Services.ReviewService;

import io.javalin.http.Context;

public class ReviewController {
    public final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    public void createReview(Context ctx) {
        int requesterId = ctx.attribute("userId");
        int userId = Integer.parseInt(ctx.pathParam("id"));

        if(requesterId != userId) {
            ctx.status(403).result("Unauthorized to create review on behalf of another user");
            return;
        }

        Review review = ctx.bodyAsClass(Review.class);
        Review created = reviewService.createReview(review);

        if (created != null) {
            ctx.status(201).json(created);
        } else {
            ctx.status(500).result("Failed to create review");
        }
    }

    public void getReviewsForUser(Context ctx) {
        int reviewedUserId = Integer.parseInt(ctx.pathParam("id"));
        List<Review> reviews = reviewService.getReviewsForUser(reviewedUserId);
        ctx.json(reviews);
    }

    public void getAverageRatingForUser(Context ctx) {
        int reviewedUserId = Integer.parseInt(ctx.pathParam("id"));
        double avgRating = reviewService.getAverageRatingForUser(reviewedUserId);
        ctx.json(avgRating);
    }

    public void deleteReview(Context ctx) {
        int requesterId = ctx.attribute("userId");
        int userId = Integer.parseInt(ctx.pathParam("id"));
        int reviewId = Integer.parseInt(ctx.pathParam("reviewId"));

        if(requesterId != userId) {
            ctx.status(403).result("Unauthorized to delete another user's review");
            return;
        }
        
        boolean deleted = reviewService.deleteReview(reviewId);

        if (deleted) {
            ctx.status(200).result("Review deleted.");
        } else {
            ctx.status(404).result("Review not found");
        }
    }
}
