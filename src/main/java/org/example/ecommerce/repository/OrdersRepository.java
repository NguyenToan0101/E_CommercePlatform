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




        @Query("SELECT o FROM Order o WHERE " +
                "(:search IS NULL OR LOWER(o.fullname) LIKE LOWER(CONCAT('%', :search, '%')) " +
                "OR LOWER(o.phone) LIKE LOWER(CONCAT('%', :search, '%')) " +
                "OR CAST(o.id AS string) LIKE CONCAT('%', :search, '%')) " +
                "AND (:status IS NULL OR o.status = :status)")
        List<Order> searchByKeywordAndStatus(@Param("search") String search,
                                             @Param("status") String status);



    int countByCustomerid(Customer customer);

    @Query("SELECT COALESCE(SUM(o.totalamount), 0) FROM Order o WHERE o.customerid = :customer")
    BigDecimal sumTotalAmountByCustomerid(Customer customer);

    Order findOrderById(Integer id);
}
