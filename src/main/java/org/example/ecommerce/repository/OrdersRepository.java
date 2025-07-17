package org.example.ecommerce.repository;

import org.example.ecommerce.entity.Customer;
import org.example.ecommerce.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
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
}
