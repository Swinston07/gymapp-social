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

import com.sterling.Interfaces.ReviewDAOInterface;
import com.sterling.Models.Review;

public class ReviewServiceTest {

    private ReviewDAOInterface reviewDao;
    private ReviewService reviewService;

    @BeforeEach
    public void setup() {
        reviewDao = mock(ReviewDAOInterface.class);
        reviewService = new ReviewService(reviewDao);
    }
    
    @Test
    void testCreateReview() {
        Review review = new Review();
        review.setReviewerId(1);
        review.setReviewedId(2);
        review.setRating(5);
        review.setComment("Great session");

        when(reviewDao.createReview(review)).thenReturn(review);

        Review result = reviewService.createReview(review);

        assertNotNull(result);
        assertEquals(1, result.getReviewerId());
        assertEquals(2, result.getReviewedId());
        assertEquals(5, result.getRating());
        assertEquals("Great session", result.getComment());

        verify(reviewDao, times(1)).createReview(result);
    }

    @Test
    void testDeleteReview() {
        when(reviewDao.deleteReview(1)).thenReturn(true);

        boolean result = reviewService.deleteReview(1);
        
        assertTrue(result);
        verify(reviewDao, times(1)).deleteReview(1);
    }

    @Test
    void testGetAverageRatingForUser() {
        when(reviewDao.getAverageRatingForUser(1)).thenReturn(4.3);

        double avg = reviewService.getAverageRatingForUser(1);

        assertEquals(4.3, avg);
        verify(reviewDao, times(1)).getAverageRatingForUser(1);
    }

    @Test
    void testGetReviewsForUser() {
        Review review1 = new Review();
        review1.setReviewId(1);
        review1.setReviewerId(2);
        review1.setReviewedId(3);
        review1.setRating(4);
        review1.setComment("Great work");

        Review review2 = new Review();
        review2.setReviewId(5);
        review2.setReviewerId(6);
        review2.setReviewedId(3);
        review2.setRating(5);
        review2.setComment("Great sesh");

        List<Review> mockReviews = List.of(review1, review2);

        when(reviewDao.getReviewsForUser(3)).thenReturn(mockReviews);

        List<Review> result = reviewService.getReviewsForUser(3);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getReviewId());
        assertEquals(2, result.get(0).getReviewerId());
        assertEquals(3, result.get(0).getReviewedId());
        assertEquals(4, result.get(0).getRating());
        assertEquals("Great work", result.get(0).getComment());
        assertEquals(5, result.get(1).getReviewId());
        assertEquals(6, result.get(1).getReviewerId());
        assertEquals(3, result.get(1).getReviewedId());
        assertEquals(5, result.get(1).getRating());
        assertEquals("Great sesh", result.get(1).getComment());
    }

    @Test
    void testGetReviewsWrittenByUser() {
        Review review = new Review();
        review.setReviewId(1);
        review.setReviewerId(2);
        review.setReviewedId(3);
        review.setRating(4);
        review.setComment("Great partner");

        when(reviewDao.getReviewsWrittenByUser(2)).thenReturn(List.of(review));

        List<Review> result = reviewService.getReviewsWrittenByUser(2);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getReviewId());
        assertEquals(2, result.get(0).getReviewerId());
        assertEquals(3, result.get(0).getReviewedId());
        assertEquals(4, result.get(0).getRating());
        assertEquals("Great partner", result.get(0).getComment());

        verify(reviewDao, times(1)).getReviewsWrittenByUser(2);
    }
}
