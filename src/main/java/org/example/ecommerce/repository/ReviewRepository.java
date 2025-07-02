package org.example.ecommerce.repository;


import org.example.ecommerce.entity.Order;
import org.example.ecommerce.entity.Product;
import org.example.ecommerce.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.productid = :product")
    Float findAverageRatingByProductid(@Param("product") Product product);
    boolean existsByProductidAndOrderid(Product product, Order order);
    List<Review> findAllByProductidOrderByCreatedatDesc(Product product);
    Integer countByProductid(Product productid);


}
