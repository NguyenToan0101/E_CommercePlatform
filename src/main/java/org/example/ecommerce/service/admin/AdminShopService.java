package org.example.ecommerce.service.admin;

import org.example.ecommerce.common.dto.shopManagement.ShopDTO;
import org.example.ecommerce.repository.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminShopService {

    @Autowired
    private ShopRepository shopRepository;

    public List<ShopDTO> getAllShops() {
        return shopRepository.getAllShopsAsDTO();
    }

}
