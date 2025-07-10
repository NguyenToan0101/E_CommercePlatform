package org.example.ecommerce.service.admin;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.example.ecommerce.common.dto.shopManagement.ShopDTO;
import org.example.ecommerce.common.dto.shopManagement.ShopDetailDTO;
import org.example.ecommerce.entity.Shop;
import org.example.ecommerce.repository.ShopRepository;
import org.example.ecommerce.service.customer.cusromer_aut.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.example.ecommerce.entity.Shop.Status;

import java.util.List;

@Service
public class AdminShopService {

    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private EmailService emailService;

    public List<ShopDTO> getAllShops() {
        return shopRepository.getAllShopsAsDTO();
    }

    public ShopDetailDTO getShopDetail(Integer shopId) {
        return shopRepository.getShopDetailById(shopId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy shop id = " + shopId));
    }

    @Transactional
    public void approveShop(Integer shopId) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new EntityNotFoundException("Shop not found: " + shopId));

        if (!shop.getStatus().equals(Status.PENDING_APPROVAL.name())) {
            throw new IllegalStateException(
                    "Shop #" + shopId + " không ở trạng thái PENDING_APPROVAL, hiện là " + shop.getStatus()
            );
        }

        // gán string representation của enum
        shop.setStatus(Status.ACTIVE.name());
        shopRepository.save(shop);
    }


    //Khoa
    @Transactional
    public void lockShop(Integer shopId) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new EntityNotFoundException("Shop not found: " + shopId));

        shop.setStatus(Status.LOCK.name());
        shopRepository.save(shop);
    }

    //tu choi shop
    @Transactional
    public void rejectShop(Integer shopId) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new EntityNotFoundException("Shop not found: " + shopId));

        if (!shop.getStatus().equals(Shop.Status.PENDING_APPROVAL.name())) {
            throw new IllegalStateException(
                    "Shop #" + shopId + " không ở trạng thái PENDING_APPROVAL, hiện là " + shop.getStatus()
            );
        }

        shop.setStatus(Shop.Status.REJECTED.name());
        shopRepository.save(shop);
    }

    //mo khoa
    @Transactional
    public void unlockShop(Integer shopId) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new EntityNotFoundException("Shop not found: " + shopId));

        if (!Boolean.TRUE.equals(shop.getLocked())) {
            throw new IllegalStateException("Shop #" + shopId + " không ở trạng thái locked");
        }

        shop.setLocked(false);
        shop.setLockedUntil(null);
        shop.setStatus(Shop.Status.ACTIVE.name());
        shopRepository.save(shop);

        // nếu muốn gửi email
//        emailService.sendAccountUnlockedEmail(shop.getInvoiceEmail());
    }

}
