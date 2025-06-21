package org.example.ecommerce.service.seller;

import org.example.ecommerce.entity.Shop;
import org.example.ecommerce.repository.seller.SellerRepo;
import org.example.ecommerce.repository.seller.ShopRepo;
import org.springframework.stereotype.Service;

@Service
public class ShopService {
    protected ShopRepo shopRepo;
    public ShopService(ShopRepo shopRepo) {
        this.shopRepo = shopRepo;
    }

    public Shop getShopById(int id) {
        return shopRepo.findById(id).orElse(new Shop());
    }
}
