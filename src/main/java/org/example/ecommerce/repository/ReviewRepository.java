package org.example.ecommerce.repository;


import org.example.ecommerce.entity.Orderitem;
import org.example.ecommerce.entity.Product;
import org.example.ecommerce.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.productid = :product")
    Float findAverageRatingByProductid(@Param("product") Product product);
    boolean existsByProductidAndOrderitemid(Product product, Orderitem order);
    List<Review> findAllByProductidOrderByCreatedatDesc(Product product);
    Integer countByProductid(Product productid);


    List<Integer> findRateById(Integer id);
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.productid= :product AND r.rating > :rate")
    Float findAverageRatingByProductidAndRatingGreaterThan(@Param("product")Product productid, @Param("rate") Integer ratingIsGreaterThan);
}
