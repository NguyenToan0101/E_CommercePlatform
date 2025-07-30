package org.example.ecommerce.repository;

import org.example.ecommerce.common.dto.shopManagement.ShopDTO;
import org.example.ecommerce.common.dto.shopManagement.ShopDetailDTO;
import org.example.ecommerce.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface ShopRepository extends JpaRepository<Shop, Integer> {

    @Query("""
    SELECT new org.example.ecommerce.common.dto.shopManagement.ShopDTO(
        s.id,
        s.shopname,
        s.manageName,
        s.businessAddress,
        s.status,
        s.invoiceEmail,
        s.phone,
        s.businessType,
        s.createdat,
        s.locked,
        s.lockedUntil
    )
    FROM Shop s
    """)
    List<ShopDTO> getAllShopsAsDTO();


    @Query("""
SELECT new org.example.ecommerce.common.dto.shopManagement.ShopDetailDTO(
    s.id,
    s.shopname,
    s.manageName,
    s.businessAddress,
    s.createdat,
    s.status,
    s.invoiceEmail,
    s.phone,
    s.businessType,
    s.description,
    s.imageshop,
    s.taxCode,
    c.categoryname,
    cu.email,
    cu.gender,
    cu.dateofbirth,
    cu.address,
    se.idnumber,
    se.frontidimage,
    se.backidimage,
    s.locked,
    s.lockedUntil
)
FROM Shop s
JOIN s.sellerid se
JOIN se.customer cu
LEFT JOIN s.maincategoryid c
WHERE s.id = :shopId
""")
    Optional<ShopDetailDTO> getShopDetailById(@Param("shopId") Integer shopId);


    //mo khoa
    List<Shop> findByLockedTrueAndLockedUntilBefore(LocalDateTime now);

    Shop findShopsById(Integer shopid);
}
