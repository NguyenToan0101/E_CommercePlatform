package org.example.ecommerce.repository;

import org.example.ecommerce.common.dto.admin.userManagement.UserDTO;
import org.example.ecommerce.entity.Customer;
import org.example.ecommerce.entity.Inventory;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    Customer findByEmail(String email);

    @EntityGraph(attributePaths = {"seller", "seller.shop"})
    @Query("SELECT c FROM Customer c")
    List<Customer> findAllWithSellerAndShop();

    List<Customer> findByIsLockedTrueAndLockedUntilBefore(LocalDateTime now);


    @Query("""
SELECT new org.example.ecommerce.common.dto.admin.userManagement.UserDTO(
    c.id,
    CONCAT(c.firstname, ' ', c.lastname),
    c.email,
    c.phone,
    c.role,
    c.status,
    c.createdat,
    (SELECT COUNT(o) FROM Order o WHERE o.customerid.id = c.id),
    (SELECT COALESCE(SUM(o.totalamount), 0) FROM Order o WHERE o.customerid.id = c.id),
    c.gender,
    c.dateofbirth,
    c.address,
    s.idnumber,
    sh.shopname,
    sh.fulladdress,
    sh.businessType,
    sh.taxCode,
    sh.phone,
    sh.invoiceEmail,
    sh.manageName,
    sh.status,
    sh.express,
    sh.fast,
    sh.economy,
    sh.lockerDelivery,
    sh.bulkyItems
)
FROM Customer c
LEFT JOIN c.seller s
LEFT JOIN s.shop sh
""")
    List<UserDTO> getAllUserDTOs();


    @Query("SELECT count(c.id) FROM Customer c")
    Integer countCustomer();
}
