package org.example.ecommerce.repository;

import org.example.ecommerce.common.dto.shopManagement.ShopDTO;
import org.example.ecommerce.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

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




}
