package org.example.ecommerce.repository;

import org.example.ecommerce.entity.Promotion;
import org.example.ecommerce.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Integer> {
    
    /**
     * Tìm kiếm khuyến mãi theo shop
     * @param shop Đối tượng shop
     * @return Danh sách khuyến mãi của shop
     */
    List<Promotion> findByShop(Shop shop);
    
    /**
     * Tìm kiếm khuyến mãi theo mã và shop
     * @param code Mã khuyến mãi
     * @param shop Shop
     * @return Khuyến mãi nếu tìm thấy
     */
    Optional<Promotion> findByCodeAndShop(String code, Shop shop);
    
    /**
     * Đếm số lượng khuyến mãi đang hoạt động của shop
     * @param shop Shop
     * @param currentTime Thời gian hiện tại
     * @return Số lượng khuyến mãi đang hoạt động
     */
    @Query("SELECT COUNT(p) FROM Promotion p WHERE p.shop = :shop " +
           "AND p.startdate <= :currentTime AND p.enddate >= :currentTime AND p.status = 'ACTIVE'")
    long countActivePromotionsByShop(@Param("shop") Shop shop, 
                                   @Param("currentTime") LocalDateTime currentTime);
}
