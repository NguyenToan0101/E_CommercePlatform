package org.example.ecommerce.repository;

import org.example.ecommerce.entity.Order;
import org.example.ecommerce.entity.Orderitem;
import org.example.ecommerce.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;

public interface OrderItemsRepository extends JpaRepository<Orderitem, Integer> {
    List<Orderitem> findAllByOrderid(Order orderId);

    List<Orderitem> findByProductid(Product productid);

    // Dashboard method
    @Query(value = "SELECT p.name, COALESCE(pi.imageurl, '') as image, SUM(oi.quantity), COALESCE(SUM(oi.quantity * oi.unitprice), 0) " +
            "FROM orderitems oi " +
            "JOIN products p ON oi.productid = p.productid " +
            "LEFT JOIN (SELECT productid, MIN(imageurl) as imageurl FROM productimages GROUP BY productid) pi ON p.productid = pi.productid " +
            "JOIN orders o ON oi.orderid = o.orderid " +
            "WHERE p.shopid = :shopId AND o.status = 'Đã giao' " +
            "GROUP BY p.name, pi.imageurl ORDER BY SUM(oi.quantity) DESC " +
            "LIMIT :limit", nativeQuery = true)
    List<Object[]> findTopSellingProductsByShopId(@Param("shopId") Integer shopId, @Param("limit") int limit);
}
