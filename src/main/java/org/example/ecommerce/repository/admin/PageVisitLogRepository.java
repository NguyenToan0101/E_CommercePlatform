package org.example.ecommerce.repository.admin;

import org.example.ecommerce.entity.admin.PageVisitLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PageVisitLogRepository extends JpaRepository<PageVisitLog, Long> {
    @Query("SELECT COUNT(l) FROM PageVisitLog l WHERE l.path = '/home'")
    long countHomeVisits();

    @Query("SELECT COUNT(l) FROM PageVisitLog l WHERE l.path LIKE concat(:path, '%')")
    Integer countPageVisitByPath(@Param("path") String path);

    @Query("SELECT COUNT(l) FROM PageVisitLog l")
    Integer countAllPageVisitLog();


}

