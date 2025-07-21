package org.example.ecommerce.repository;

import org.example.ecommerce.entity.conplaint.ComplaintCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComplaintCategoryRepository extends JpaRepository<ComplaintCategory, Integer> {
}
