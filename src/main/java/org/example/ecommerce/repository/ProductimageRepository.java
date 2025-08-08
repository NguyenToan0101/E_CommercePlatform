package org.example.ecommerce.repository;

import org.example.ecommerce.entity.Product;
import org.example.ecommerce.entity.Productimage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductimageRepository extends JpaRepository<Productimage, Integer> {
    List<Productimage> findAllByProductid(Product p);

    Optional<Productimage> findFirstByProductidOrderByIdAsc(Product productid);

    // Lấy URL ảnh đầu tiên bằng native query để chỉ select imageurl, tránh load embedding
    @Query(value = "SELECT imageurl FROM productimages WHERE productid = :productId ORDER BY imageid ASC LIMIT 1", nativeQuery = true)
    String findFirstImageUrlByProductId(@Param("productId") Integer productId);

    // Lite images for edit form (no embedding)
    @Query("select pi.id as id, pi.imageurl as imageurl from Productimage pi where pi.productid.id = :productId order by pi.id asc")
    List<ProductImageLite> findLiteByProductId(@Param("productId") Integer productId);

    // Bulk delete without fetching entities (avoids touching embedding)
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from Productimage pi where pi.productid = :product")
    void deleteAllByProduct(@Param("product") Product product);
}
