package org.example.ecommerce.repository;

import org.example.ecommerce.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromotionRepository  extends JpaRepository<Promotion, Integer> {
}
