package org.example.ecommerce.repository;

import org.example.ecommerce.entity.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComplaintRepository extends JpaRepository<Complaint, Integer> {
} 