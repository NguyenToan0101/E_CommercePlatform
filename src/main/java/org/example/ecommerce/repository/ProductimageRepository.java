package org.example.ecommerce.repository;

import org.example.ecommerce.entity.Product;
import org.example.ecommerce.entity.Productimage;
import org.example.ecommerce.service.SimilarImageProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductimageRepository extends JpaRepository<Productimage, Integer> {
    List<Productimage> findAllByProductid(Product p);

    Optional<Productimage> findFirstByProductidOrderByIdAsc(Product productid);

    @org.springframework.data.jpa.repository.Query("SELECT pi.imageurl FROM Productimage pi WHERE pi.productid.id = :productId ORDER BY pi.id ASC LIMIT 1")
    String findFirstImageUrlByProductId(@org.springframework.data.repository.query.Param("productId") Integer productId);
//
//    @org.springframework.data.jpa.repository.Query("SELECT pi FROM Productimage pi WHERE pi.productid = :product ORDER BY pi.id ASC")
//    List<Productimage> findAllByProductidOrderByIdAsc(@org.springframework.data.repository.query.Param("product") Product product);
//
//    @org.springframework.data.jpa.repository.Query("SELECT pi.id, pi.imageurl FROM Productimage pi WHERE pi.productid = :product ORDER BY pi.id ASC")
//    List<Object[]> findImageDataByProductid(@org.springframework.data.repository.query.Param("product") Product product);

    @org.springframework.data.jpa.repository.Query("SELECT pi.id, pi.imageurl FROM Productimage pi WHERE pi.productid.id = :productId ORDER BY pi.id ASC")
    List<Object[]> findImageDataByProductId(@org.springframework.data.repository.query.Param("productId") Integer productId);

    @Query(value = """
        SELECT
          p.imageid   AS imageId,
          p.productid AS productId,
          p.imageurl  AS imageUrl
        FROM productimages p
        WHERE p.embedding IS NOT NULL
        ORDER BY p.embedding <-> CAST(:vectorLiteral AS vector(512))
        LIMIT :limit
        """,
            nativeQuery = true
    )
    List<SimilarImageProjection> findTopKSimilarByLiteral(
            @Param("vectorLiteral") String vectorLiteral,
            @Param("limit") int limit
    );

    @Query(value = """
        SELECT COUNT(*) FROM productimages WHERE embedding IS NOT NULL
        """,
            nativeQuery = true
    )
    Long countImagesWithEmbedding();

    List<Productimage> findByEmbeddingIsNull();
}
