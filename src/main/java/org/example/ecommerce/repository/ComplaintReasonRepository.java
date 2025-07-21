package org.example.ecommerce.repository;

import org.example.ecommerce.entity.conplaint.ComplaintReason;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComplaintReasonRepository extends JpaRepository<ComplaintReason, Integer> {
}
