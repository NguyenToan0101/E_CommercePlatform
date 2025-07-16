package org.example.ecommerce.repository;

import org.example.ecommerce.entity.OrderFactLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface OderFactLogRepo extends JpaRepository<OrderFactLog,Integer> {
    @Query("SELECT SUM(o.unitPrice) FROM OrderFactLog o WHERE o.promotionId IS NOT NULL")
    BigDecimal sumRevenuePromotion();

    @Query("SELECT COUNT (o.id) FROM OrderFactLog o WHERE o.promotionId IS NOT NULL")
    Integer countOrderPromotion();

    @Query("SELECT COUNT (o.id) FROM OrderFactLog o WHERE o.promotionId IS NOT NULL")
    Integer countOrderFactLog();

    @Query("SELECT SUM(o.unitPrice) FROM OrderFactLog o " +
            "WHERE o.promotionId IS NOT NULL AND o.orderDate BETWEEN :start AND :end")
    BigDecimal sumRevenuePromotionByMonth(@Param("start") Instant start, @Param("end") Instant end);

    @Query("SELECT count (o.id) FROM OrderFactLog o " +
            "WHERE o.promotionId IS NOT NULL AND o.orderDate BETWEEN :start AND :end")
    Integer countOrderPromotionByMonth(@Param("start") Instant start, @Param("end") Instant end);

    @Query("SELECT SUM(o.discountAmount) FROM OrderFactLog o " +
            "WHERE o.promotionId IS NOT NULL AND o.orderDate BETWEEN :start AND :end")
    BigDecimal sumDiscountAmountPromotionByMonth(@Param("start") Instant start, @Param("end") Instant end);
    @Query("SELECT COUNT (o.id) FROM Promotion o " +
            "WHERE  o.type LIKE CONCAT(:type, '%') ")
    Float getPercentageDiscountType(@Param("type") String type);

    @Query("SELECT COUNT (o.id) FROM Promotion o "
            )
    Float getTotalPromotion();

    @Query("SELECT count (o.id) FROM OrderFactLog o " +
            "WHERE  o.promotionId = :id ")
    Integer countOrderPromotionById(@Param("id") Integer id);

    @Query("SELECT SUM(o.unitPrice) FROM OrderFactLog o " +
            "WHERE  o.promotionId = :id ")
    BigDecimal sumRevenuePromotionByID(@Param("id") Integer id);


    List<OrderFactLog> findByPromotionId(Integer integer);

    @Query("SELECT SUM(o.profit) FROM OrderFactLog o " +
            "WHERE  o.promotionId = :id ")
    BigDecimal sumProfitPromotionByID(@Param("id") Integer id);

    @Query("SELECT SUM(o.discountAmount) FROM OrderFactLog o " +
            "WHERE  o.promotionId = :id ")
    BigDecimal sumDiscountPromotionByID(@Param("id") Integer id);
}


