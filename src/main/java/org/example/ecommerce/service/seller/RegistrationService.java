package org.example.ecommerce.service.seller;

import org.example.ecommerce.entity.Shop;
import org.example.ecommerce.repository.seller.RegistrationRepo;
import org.springframework.stereotype.Service;


@Service
public class RegistrationService  {
    private final RegistrationRepo repo;
    public RegistrationService(RegistrationRepo repo) {
        this.repo = repo;
    }
    public void registrationShop(Shop shop) {

            repo.save(shop);


    }
}
