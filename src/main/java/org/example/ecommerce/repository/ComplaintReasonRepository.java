package org.example.ecommerce.repository;

import org.example.ecommerce.entity.conplaint.ComplaintCategory;
import org.example.ecommerce.entity.conplaint.ComplaintReason;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComplaintReasonRepository extends JpaRepository<ComplaintReason, Integer> {
    List<ComplaintReason> findAllByCategory(ComplaintCategory category);

    ComplaintReason findComplaintReasonByReasonId(Integer reasonId);
}
