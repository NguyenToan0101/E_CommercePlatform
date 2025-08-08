package org.example.ecommerce.repository;

import org.example.ecommerce.entity.CustomerRank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRankRepository extends JpaRepository<CustomerRank, Integer> {
    
    Optional<CustomerRank> findByCustomerId(Integer customerId);
    
    @Query("SELECT cr FROM CustomerRank cr ORDER BY cr.points DESC")
    List<CustomerRank> findAllOrderByPointsDesc();
    
    @Query("SELECT cr FROM CustomerRank cr WHERE cr.rankType = ?1 ORDER BY cr.points DESC")
    List<CustomerRank> findByRankTypeOrderByPointsDesc(String rankType);
}
