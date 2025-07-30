package org.example.ecommerce.repository;


import org.example.ecommerce.entity.conplaint.ComplaintReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComplaintFeedbackRepository extends JpaRepository<ComplaintReview, Integer> {
}
