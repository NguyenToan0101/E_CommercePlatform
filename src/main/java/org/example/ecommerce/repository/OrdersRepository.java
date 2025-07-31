package org.example.ecommerce.repository;

import org.example.ecommerce.entity.Customer;
import org.example.ecommerce.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import java.math.BigDecimal;

public interface OrdersRepository extends JpaRepository<Order, Integer> {
    List<Order> findAllByCustomeridOrderByOrderdateDesc(Customer customer);

    @Query("SELECT DISTINCT o FROM Order o JOIN o.orderitems oi JOIN oi.productid p WHERE p.shopid.id = :shopId AND (:status IS NULL OR o.status = :status) ORDER BY o.orderdate DESC")
    List<Order> findAllByShopIdAndStatus(@Param("shopId") Integer shopId, @Param("status") String status);


    @Query("SELECT o.status, COUNT(o) FROM Order o JOIN o.orderitems oi JOIN oi.productid p WHERE p.shopid.id = :shopId GROUP BY o.status")
    List<Object[]> countOrdersByStatusForShop(@Param("shopId") Integer shopId);




        @Query("SELECT o FROM Order o WHERE " +
                "(:search IS NULL OR LOWER(o.fullname) LIKE LOWER(CONCAT('%', :search, '%')) " +
                "OR LOWER(o.phone) LIKE LOWER(CONCAT('%', :search, '%')) " +
                "OR CAST(o.id AS string) LIKE CONCAT('%', :search, '%')) " +
                "AND (:status IS NULL OR o.status = :status)")
        List<Order> searchByKeywordAndStatus(@Param("search") String search,
                                             @Param("status") String status);

    @Query("SELECT COALESCE(SUM(o.totalamount), 0) FROM Order o " +
            "WHERE LOWER(o.address) LIKE LOWER(CONCAT('%', :address, '%') ) AND o.status = 'Đã giao'")
    BigDecimal sumRevenueByAddress(@Param("address") String address);

    @Query("SELECT COALESCE(COUNT (o.id), 0) FROM Order o " +
            "WHERE LOWER(o.address) LIKE LOWER(CONCAT('%', :address, '%')) AND o.status = 'Đã giao'")
    Integer countOrderByAddress(@Param("address") String address);

    @Query("SELECT COALESCE(SUM(o.totalamount), 0) FROM Order o " )
    BigDecimal sumDifferenceRevenueByAddress();

    @Query("SELECT COALESCE(COUNT (o.id), 0) FROM Order o " )
    Integer countDifferenceOrderByAddress();



    int countByCustomerid(Customer customer);

    @Query("SELECT COALESCE(SUM(o.totalamount), 0) FROM Order o WHERE o.customerid = :customer")
    BigDecimal sumTotalAmountByCustomerid(Customer customer);

    Order findOrderById(Integer id);

    // Dashboard methods
    @Query("SELECT COALESCE(SUM(o.totalamount), 0) FROM Order o " +
            "WHERE o.id IN (SELECT DISTINCT o2.id FROM Order o2 " +
            "JOIN o2.orderitems oi JOIN oi.productid p " +
            "WHERE p.shopid.id = :shopId) AND o.status = 'Đã giao'")
    BigDecimal sumRevenueByShopId(@Param("shopId") Integer shopId);

    @Query("SELECT COALESCE(COUNT(o.id), 0) FROM Order o " +
            "JOIN o.orderitems oi JOIN oi.productid p WHERE p.shopid.id = :shopId")
    Long countOrdersByShopId(@Param("shopId") Integer shopId);

    @Query("SELECT COALESCE(COUNT(DISTINCT o.customerid.id), 0) FROM Order o " +
            "JOIN o.orderitems oi JOIN oi.productid p WHERE p.shopid.id = :shopId")
    Long countDistinctCustomersByShopId(@Param("shopId") Integer shopId);

    @Query("SELECT COALESCE(SUM(o.totalamount), 0) FROM Order o " +
            "WHERE o.id IN (SELECT DISTINCT o2.id FROM Order o2 " +
            "JOIN o2.orderitems oi JOIN oi.productid p " +
            "WHERE p.shopid.id = :shopId) " +
            "AND EXTRACT(YEAR FROM o.orderdate) = :year AND EXTRACT(MONTH FROM o.orderdate) = :month " +
            "AND o.status = 'Đã giao'")
    BigDecimal sumRevenueByShopIdAndMonth(@Param("shopId") Integer shopId, @Param("year") int year, @Param("month") int month);

    @Query("SELECT c.categoryname, COALESCE(SUM(o.totalamount), 0) FROM Order o " +
            "JOIN o.orderitems oi JOIN oi.productid p JOIN p.categoryid c " +
            "WHERE p.shopid.id = :shopId AND o.status = 'Đã giao' " +
            "AND o.id IN (SELECT DISTINCT o2.id FROM Order o2 " +
            "JOIN o2.orderitems oi2 JOIN oi2.productid p2 " +
            "WHERE p2.shopid.id = :shopId) " +
            "GROUP BY c.categoryname ORDER BY SUM(o.totalamount) DESC")
    List<Object[]> sumRevenueByCategoryAndShopId(@Param("shopId") Integer shopId);

    @Query(value = "SELECT o.* FROM orders o " +
            "JOIN orderitems oi ON o.orderid = oi.orderid " +
            "JOIN products p ON oi.productid = p.productid " +
            "WHERE p.shopid = :shopId " +
            "ORDER BY o.orderdate DESC " +
            "LIMIT :limit", nativeQuery = true)
    List<Order> findRecentOrdersByShopId(@Param("shopId") Integer shopId, @Param("limit") int limit);
}
