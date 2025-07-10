package org.example.ecommerce.repository;

import org.example.ecommerce.common.dto.shopManagement.ShopDTO;
import org.example.ecommerce.common.dto.shopManagement.ShopDetailDTO;
import org.example.ecommerce.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
        s.createdat
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
    CAST(s.createdat AS java.lang.String),
    s.status,
    s.invoiceEmail,
    s.phone,
    s.businessType,
    s.description,
    s.imageshop,
    s.taxCode,
    c.categoryname,
    cu.email,
    CAST(cu.gender AS java.lang.String),
    CAST(cu.dateofbirth AS java.lang.String),
    cu.address,
    se.idNumber,
    se.frontIdImage,
    se.backIdImage
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
}
