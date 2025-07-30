package org.example.ecommerce.repository;

import org.example.ecommerce.entity.PromotionCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromotionCategoryRepository extends JpaRepository<PromotionCategory, Integer> {
}
