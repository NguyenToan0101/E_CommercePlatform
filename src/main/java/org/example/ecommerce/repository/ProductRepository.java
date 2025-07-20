package org.example.ecommerce.repository;


import org.example.ecommerce.entity.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Override
    @EntityGraph(attributePaths = {
            "shopid",
            "categoryid",
            "inventories",
            "productimages"
    })
    List<Product> findAll();  //join-fetch shop, category, inventory & images

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

    //tim theo danh muc chinh
    @EntityGraph(attributePaths = {
            "shopid","categoryid","inventories","productimages"
    })
    @Query("""
      SELECT DISTINCT p
      FROM Product p
      JOIN p.categoryid c
      WHERE c.id = :mainId
         OR c.parent.id = :mainId
    """)
    List<Product> findByMainCategoryId(@Param("mainId") Integer mainCategoryId);

    //view detail
    @EntityGraph(attributePaths = {
            "shopid",
            "categoryid",
            "inventories",
            "productimages"
    })
    @Query("""
      SELECT p FROM Product p
      LEFT JOIN FETCH p.inventories
      LEFT JOIN FETCH p.productimages
      LEFT JOIN FETCH p.shopid
      LEFT JOIN FETCH p.categoryid
      WHERE p.id = :id
    """)
    Optional<Product> findWithAllById(@Param("id") Integer id);

    List<Product> findByStatusAndLockedUntilBefore(String status, Instant time);


}
