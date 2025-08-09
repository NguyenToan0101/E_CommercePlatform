package org.example.ecommerce.repository;


import org.example.ecommerce.entity.Category;
import org.example.ecommerce.entity.Product;
import org.example.ecommerce.entity.Shop;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Override
    @EntityGraph(attributePaths = {
            "shopid",
            "categoryid",
            "inventories"
    })
    List<Product> findAll();  //join-fetch shop, category, inventory & images

    // Query tối ưu để tránh lỗi embedding array
    @Query("""
        SELECT p FROM Product p
        LEFT JOIN FETCH p.shopid
        LEFT JOIN FETCH p.categoryid
        LEFT JOIN FETCH p.inventories
        """)
    List<Product> findAllWithoutEmbeddingIssues();

    List<Product> findByNameContainingIgnoreCase(String keyword);
    Product findById(int id);

    // EntityGraph cho list sản phẩm (không load embedding)
    @EntityGraph(attributePaths = {"categoryid", "inventories"})
    List<Product> findByShopidId(Integer shopId);

    @EntityGraph(attributePaths = {"categoryid", "inventories"})
    List<Product> findByShopidIdAndNameContainingIgnoreCase(Integer shopId, String keyword);

    List<Product> findByShopidIdOrderById(Integer shopId);

    List<Product> findByShopidIdAndStatusOrderByIdDesc(Integer shopId, String status);

    // Method riêng để load sản phẩm với images cho list view
    @Query("""
        SELECT p FROM Product p
        LEFT JOIN FETCH p.inventories
        LEFT JOIN FETCH p.shopid
        LEFT JOIN FETCH p.categoryid
        WHERE p.shopid.id = :shopId
        ORDER BY p.id DESC
    """)
    List<Product> findByShopidIdWithImages(@Param("shopId") Integer shopId);

    // Method riêng để load sản phẩm với status và images
    @Query("""
        SELECT p FROM Product p
        LEFT JOIN FETCH p.inventories
        LEFT JOIN FETCH p.shopid
        LEFT JOIN FETCH p.categoryid
        WHERE p.shopid.id = :shopId AND p.status = :status
        ORDER BY p.id DESC
    """)
    List<Product> findByShopidIdAndStatusWithImages(@Param("shopId") Integer shopId, @Param("status") String status);

    // Method riêng để load sản phẩm với productimages (chỉ dùng khi cần thiết)
    @Query("""
        SELECT p FROM Product p
        LEFT JOIN FETCH p.inventories
        LEFT JOIN FETCH p.productimages
        LEFT JOIN FETCH p.shopid
        LEFT JOIN FETCH p.categoryid
        WHERE p.id = :productId
    """)
    Optional<Product> findByIdWithImages(@Param("productId") Integer productId);

    @Query("SELECT p.status, COUNT(p) FROM Product p WHERE p.shopid.id = :shopId GROUP BY p.status")
    List<Object[]> countProductsByStatus(@Param("shopId") Integer shopId);

    long countByShopidId(Integer shopId);

    //tim theo danh muc chinh
    @EntityGraph(attributePaths = {
            "shopid","categoryid","inventories"
    })
    @Query("""
      SELECT DISTINCT p
      FROM Product p
      JOIN p.categoryid c
      WHERE c.id = :mainId
         OR c.parent.id = :mainId
    """)
    List<Product> findByMainCategoryId(@Param("mainId") Integer mainCategoryId);

    //view detail - chỉ load khi cần thiết
    @EntityGraph(attributePaths = {
            "shopid",
            "categoryid",
            "inventories"
    })
    @Query("""
      SELECT p FROM Product p
      LEFT JOIN FETCH p.inventories
      LEFT JOIN FETCH p.shopid
      LEFT JOIN FETCH p.categoryid
      WHERE p.id = :id
    """)
    Optional<Product> findWithAllById(@Param("id") Integer id);

    @EntityGraph(attributePaths = {
            "shopid",
            "categoryid",
            "inventories",
            "reviews",
            "wishlists"
    })
    @Query("""
      SELECT DISTINCT p FROM Product p
      LEFT JOIN FETCH p.inventories
      LEFT JOIN FETCH p.shopid
      LEFT JOIN FETCH p.categoryid
      LEFT JOIN FETCH p.reviews
      LEFT JOIN FETCH p.wishlists
      WHERE p.id = :id
    """)
    Optional<Product> findWithAllRelationsById(@Param("id") Integer id);

    List<Product> findByStatusAndLockedUntilBefore(String status, Instant time);
    List<Product> findAllByShopid_Id(Integer shopidId);

    @Query("""
        SELECT p.id, p.name, p.status, p.createdat,
               s.fulladdress,
               (SELECT MIN(i.price) FROM Inventory i WHERE i.productid.id = p.id),
               (SELECT COALESCE(SUM(i.solditems), 0) FROM Inventory i WHERE i.productid.id = p.id AND i.solditems IS NOT NULL)
        FROM Product p
        LEFT JOIN p.shopid s
        WHERE p.status = 'available'
        ORDER BY p.createdat DESC
    """)
    List<Object[]> findAvailableProductsOptimized();

    @Query("""
        SELECT p.id, p.name, p.description, p.status, p.createdat,
               s.id as shopId, s.shopname, s.fulladdress, s.description as shopDescription, s.imageshop,
               c.id as categoryId, c.categoryname,
               (SELECT COALESCE(SUM(i.solditems), 0) FROM Inventory i WHERE i.productid.id = p.id AND i.solditems IS NOT NULL)
        FROM Product p
        LEFT JOIN p.shopid s
        LEFT JOIN p.categoryid c
        WHERE p.id = :productId
    """)
    List<Object[]> findProductDetailOptimized(@Param("productId") Integer productId);

    @Query("""
        SELECT p.id, p.name, p.status, p.createdat,
               s.fulladdress,
               (SELECT MIN(i.price) FROM Inventory i WHERE i.productid.id = p.id),
               (SELECT COALESCE(SUM(i.solditems), 0) FROM Inventory i WHERE i.productid.id = p.id AND i.solditems IS NOT NULL)
        FROM Product p
        LEFT JOIN p.shopid s
        WHERE p.status = 'available' AND s.id = :shopId
        ORDER BY p.createdat DESC
    """)
    List<Object[]> findAvailableProductsByShopIdOptimized(@Param("shopId") Integer shopId);

    @Query("""
        SELECT p.id, p.name, p.status, p.createdat,
               s.fulladdress,
               (SELECT MIN(i.price) FROM Inventory i WHERE i.productid.id = p.id),
               (SELECT COALESCE(SUM(i.solditems), 0) FROM Inventory i WHERE i.productid.id = p.id AND i.solditems IS NOT NULL)
        FROM Product p
        LEFT JOIN p.shopid s
        WHERE p.status = 'available' AND p.categoryid.id IN :categoryIds
        ORDER BY p.createdat DESC
    """)
    List<Object[]> findAvailableProductsByCategoryIdsOptimized(@Param("categoryIds") List<Integer> categoryIds);

    @Query("""
        SELECT p.id, p.name, p.status, p.createdat,
               s.fulladdress,
               (SELECT MIN(i.price) FROM Inventory i WHERE i.productid.id = p.id),
               (SELECT COALESCE(SUM(i.solditems), 0) FROM Inventory i WHERE i.productid.id = p.id AND i.solditems IS NOT NULL)
        FROM Product p
        LEFT JOIN p.shopid s
        WHERE p.status = 'available' AND LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%'))
        ORDER BY p.createdat DESC
    """)
    List<Object[]> findAvailableProductsByNameOptimized(@Param("keyword") String keyword);
    List<Product> findByShopidAndCategoryidIn(Shop shopid, Collection<Category> categoryids);
}