package org.example.ecommerce.repository;

import org.example.ecommerce.entity.Customer;
import org.example.ecommerce.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrdersRepository extends JpaRepository<Order, Integer> {
    List<Order> findAllByCustomeridOrderByOrderdateDesc(Customer customer);

    @Query("SELECT DISTINCT o FROM Order o JOIN o.orderitems oi JOIN oi.productid p WHERE p.shopid.id = :shopId AND (:status IS NULL OR o.status = :status) ORDER BY o.orderdate DESC")
    List<Order> findAllByShopIdAndStatus(@Param("shopId") Integer shopId, @Param("status") String status);

    @Query("SELECT o.status, COUNT(o) FROM Order o JOIN o.orderitems oi JOIN oi.productid p WHERE p.shopid.id = :shopId GROUP BY o.status")
    List<Object[]> countOrdersByStatusForShop(@Param("shopId") Integer shopId);
}
