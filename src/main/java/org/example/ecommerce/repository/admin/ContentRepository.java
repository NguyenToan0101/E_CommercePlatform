package org.example.ecommerce.repository.admin;

import jakarta.transaction.Transactional;
import org.example.ecommerce.common.dto.catalogAndContent.ContentListDTO;
import org.example.ecommerce.entity.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ContentRepository extends JpaRepository<Content, Integer> {
    @Query("""
  SELECT new org.example.ecommerce.common.dto.catalogAndContent.ContentListDTO(
    c.contentid,
    c.title,
    c.slug,
    c.type,
    c.status,
    COALESCE(a.fullname, 'Unknown'),
    c.updatedAt
  )
  FROM Content c
  LEFT JOIN c.updatedBy a
  WHERE (:status IS NULL OR :status = 'all' OR LOWER(c.status) = LOWER(:status))
    AND (:keyword IS NULL OR LOWER(c.title) LIKE LOWER(CONCAT('%', :keyword, '%')))
  ORDER BY c.updatedAt DESC
""")
    List<ContentListDTO> findFilteredContentList(@Param("status") String status, @Param("keyword") String keyword);


    //delete
    @Modifying
    @Transactional
    @Query("DELETE FROM Content c WHERE c.contentid = :id")
    void deleteByContentid(@Param("id") Integer id);

    //published
    @Modifying
    @Transactional
    @Query("""
        UPDATE Content c
        SET c.status = :status,
            c.updatedAt = :updatedAt
        WHERE c.contentid = :id
    """)
    int updateStatus(
            @Param("id") Integer id,
            @Param("status") String status,
            @Param("updatedAt") LocalDateTime updatedAt
    );

    //find by type
    List<Content> findByTypeIn(List<String> types);
}
