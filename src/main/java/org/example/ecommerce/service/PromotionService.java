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

import java.util.List;


@Service
public class PromotionService {
    private final PromotionRepository promotionRepository;
    private final PromotionMapper promotionMapper;
    private final CategoryRepository categoryRepository;
    public PromotionService(PromotionRepository promotionRepository, PromotionMapper promotionMapper, CategoryRepository categoryRepository) {
        this.promotionRepository = promotionRepository;

        this.promotionMapper = promotionMapper;
        this.categoryRepository = categoryRepository;
    }


    public List<PromotionDTO> getPromotions() {

        return promotionMapper.toDTOs(promotionRepository.findAll());
    }

    @Transactional
    public void save(PromotionDTO promotionDTO,String statusUpdateOrAdd) {
        System.out.println(promotionDTO.toString());
            if (promotionDTO.getName() == null) {
                throw new PromotionException("Name cannot be empty");
            } else if (promotionDTO.getCode().isEmpty()) {
                throw new PromotionException("Code cannot be empty");
            } else if (promotionDTO.getType().isEmpty()) {
                throw new PromotionException("Type cannot be empty");
            } else if (promotionDTO.getDescription().isEmpty()) {
                throw new PromotionException("Description cannot be empty");
            } else if (promotionDTO.getValue() == null) {
                throw new PromotionException("Value cannot be empty");
            } else if (promotionDTO.getEnddate().toString().isEmpty() || promotionDTO.getEnddate().isBefore(LocalDateTime.now()) || promotionDTO.getEnddate().isBefore(promotionDTO.getStartdate())) {
                throw new PromotionException("End date is invalid");
            } else if (promotionDTO.getStartdate().toString().isEmpty() ) {
                throw new PromotionException("Start cannot be empty");
            } else if (promotionDTO.getUsageLimit() == null) {
                throw new PromotionException("Usage limit cannot be empty");
            } else if (promotionDTO.getMinOrderValue() == null) {
                throw new PromotionException("Min order value cannot be empty");
            } else if (promotionDTO.getMaxDiscount() == null) {
                throw new PromotionException("Max discount cannot be empty");
            } else if (promotionDTO.getDescription().isEmpty()) {
                throw new PromotionException("Description cannot be empty");
            } else if (promotionDTO.getCategories().isEmpty()) {
                throw new PromotionException("Categories cannot be empty");
            } else if (promotionDTO.getPerUserLimit() == null) {
                throw new PromotionException("PerUserLimit cannot be empty");
            }


        if(promotionDTO.getType().equalsIgnoreCase("percentage")){
            if(promotionDTO.getValue() <= 0 || promotionDTO.getValue() >= 51){
                throw new PromotionException("Value cannot less than 0% and more than 51%");
            }
            if (promotionDTO.getMaxDiscount() <= 0 || promotionDTO.getMaxDiscount() >= 10000000){
                throw new PromotionException("Max discount cannot less than 0 and more than 10.000.000VNĐ");
            }
        }else if(promotionDTO.getType().equalsIgnoreCase("fixed")){
            if(promotionDTO.getValue() <= 0 || promotionDTO.getValue() >= 10000000 ){
                throw new PromotionException("Value cannot less than 0 and more than 10.000.000VNĐ");
            }
            if (promotionDTO.getMaxDiscount().toString().isEmpty()){
                throw new PromotionException("Max discount cannot use for type fixed");
            }
        } else if (promotionDTO.getType().equalsIgnoreCase("shipping")) {
            if(promotionDTO.getValue() <= 0 || promotionDTO.getValue() >= 100000 ){
                throw new PromotionException("Value cannot less than 0 and more than 100.000VNĐ");
            }
            if (promotionDTO.getMaxDiscount().toString().isEmpty()){
                throw new PromotionException("Max discount cannot use for type fixed");
            }
        }


        if(promotionDTO.getMinOrderValue()<=0){
            throw new PromotionException("Min order value cannot less than 0");
        } else if (promotionDTO.getUsageLimit() <=0) {
            throw new PromotionException("Usage limit cannot less than 0");
        } else if (promotionDTO.getPerUserLimit()<=0 || promotionDTO.getPerUserLimit() >= 10) {
            throw new PromotionException("PerUserLimit cannot less than 0 and more than 10");
        }

        if(statusUpdateOrAdd.equalsIgnoreCase("add") && promotionDTO.getStartdate().isBefore(LocalDateTime.now())){
            throw new PromotionException("Start date cannot be before now");
        }


        Promotion promotion = promotionMapper.toEntity(promotionDTO);
        List<Category> category = promotion.getCategories().stream().map(
                c -> categoryRepository.findById(c.getId()).orElseThrow( () ->  new PromotionException("Not found category"))).toList();
        promotion.setCategories(category);
        promotionRepository.save(promotion);
    }

    public void setStatus(Integer id,String status) {
        promotionRepository.findById(id).ifPresent(promotion -> {promotion.setStatus(status);promotionRepository.save(promotion);});
    }
    public void detelePromotion(Integer id){

            promotionRepository.deleteById(id);


    }
}
