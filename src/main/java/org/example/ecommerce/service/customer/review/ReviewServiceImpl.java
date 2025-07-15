package org.example.ecommerce.service.customer.review;

import org.example.ecommerce.entity.Review;
import org.example.ecommerce.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class ReviewServiceImpl implements ReviewService{
    @Autowired
    private ReviewRepository reviewRepository;

    public boolean submitReview(Review review) {
        if (reviewRepository.existsByProductidAndOrderid(review.getProductid(), review.getOrderid())) {
            return false;
        }
        review.setCreatedat(Instant.now());
        reviewRepository.save(review);

        return true;
    }
}
