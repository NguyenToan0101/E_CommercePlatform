package org.example.ecommerce.repository;

import org.example.ecommerce.common.dto.dashboard.RealTimeActivityDTO;
import org.example.ecommerce.entity.OrderFactLog;
import org.example.ecommerce.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
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

    @Query("SELECT COUNT (o.id) FROM Promotion o ")
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

    @Query("SELECT COUNT(o.promotionId) FROM OrderFactLog o " +
            "WHERE  o.customerId = :id AND o.promotionId = :promotionid ")
    Integer countPromotionUsedByCustomerid(@Param("id") Integer id, @Param("promotionid") Integer promotionid);
    //Dashboard admin----------

    @Query("SELECT SUM(o.unitPrice) FROM OrderFactLog  o " +
            "WHERE o.orderDate BETWEEN :start AND :end")
    BigDecimal sumRevenueByMonth(@Param("start") Instant start, @Param("end") Instant end);

    @Query(value = """
    SELECT
        CONCAT('Q', EXTRACT(QUARTER FROM o.order_date), ' ', EXTRACT(YEAR FROM o.order_date)) AS quarter,
        SUM(o.unit_price) AS revenue,
        COUNT(DISTINCT o.order_id) AS orders,
        ROUND(AVG(o.unit_price), 0) AS avg_order
    FROM order_fact_log o
    WHERE o.order_date >= date_trunc('quarter', now()) - INTERVAL '15 months'
    GROUP BY EXTRACT(YEAR FROM o.order_date), EXTRACT(QUARTER FROM o.order_date)
    ORDER BY EXTRACT(YEAR FROM o.order_date), EXTRACT(QUARTER FROM o.order_date)
    """, nativeQuery = true)
    List<Object[]> getQuarterRevenueRaw();

    //Realtime
