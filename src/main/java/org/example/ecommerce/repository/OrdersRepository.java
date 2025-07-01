package org.example.ecommerce.repository;

import org.example.ecommerce.entity.Customer;
import org.example.ecommerce.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface OrdersRepository extends JpaRepository<Order, Integer> {

    int countByCustomerid(Customer customer);

    @Query("SELECT COALESCE(SUM(o.totalamount), 0) FROM Order o WHERE o.customerid = :customer")
    BigDecimal sumTotalAmountByCustomerid(Customer customer);
}
