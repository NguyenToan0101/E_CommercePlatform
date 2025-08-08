package org.example.ecommerce.service;

import jakarta.transaction.Transactional;
import org.example.ecommerce.common.dto.promotion.PromotionDTO;
import org.example.ecommerce.common.exception.PromotionException;
import org.example.ecommerce.common.mapper.PromotionMapper;
import org.example.ecommerce.entity.Category;
import org.example.ecommerce.entity.Promotion;
import org.example.ecommerce.repository.CategoryRepository;
import org.example.ecommerce.repository.PromotionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class    PromotionService {
    private final PromotionRepository promotionRepository;
    private final PromotionMapper promotionMapper;
    private final CategoryRepository categoryRepository;
    private final Integer THOUSAND = 1000;

    public PromotionService(PromotionRepository promotionRepository, PromotionMapper promotionMapper, CategoryRepository categoryRepository) {
        this.promotionRepository = promotionRepository;

        this.promotionMapper = promotionMapper;
        this.categoryRepository = categoryRepository;

    }




    @Transactional
    public void save(PromotionDTO promotionDTO, String statusUpdateOrAdd) {
        System.out.println(promotionDTO.toString());

        if (promotionDTO.getName() == null || promotionDTO.getName().isEmpty()) {
            throw new PromotionException("Name cannot be empty");
        } else if (promotionDTO.getCode() == null || promotionDTO.getCode().isEmpty()) {
            throw new PromotionException("Code cannot be empty");
        } else if (promotionDTO.getType() == null || promotionDTO.getType().isEmpty()) {
            throw new PromotionException("Type cannot be empty");
        } else if (promotionDTO.getDescription() == null || promotionDTO.getDescription().isEmpty()) {
            throw new PromotionException("Description cannot be empty");
        } else if (promotionDTO.getValue() == null) {
            throw new PromotionException("Value cannot be empty");
        } else if (promotionDTO.getEnddate() == null ||
                promotionDTO.getEnddate().toString().isEmpty() ||
                promotionDTO.getEnddate().isBefore(LocalDateTime.now()) ||
                promotionDTO.getEnddate().isBefore(promotionDTO.getStartdate())) {
            throw new PromotionException("End date is invalid");
        } else if (promotionDTO.getStartdate() == null || promotionDTO.getStartdate().toString().isEmpty()) {
            throw new PromotionException("Start cannot be empty");
        } else if (promotionDTO.getUsageLimit() == null) {
            throw new PromotionException("Usage limit cannot be empty");
        } else if (promotionDTO.getMinOrderValue() == null) {
            throw new PromotionException("Min order value cannot be empty");
        } else if (promotionDTO.getMaxDiscount() == null) {
            throw new PromotionException("Max discount cannot be empty");
        } else if (promotionDTO.getCategories() == null || promotionDTO.getCategories().isEmpty()) {
            throw new PromotionException("Categories cannot be empty");
        } else if (promotionDTO.getPerUserLimit() == null) {
            throw new PromotionException("PerUserLimit cannot be empty");
        }

        // Kiểm tra loại khuyến mãi
        if (promotionDTO.getType().equalsIgnoreCase("percentage")) {
            if (promotionDTO.getValue() <= 0 || promotionDTO.getValue() >= 51) {
                throw new PromotionException("Value cannot less than 0% and more than 51%");
            }
            if (promotionDTO.getMaxDiscount() <= 0 || promotionDTO.getMaxDiscount() >= 10000 * THOUSAND) {
                throw new PromotionException("Max discount cannot less than 0 and more than 10.000.000VNĐ");
            }
        } else if (promotionDTO.getType().equalsIgnoreCase("fixed")) {
            if (promotionDTO.getValue() <= 0 || promotionDTO.getValue() >= 10000 * THOUSAND) {
                throw new PromotionException("Value cannot less than 0 and more than 10.000.000VNĐ");
            }

        } else if (promotionDTO.getType().equalsIgnoreCase("shipping")) {
            if (promotionDTO.getValue() <= 0 || promotionDTO.getValue() >= 200 * THOUSAND) {
                throw new PromotionException("Value cannot less than 0 and more than 200.000VNĐ");
            }

        }

        // Các kiểm tra giá trị số
        if (promotionDTO.getMinOrderValue() < 0) {
            throw new PromotionException("Min order value cannot less than 0");
        } else if (promotionDTO.getUsageLimit() <= 0) {
            throw new PromotionException("Usage limit cannot less than 0");
        } else if (promotionDTO.getPerUserLimit() <= 0 || promotionDTO.getPerUserLimit() >= 10) {
            throw new PromotionException("PerUserLimit cannot less than 0 and more than 10");
        }

        // Check ngày bắt đầu khi thêm mới
        if (statusUpdateOrAdd.equalsIgnoreCase("add") &&
                promotionDTO.getStartdate().isBefore(LocalDateTime.now())) {
            throw new PromotionException("Start date cannot be before now");
        }

        // Lưu entity
        Promotion promotion = promotionMapper.toEntity(promotionDTO);
        List<Category> category = promotion.getCategories().stream()
                .map(c -> categoryRepository.findById(c.getId())
                        .orElseThrow(() -> new PromotionException("Not found category")))
                .toList();
        promotion.setCategories(category);
        promotionRepository.save(promotion);
    }


    public void setStatusActive(Integer id,String status) {

        promotionRepository.findById(id).ifPresent(promotion -> {
            if(status.equalsIgnoreCase("SCHEDULED")){
                    promotion.setStartdate(LocalDateTime.now());
                promotion.setStatus("ACTIVE");
            } else if (status.equalsIgnoreCase("EXPIRED")) {
                    promotion.setEnddate(LocalDateTime.now().plusDays(1));
                promotion.setStatus("ACTIVE");
            } else if (status.equalsIgnoreCase("PAUSED")) {
                promotion.setStatus("ACTIVE");
            }

            promotionRepository.save(promotion);});
    }
    public void setStatusPaused(Integer id,String status){
        promotionRepository.findById(id).ifPresent(promotion -> {promotion.setStatus("PAUSED");
        promotionRepository.save(promotion);});
    }

    public List<PromotionDTO> getPromotions() {
        List<Promotion> promotions = promotionRepository.findAll();
        for (Promotion promotion : promotions) {
            if(!promotion.getStatus().equalsIgnoreCase("PAUSED")){
                if(promotion.getStartdate().isBefore(LocalDateTime.now())
//                        && promotion.getStatus().equalsIgnoreCase("SCHEDULED")
                        && promotion.getEnddate().isAfter(LocalDateTime.now())) {
                    promotion.setStatus("ACTIVE");
                    promotionRepository.save(promotion);
                } else if(promotion.getEnddate().isBefore(LocalDateTime.now())
//                        && promotion.getStatus().equalsIgnoreCase("ACTIVE")
                ) {
                    promotion.setStatus("EXPIRED");
                    promotionRepository.save(promotion);
                } else if (promotion.getStartdate().isAfter(LocalDateTime.now())) {
                    promotion.setStatus("SCHEDULED");
                    promotionRepository.save(promotion);
                }
            }
            if(!promotion.getType().equalsIgnoreCase("PERCENTAGE")){
                System.out.println("Value " + promotion.getValue().intValue());
                promotion.setMaxDiscount(promotion.getValue().intValue());
                promotionRepository.save(promotion);
            }
        }

        return  promotionMapper.toDTOs(promotions);
    }
    public void detelePromotion(Integer id){

            promotionRepository.deleteById(id);


    }
    public void templatePromotion(String title){
        Promotion promotion = new Promotion();
        Optional<Category> category= categoryRepository.findById(313);
        List<Category> categories = new ArrayList<>();
        category.ifPresent(categories::add);

        promotion.setStartdate(LocalDateTime.now());
        promotion.setEnddate(LocalDateTime.now().plusDays(1));
        switch (title){
            case "Flash Sale":{
                promotion.setCode("FLASHSALE");
                promotion.setDescription("Giảm giá sâu trong thời gian ngắn");

                promotion.setName("Flash Sale");
                promotion.setType("FIXED");
                promotion.setMinOrderValue(50 * THOUSAND);
                promotion.setUsageLimit(3 *THOUSAND);
                promotion.setPerUserLimit(2);
                promotion.setValue((double) (20 *THOUSAND));
                promotion.setStatus("ACTIVE");

                promotion.setCategories(categories);
                break;
            }

            case "Miễn phí vận chuyển":{
                promotion.setCode("FREESHIP");
                promotion.setDescription("Khuyến khích mua hàng với vận chuyển miễn phí");

                promotion.setName("Free Ship");
                promotion.setType("SHIPPING");
                promotion.setMinOrderValue(100 * THOUSAND);
                promotion.setUsageLimit(THOUSAND);
                promotion.setPerUserLimit(1);
                promotion.setValue((double) (40 *THOUSAND));
                promotion.setStatus("ACTIVE");
                promotion.setCategories(categories);
                break;
            }
            case "Mua 1 tặng 1":{
                promotion.setCode("BUY1GET1");
                promotion.setDescription("Tăng số lượng sản phẩm bán ra ");

                promotion.setName("Mua 1 tặng 1");
                promotion.setType("PERCENTAGE");
                promotion.setMinOrderValue(200 * THOUSAND);
                promotion.setUsageLimit(2 *THOUSAND);
                promotion.setPerUserLimit(1);
                promotion.setValue((double) (10));
                promotion.setStatus("ACTIVE");
                promotion.setMaxDiscount(50*THOUSAND);
                promotion.setCategories(categories);
                break;
            }
            case "Giảm giá sinh nhật" : {
                promotion.setCode("BIRTHDATE");
                promotion.setDescription("Chương trình đặc biệt cho khách hàng ");

                promotion.setName("Giảm giá sinh nhật");
                promotion.setType("PERCENTAGE");
                promotion.setMinOrderValue(100 * THOUSAND);
                promotion.setUsageLimit(10 *THOUSAND);
                promotion.setPerUserLimit(2);
                promotion.setValue((double) (5));
                promotion.setStatus("ACTIVE");
                promotion.setMaxDiscount(50*THOUSAND);
                promotion.setCategories(categories);
                break;
            }
            case "Khuyến mãi khách hàng mới":{
                promotion.setCode("NEWUSER");
                promotion.setDescription("Thu hút khách hàng lần đầu mua hàng ");

                promotion.setName("Giảm giá cho khách hàng mới");
                promotion.setType("FIXED");
                promotion.setMinOrderValue(0);
                promotion.setUsageLimit(Integer.MAX_VALUE);
                promotion.setPerUserLimit(1);
                promotion.setValue((double) (40*THOUSAND));
                promotion.setStatus("ACTIVE");
                promotion.setCategories(categories);
                break;
            }
            case "Combo giảm giá":{
                promotion.setCode("COMBOSALE");
                promotion.setDescription("Giảm giá khi mua nhiều sản phẩm ");

                promotion.setName("Combo giảm giá");
                promotion.setType("PERCENTAGE");
                promotion.setMinOrderValue(300 * THOUSAND);
                promotion.setUsageLimit(100 *THOUSAND);
                promotion.setPerUserLimit(1);
                promotion.setValue((double) (10));
                promotion.setStatus("ACTIVE");
                promotion.setMaxDiscount(50*THOUSAND);
                promotion.setCategories(categories);
                break;
            }
        }
        promotionRepository.save(promotion);
        promotionMapper.toDTO(promotion);
    }

    public Optional<Promotion> getPromotionById(int id) {
        return promotionRepository.findById(id);
    }
    public List<Promotion> getAllPromotions() {
        return promotionRepository.findAll();
    }
}