//    @Query(value = """
//SELECT
//  tb.time,
//  COALESCE(pv.visits, 0) AS users,
//  COALESCE(ofl.orders, 0) AS orders,
//  COALESCE(ofl.revenue, 0) AS revenue
//FROM (
//  SELECT
//    TO_CHAR(gs, 'HH24:00') AS time
//  FROM generate_series(
//    date_trunc('day', CURRENT_DATE),                  -- Bắt đầu từ 00:00 hôm nay
//    DATE_TRUNC('hour', now())                         -- Kết thúc tại giờ hiện tại (làm tròn xuống)
//      - (EXTRACT(HOUR FROM now())::int % 2) * INTERVAL '1 hour',  -- Điều chỉnh về 2-hour slot
//    INTERVAL '2 hour'                                 -- Cách nhau 2 giờ
//  ) gs
//) tb
//LEFT JOIN (
//  SELECT
//    TO_CHAR(DATE_TRUNC('hour', p.created_at)
//      - (EXTRACT(HOUR FROM p.created_at)::int % 2) * INTERVAL '1 hour', 'HH24:00') AS time,
//    COUNT(*) AS visits
//  FROM page_visit_log p
//  WHERE p.created_at >= CURRENT_DATE AND p.path = '/home'
//  GROUP BY time
//) pv ON tb.time = pv.time
//LEFT JOIN (
//  SELECT
//    TO_CHAR(DATE_TRUNC('hour', o.order_date)
//      - (EXTRACT(HOUR FROM o.order_date)::int % 2) * INTERVAL '1 hour', 'HH24:00') AS time,
//    COUNT(DISTINCT o.order_id) AS orders,
//    COALESCE(SUM(o.unit_price), 0) AS revenue
//  FROM order_fact_log o
//  WHERE o.order_date >= CURRENT_DATE
//  GROUP BY time
//) ofl ON tb.time = ofl.time
//ORDER BY tb.time
//""", nativeQuery = true)
//    ArrayList<RealTimeActivityDTO> getRealTimeActivityData();
    @Query(value = """
SELECT
  tb.time,
  COALESCE(pv.visits, 0) AS users,
  COALESCE(ofl.orders, 0) AS orders,
  COALESCE(ofl.revenue, 0) AS revenue
FROM (
  SELECT 
    TO_CHAR(gs, 'HH24:00') AS time
  FROM generate_series(
    date_trunc('day', NOW() AT TIME ZONE 'Asia/Ho_Chi_Minh'),                  
    date_trunc('day', NOW() AT TIME ZONE 'Asia/Ho_Chi_Minh') + 
      (FLOOR(EXTRACT(HOUR FROM NOW() AT TIME ZONE 'Asia/Ho_Chi_Minh') / 2) * 2) * INTERVAL '1 hour',  
    INTERVAL '2 hour'                                 
  ) gs
) tb
LEFT JOIN (
  SELECT 
    TO_CHAR(
      DATE_TRUNC('hour', p.created_at AT TIME ZONE 'Asia/Ho_Chi_Minh')
      - (EXTRACT(HOUR FROM p.created_at AT TIME ZONE 'Asia/Ho_Chi_Minh')::int % 2) * INTERVAL '1 hour',
      'HH24:00'
    ) AS time,
    COUNT(*) AS visits
  FROM page_visit_log p
  WHERE p.created_at AT TIME ZONE 'Asia/Ho_Chi_Minh' >= date_trunc('day', NOW() AT TIME ZONE 'Asia/Ho_Chi_Minh') 
    AND p.path = '/home'
  GROUP BY time
) pv ON tb.time = pv.time
LEFT JOIN (
  SELECT 
    TO_CHAR(
      DATE_TRUNC('hour', o.order_date AT TIME ZONE 'Asia/Ho_Chi_Minh')
      - (EXTRACT(HOUR FROM o.order_date AT TIME ZONE 'Asia/Ho_Chi_Minh')::int % 2) * INTERVAL '1 hour',
      'HH24:00'
    ) AS time,
    COUNT(DISTINCT o.order_id) AS orders,
    COALESCE(SUM(o.unit_price), 0) AS revenue
  FROM order_fact_log o
  WHERE o.order_date AT TIME ZONE 'Asia/Ho_Chi_Minh' >= date_trunc('day', NOW() AT TIME ZONE 'Asia/Ho_Chi_Minh') 
  GROUP BY time
) ofl ON tb.time = ofl.time
ORDER BY tb.time
""", nativeQuery = true)
    ArrayList<RealTimeActivityDTO> getRealTimeActivityData();



    @Query(value = "SELECT TO_CHAR(NOW() AT TIME ZONE 'Asia/Ho_Chi_Minh', 'HH24:MI')", nativeQuery = true)
    String getCurrentHourMinute();
//Sum by region

    @Query("SELECT COALESCE(SUM(o.unitPrice), 0) FROM OrderFactLog o " +
            "WHERE LOWER(o.region) LIKE LOWER(CONCAT('%', :region, '%'))")
    BigDecimal sumRevenueByRegion(@Param("region") String region);

    @Query("SELECT count (o.id) FROM OrderFactLog o WHERE LOWER(o.region) LIKE LOWER(CONCAT('%', :region, '%'))")
    Integer countUserByRegion(@Param("region") String region);

    @Query("SELECT count (o.id) FROM OrderFactLog o ")
    Integer countAllUserByRegion();

    @Query("SELECT SUM(o.unitPrice) FROM OrderFactLog  o " )
    BigDecimal sumAllRevenueByRegion();

    @Query("SELECT count (o.id) FROM OrderFactLog  o " )
    Integer countAllOderByRegion();

    @Query("SELECT COUNT(c.gender) FROM Customer c WHERE c.gender = 'M' AND c.dateofbirth BETWEEN :start AND :end")
    Integer countMaleByAge(@Param("start") LocalDate start, @Param("end") LocalDate end);

    @Query("SELECT COUNT(c.gender) FROM Customer c WHERE c.gender = 'F' AND c.dateofbirth BETWEEN :start AND :end")
    Integer countFemaleByAge(@Param("start") LocalDate start,@Param("end") LocalDate end);

    @Query("SELECT COUNT(c.gender) FROM Customer c WHERE  c.dateofbirth BETWEEN :start AND :end")
    Integer countCustomerByAge(@Param("start") LocalDate start,@Param("end") LocalDate end);

    //TopProduct
    @Query("""
    SELECT p.name, SUM(o.unitPrice), c.categoryname AS totalRevenue
    FROM OrderFactLog o
    JOIN Product p ON p.id = o.productId
    JOIN Category c ON c.id = p.categoryid.id
    GROUP BY p.id, p.name, c.categoryname
    ORDER BY totalRevenue DESC
    """)
    List<Object[]> getTopSellingProductIds(Pageable pageable);


