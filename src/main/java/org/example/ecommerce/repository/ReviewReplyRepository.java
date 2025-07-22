package org.example.ecommerce.repository;

import org.example.ecommerce.entity.ReviewReply;
import org.example.ecommerce.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ReviewReplyRepository extends JpaRepository<ReviewReply, Integer> {
    Optional<ReviewReply> findByReview(Review review);
} 