package org.example.ecommerce.repository;

import org.example.ecommerce.entity.RecommenderSystem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecommenderSystemRepo extends JpaRepository<RecommenderSystem,Integer> {
}
