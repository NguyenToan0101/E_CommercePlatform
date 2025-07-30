//package org.example.ecommerce.repository;
//
//import org.example.ecommerce.entity.SearchHistory;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Modifying;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//@Repository
//public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Long> {
//
//    @Query("SELECT sh.keyword FROM SearchHistory sh WHERE sh.userId = :userId ORDER BY sh.searchedAt DESC")
//    List<String> findByUserId(@Param("userId") String userId, Pageable pageable);
//
//    @Modifying
//    @Transactional
//    @Query("DELETE FROM SearchHistory sh WHERE sh.userId = :userId")
//    void deleteAllByUserId(@Param("userId") String userId);
//
//    @Modifying
//    @Transactional
//    @Query("DELETE FROM SearchHistory sh WHERE sh.userId = :userId AND sh.keyword = :keyword")
//    void deleteByUserIdAndKeyword(@Param("userId") String userId, @Param("keyword") String keyword);
//}
