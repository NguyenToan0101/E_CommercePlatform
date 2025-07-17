package org.example.ecommerce.service.seller;

import org.example.ecommerce.entity.Seller;
import org.example.ecommerce.repository.seller.SellerRepo;
import org.springframework.stereotype.Service;

@Service
public class SellerService {
    private final SellerRepo sellerRepo;
    public SellerService(SellerRepo sellerRepo) {
        this.sellerRepo = sellerRepo;
    }
    public void saveSeller(Seller seller) {

        sellerRepo.save(seller);


    }
    public Seller getSellerById(int id) {
        return sellerRepo.findById(id).orElse(new Seller());
    }
}