//    @Query("select p.name from  Product p  where p.id = :id ")
//    String getNameProductByProductId(@Param("id") int id);
//
//    @Query("select p.name from  Product p  where p.id = :id ")
//    String getCategoryProductByProductId(@Param("id") int id);


    //Statistics
    @Query(value = """
    SELECT 
        EXTRACT(YEAR FROM o.order_date) AS year,
        SUM(o.unit_price) AS revenue,
        SUM(o.profit) AS profit,
        SUM(o.cost) AS cost
    FROM order_fact_log o
    GROUP BY EXTRACT(YEAR FROM o.order_date)
    ORDER BY EXTRACT(YEAR FROM o.order_date)
""", nativeQuery = true)
    List<Object[]> getYearlyRevenueRaw();

    @Query(value = """
    SELECT 
        EXTRACT(MONTH FROM o.order_date) AS year,
        SUM(o.unit_price) AS revenue,
        SUM(o.profit) AS profit,
        SUM(o.cost) AS cost
    FROM order_fact_log o
    GROUP BY EXTRACT(MONTH FROM o.order_date)
    ORDER BY EXTRACT(MONTH FROM o.order_date)
""", nativeQuery = true)
    List<Object[]> getMonthlyRevenueRaw();


    @Query("SELECT COUNT(o.id) FROM Order o WHERE o.status = :status ")
    Integer countOrderStatus(@Param("status") String status);

    @Query("SELECT COUNT(o.id) FROM Order o")
    Integer countAllOrderStatus();

    @Query(value = """
SELECT
  CASE
    WHEN sub.hour BETWEEN 0 AND 3 THEN '00:00 - 04:00'
    WHEN sub.hour BETWEEN 4 AND 7 THEN '04:00 - 08:00'
    WHEN sub.hour BETWEEN 8 AND 11 THEN '08:00 - 12:00'
    WHEN sub.hour BETWEEN 12 AND 15 THEN '12:00 - 16:00'
    WHEN sub.hour BETWEEN 16 AND 19 THEN '16:00 - 20:00'
    ELSE '20:00 - 24:00'
  END AS time_range,
  COUNT(DISTINCT sub.order_id) AS orders
FROM (
  SELECT o.order_id,
         EXTRACT(HOUR FROM o.order_date AT TIME ZONE 'Asia/Ho_Chi_Minh') AS hour
  FROM order_fact_log o
  WHERE o.order_date AT TIME ZONE 'Asia/Ho_Chi_Minh' >= date_trunc('day', NOW() AT TIME ZONE 'Asia/Ho_Chi_Minh')
) AS sub
GROUP BY time_range
ORDER BY time_range
""", nativeQuery = true)
    List<Object[]> getOrderCountByTimeRange();


    @Query(value = """
    SELECT 
        c.categoryname AS category,
        COUNT(DISTINCT o.product_id) AS products,
        SUM(o.quantity) AS sales,
        SUM(o.unit_price) AS revenue
    FROM order_fact_log o
    JOIN categories c ON o.category_id = c.categoryid
    GROUP BY c.categoryname
    ORDER BY revenue DESC
""", nativeQuery = true)
    List<Object[]> getProductCategoryStats();

    @Query("SELECT count(r.id) FROM Review r WHERE r.rating = :star")
    Integer countRatingByStar(@Param("star") int star);

    @Query("SELECT count(r.id) FROM Review r ")
    Integer countAllRatingByStar();


    @Query("SELECT count(p.id) FROM PageVisitLog p WHERE p.device = :name ")
    Integer countDeviceByName(@Param("name") String name);

    @Query("SELECT count(p.id) FROM PageVisitLog p")
    Integer countAllDevice();

}


