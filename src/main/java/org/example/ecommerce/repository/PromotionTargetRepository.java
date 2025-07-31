package org.example.ecommerce.repository;

import org.example.ecommerce.entity.PromotionTarget;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromotionTargetRepository extends JpaRepository<PromotionTarget, Integer> {
}
