package org.example.ecommerce.service.seller;

import jakarta.persistence.EntityNotFoundException;
import org.example.ecommerce.repository.seller.SellerRepo;
import org.example.ecommerce.repository.seller.ShopRepo;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class ShopService {
    protected ShopRepo shopRepo;
    @Autowired
    private SellerRepo sellerRepo;
    public ShopService(ShopRepo shopRepo) {
        this.shopRepo = shopRepo;
    }


}
