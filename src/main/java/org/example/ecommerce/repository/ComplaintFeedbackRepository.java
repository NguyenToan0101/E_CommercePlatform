package org.example.ecommerce.repository;

import org.example.ecommerce.entity.conplaint.ComplaintFeedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComplaintFeedbackRepository extends JpaRepository<ComplaintFeedback, Integer> {
}
