package org.example.ecommerce.scheduler;

import org.example.ecommerce.entity.Shop;
import org.example.ecommerce.repository.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ShopUnlockScheduler {
    @Autowired
    private ShopRepository shopRepository;

    @Scheduled(fixedRate = 60000) // mỗi phút
    public void unlockExpiredShops() {
        List<Shop> expired = shopRepository.findByLockedTrueAndLockedUntilBefore(LocalDateTime.now());
        for (Shop s : expired) {
            s.setLocked(false);
            s.setLockedUntil(null);
            s.setStatus(Shop.Status.ACTIVE.name());
        }
        shopRepository.saveAll(expired);
    }
}
