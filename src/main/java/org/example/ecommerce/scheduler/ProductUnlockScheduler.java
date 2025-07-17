package org.example.ecommerce.scheduler;

import org.example.ecommerce.entity.Product;
import org.example.ecommerce.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Instant;
import java.util.List;

public class ProductUnlockScheduler {
    @Autowired
    private ProductRepository productRepository;


    @Scheduled(fixedRate = 60_000)
    public void unlockExpiredProducts() {
        Instant now = Instant.now();
        List<Product> expired = productRepository.findByStatusAndLockedUntilBefore("locked", now);

        for (Product p : expired) {
            p.setStatus("available");
            p.setLockedUntil(null);
        }

        if (!expired.isEmpty()) {
            productRepository.saveAll(expired);
        }
    }
}
