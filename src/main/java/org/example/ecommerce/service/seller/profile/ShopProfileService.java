package org.example.ecommerce.service.seller.profile;

import org.example.ecommerce.entity.Shop;
import org.example.ecommerce.repository.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.example.ecommerce.service.UploadImageFile;
import java.io.IOException;

@Service
public class ShopProfileService {
    @Autowired
    private ShopRepository shopRepository;

    @Autowired
    private UploadImageFile uploadImageFile;

    public Shop getShopBySeller(Integer sellerId) {
        return shopRepository.findBySellerid_Id(sellerId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy shop!"));
    }

    public void updateShopProfile(Integer sellerId, Shop updatedShop, MultipartFile imageFile) throws IOException {
        Shop shop = getShopBySeller(sellerId);
        shop.setShopname(updatedShop.getShopname());
        shop.setDescription(updatedShop.getDescription());
        shop.setFulladdress(updatedShop.getFulladdress());
        shop.setPhone(updatedShop.getPhone());
        shop.setBusinessType(updatedShop.getBusinessType());
        shop.setInvoiceEmail(updatedShop.getInvoiceEmail());
        if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = uploadImageFile.uploadImage(imageFile);
            shop.setImageshop(imageUrl);
        }
        // ... các trường khác nếu cần
        shopRepository.save(shop);
    }
} 