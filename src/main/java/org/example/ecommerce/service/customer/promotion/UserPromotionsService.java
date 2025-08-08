package org.example.ecommerce.service.customer.promotion;

import org.example.ecommerce.common.dto.promotion.PromotionDTO;
import org.example.ecommerce.common.mapper.PromotionMapper;
import org.example.ecommerce.entity.Customer;
import org.example.ecommerce.entity.Promotion;
import org.example.ecommerce.entity.Userpromotion;
import org.example.ecommerce.repository.PromotionRepository;
import org.example.ecommerce.repository.UserPromotionsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserPromotionsService {

    @Autowired
    private PromotionRepository promotionRepository;

    @Autowired
    private UserPromotionsRepository userPromotionsRepository;

    @Autowired
    private PromotionMapper promotionMapper;


    public List<PromotionDTO> getAvailablePromotions(Customer customer) {
        LocalDateTime now = LocalDateTime.now();
        
        // Lấy tất cả promotions đang hoạt động
        List<Promotion> allPromotions = promotionRepository.findAll();
        
        // Lọc promotions theo điều kiện
        List<Promotion> availablePromotions = allPromotions.stream()
            .filter(promotion -> {
                // Kiểm tra status
                if (!"ACTIVE".equalsIgnoreCase(promotion.getStatus())) {
                    return false;
                }
                
                // Kiểm tra thời gian
                if (promotion.getStartdate().isAfter(now) || promotion.getEnddate().isBefore(now)) {
                    return false;
                }
                
                // Kiểm tra usage limit
                if (promotion.getUsageLimit() != null && 
                    promotion.getUsageCount() != null &&
                    promotion.getUsageCount() >= promotion.getUsageLimit()) {
                    return false;
                }
                
                return true;
            })
            .collect(Collectors.toList());
        
        return promotionMapper.toDTOs(availablePromotions);
    }


    public List<PromotionDTO> getUsedPromotions(Customer customer) {
        List<Userpromotion> userPromotions = userPromotionsRepository.findByCustomerid(customer);
        
        List<Promotion> usedPromotions = userPromotions.stream()
            .map(Userpromotion::getPromotionid)
            .collect(Collectors.toList());
        
        return promotionMapper.toDTOs(usedPromotions);
    }


    public boolean canUsePromotion(Customer customer, Integer promotionId) {
        // Kiểm tra xem user đã sử dụng promotion này chưa
        List<Userpromotion> userPromotions = userPromotionsRepository.findByCustomeridAndPromotionid_Id(customer, promotionId);

        if (userPromotions.isEmpty()) {
            return true;
        }

        // Lấy promotion để kiểm tra perUserLimit
        Promotion promotion = promotionRepository.findById(promotionId).orElse(null);
        if (promotion == null) {
            return false;
        }

        // Kiểm tra số lần sử dụng của user
        return userPromotions.size() < promotion.getPerUserLimit();
    }


    public int getUsageCountByUser(Customer customer, Integer promotionId) {
        List<Userpromotion> userPromotions = userPromotionsRepository.findByCustomeridAndPromotionid_Id(customer, promotionId);
        return userPromotions.size();
    }


    public PromotionDTO getPromotionById(Integer promotionId) {
        Promotion promotion = promotionRepository.findById(promotionId).orElse(null);
        if (promotion == null) {
            return null;
        }
        return promotionMapper.toDTO(promotion);
    }




        public boolean saveUserPromotion(Customer customer, Integer promotionId) {
            try {
                // Kiểm tra promotion có tồn tại không
                Promotion promotion = promotionRepository.findById(promotionId).orElse(null);
                if (promotion == null) {
                    return false;
                }

                // Kiểm tra xem user đã lưu promotion này chưa
                List<Userpromotion> existingUserPromotions = userPromotionsRepository.findByCustomeridAndPromotionid_Id(customer, promotionId);
                if (!existingUserPromotions.isEmpty()) {
                    return false; // Đã lưu rồi
                }

                // Tạo Userpromotion mới
                Userpromotion userPromotion = new Userpromotion();
                userPromotion.setCustomerid(customer);
                userPromotion.setPromotionid(promotion);
                userPromotion.setUsedat(Instant.now());

                // Lưu vào database
                userPromotionsRepository.save(userPromotion);

                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    } 