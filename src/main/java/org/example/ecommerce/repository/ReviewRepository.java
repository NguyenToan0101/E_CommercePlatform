package org.example.ecommerce.repository;


import org.example.ecommerce.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    @Query("SELECT r.rating FROM Review r WHERE r.productid.id = :productId")
    List<Integer> findRateById(@Param("productId") Integer productId);
    List<Review> findByProductid_Id(Integer productId);
}
