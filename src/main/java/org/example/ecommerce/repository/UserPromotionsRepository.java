package org.example.ecommerce.repository;

import org.example.ecommerce.entity.Customer;
import org.example.ecommerce.entity.Promotion;
import org.example.ecommerce.entity.Userpromotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserPromotionsRepository extends JpaRepository<Userpromotion,Integer> {
    
    /**
     * Tìm tất cả userpromotions theo customer
     */
    List<Userpromotion> findByCustomerid(Customer customer);
    
    /**
     * Tìm userpromotions theo customer và promotion
     */
    List<Userpromotion> findByCustomeridAndPromotionid_Id(Customer customer, Integer promotionId);
    
    /**
     * Đếm số lần customer đã sử dụng promotion
     */
    @Query("SELECT COUNT(up) FROM Userpromotion up WHERE up.customerid = :customer AND up.promotionid.id = :promotionId")
    long countByCustomerAndPromotion(@Param("customer") Customer customer, @Param("promotionId") Integer promotionId);
    
    /**
     * Kiểm tra xem customer đã sử dụng promotion chưa
     */
    boolean existsByCustomeridAndPromotionid_Id(Customer customer, Integer promotionId);
}
