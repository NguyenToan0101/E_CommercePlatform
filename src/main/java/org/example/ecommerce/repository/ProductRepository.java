package org.example.ecommerce.repository;


import org.example.ecommerce.entity.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findAll();
    List<Product> findByNameContainingIgnoreCase(String keyword);
    Product findById(int id);

    @EntityGraph(attributePaths = {"categoryid", "productimages", "inventories"})
    List<Product> findByShopidId(Integer shopId);

    @EntityGraph(attributePaths = {"categoryid", "productimages", "inventories"})
    List<Product> findByShopidIdAndNameContainingIgnoreCase(Integer shopId, String keyword);

    List<Product> findByShopidIdAndStatus(Integer shopId, String status);

    @Query("SELECT p.status, COUNT(p) FROM Product p WHERE p.shopid.id = :shopId GROUP BY p.status")
    List<Object[]> countProductsByStatus(@Param("shopId") Integer shopId);

    long countByShopidId(Integer shopId);
}
