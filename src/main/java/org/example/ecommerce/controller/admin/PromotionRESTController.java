package org.example.ecommerce.controller.admin;


import org.example.ecommerce.common.dto.promotion.OverviewDTO;
import org.example.ecommerce.common.dto.promotion.PromotionDTO;
import org.example.ecommerce.common.dto.promotion.PromotionDashboardDTO;
import org.example.ecommerce.common.dto.promotion.StatusPromotionDTO;
import org.example.ecommerce.common.dto.promotion.overview.PromotionMonthlyDTO;
import org.example.ecommerce.common.dto.promotion.overview.PromotionTopDTO;
import org.example.ecommerce.common.dto.promotion.overview.PromotionTypeDTO;
import org.example.ecommerce.service.PromotionService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/promotion")
public class PromotionRESTController {
    private final PromotionService promotionService;
    public PromotionRESTController(PromotionService promotionService) {
        this.promotionService = promotionService;
    }
    @GetMapping("/data")
    public PromotionDashboardDTO getPromotionData() {
        // Dữ liệu biểu đồ theo tháng
        ArrayList<PromotionMonthlyDTO> monthlyData = new ArrayList<>();
        monthlyData.add(new PromotionMonthlyDTO("Jan", new BigDecimal("100000"), 120, new BigDecimal("5000")));
        monthlyData.add(new PromotionMonthlyDTO("Feb", new BigDecimal("150000"), 180, new BigDecimal("7000")));
        monthlyData.add(new PromotionMonthlyDTO("Mar", new BigDecimal("130000"), 160, new BigDecimal("6000")));

        // Dữ liệu phân loại khuyến mãi
        ArrayList<PromotionTypeDTO> typeData = new ArrayList<>();
        typeData.add(new PromotionTypeDTO("Discount", 40.5f, "#FF6384"));
        typeData.add(new PromotionTypeDTO("Buy One Get One", 35.0f, "#36A2EB"));
        typeData.add(new PromotionTypeDTO("Free Shipping", 24.5f, "#FFCE56"));

        // Dữ liệu top khuyến mãi
        ArrayList<PromotionTopDTO> topData = new ArrayList<>();
        topData.add(new PromotionTopDTO("Spring Sale", 90, new BigDecimal("500000"), 2.5f));
        topData.add(new PromotionTopDTO("Summer Deal", 70, new BigDecimal("400000"), 1.8f));
        topData.add(new PromotionTopDTO("Holiday Promo", 60, new BigDecimal("300000"), 1.2f));

        // Tạo đối tượng OverviewDTO
        OverviewDTO overview = new OverviewDTO(
                new BigDecimal("380000"), // revenue
                12.5f,                    // revenuePercentage
                420,                      // order
                8.3f,                     // orderPercentage
                3.2f,                     // conversionRate
                1.1f,                     // conversionRatePercentage
                75,                       // newUser
                6,                        // newUserPercentage
                monthlyData,             // promotionEffectivenessData
                typeData,                // promotionTypeData
                topData                  // topPromotionsData
        );
        List<PromotionDTO> promotionData = promotionService.getPromotions();
        // Gói vào PromotionDashboardDTO
        PromotionDashboardDTO dto = new PromotionDashboardDTO();
        dto.setOverview(overview);
        dto.setPromotions(promotionData);
        return dto;
    }

    @PostMapping("/update")
    public void updatePromotion(@RequestBody PromotionDTO promotionDTO) {
        promotionService.save(promotionDTO,"update");
    }


    @PostMapping("/add")
    public void addPromotion(@RequestBody PromotionDTO promotionDTO) {

        promotionService.save(promotionDTO,"add");
    }

    @PostMapping("/setStatus")
    public void setStatus(@RequestBody StatusPromotionDTO statusPromotionDTO) {

        promotionService.setStatus(statusPromotionDTO.getId(),statusPromotionDTO.getStatus());
    }
    @PostMapping("/delete")
    public void deletePromotion(@RequestBody StatusPromotionDTO statusPromotionDTO) {
        System.out.println("ID-------------: "+statusPromotionDTO.getId());
        promotionService.detelePromotion(statusPromotionDTO.getId());
    }
}
