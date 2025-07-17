package org.example.ecommerce.controller.admin;


import org.example.ecommerce.common.dto.promotion.OverviewDTO;
import org.example.ecommerce.common.dto.promotion.PromotionDTO;
import org.example.ecommerce.common.dto.promotion.PromotionDashboardDTO;
import org.example.ecommerce.common.dto.promotion.StatusPromotionDTO;
import org.example.ecommerce.common.dto.promotion.overview.PromotionMonthlyDTO;
import org.example.ecommerce.common.dto.promotion.overview.PromotionTopDTO;
import org.example.ecommerce.common.dto.promotion.overview.PromotionTypeDTO;
import org.example.ecommerce.entity.Promotion;
import org.example.ecommerce.service.PromotionService;
import org.example.ecommerce.service.admin.OderLogService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/promotion")
public class PromotionRESTController {
    private final PromotionService promotionService;
    private final OderLogService orderLogService;
    public PromotionRESTController(PromotionService promotionService, OderLogService orderLogService) {
        this.promotionService = promotionService;
        this.orderLogService = orderLogService;
    }
    @GetMapping("/data")
    public PromotionDashboardDTO getPromotionData() {
        // Dữ liệu biểu đồ theo tháng
        ArrayList<PromotionMonthlyDTO> monthlyData = new ArrayList<>();
        monthlyData.add(new PromotionMonthlyDTO("01/2025", new BigDecimal("180000000"), 1850, new BigDecimal("25000000")));
        monthlyData.add(new PromotionMonthlyDTO("02/2025", new BigDecimal("165000000"), 1650, new BigDecimal("22000000")));
        monthlyData.add(new PromotionMonthlyDTO("03/2025", new BigDecimal("210000000"), 2100, new BigDecimal("28000000")));
        monthlyData.add(new PromotionMonthlyDTO("04/2025", new BigDecimal("195000000"), 1950, new BigDecimal("26000000")));
        monthlyData.add(new PromotionMonthlyDTO("05/2025", new BigDecimal("225000000"), 2250, new BigDecimal("30000000")));
        monthlyData.add(new PromotionMonthlyDTO("06/2025", new BigDecimal("240000000"), 2400, new BigDecimal("32000000")));
        monthlyData.add(new PromotionMonthlyDTO("07/2025", orderLogService.getTotalProfitPromotionByMonth(2025,7),
                orderLogService.getOderPromotionCountByMonth(2025,7),
                orderLogService.getDiscountAmountPromotionCountByMonth(2025,7)
                ));


        // Dữ liệu phân loại khuyến mãi
        ArrayList<PromotionTypeDTO> typeData = new ArrayList<>();
        typeData.add(new PromotionTypeDTO("Giảm giá %", orderLogService.getPercentTypePromotion("PERCENTAGE"), "#001F54"));
        typeData.add(new PromotionTypeDTO("Giảm giá cố định", orderLogService.getPercentTypePromotion("FIXED"), "#4DD0E1"));
        typeData.add(new PromotionTypeDTO("Miễn phí vận chuyển", orderLogService.getPercentTypePromotion("SHIPPING"), "#81C784"));
//        typeData.add(new PromotionTypeDTO("Mua 1 tặng 1", 10.0f, "#FFB74D"));


        // Dữ liệu top khuyến mãi
        ArrayList<PromotionTopDTO> topData = new ArrayList<>();
//        topData.add(new PromotionTopDTO("Spring Sale", 90, new BigDecimal("500000"), 2.5f));
//        topData.add(new PromotionTopDTO("Summer Deal", 70, new BigDecimal("400000"), 1.8f));
//        topData.add(new PromotionTopDTO("Holiday Promo", 60, new BigDecimal("300000"), 1.2f));

        List<Promotion> promotions = promotionService.getAllPromotions();
        for (Promotion promotion : promotions) {
            if(orderLogService.getOrderFactLogByPromotionId(promotion.getId()).stream().anyMatch(orderLog -> orderLog.getPromotionId().equals(promotion.getId()))) {
                topData.add(new PromotionTopDTO(promotion.getName(),orderLogService.countOderByPromotionByID(promotion.getId()),
                        orderLogService.sumRevenuePromotionById(promotion.getId()),
                        orderLogService.getConversionPromotionById(promotion.getId()),
                        orderLogService.sumProfitAmountPromotionById(promotion.getId()).doubleValue() /
                                orderLogService.sumDiscountAmountPromotionById(promotion.getId()).multiply(BigDecimal.valueOf(100)).doubleValue()

                        )

                );

            }

        }

        // Tạo đối tượng OverviewDTO
        OverviewDTO overview = new OverviewDTO(
                orderLogService.getSumProfitPromotion(), // revenue
                12.5f,                    // revenuePercentage
                orderLogService.countOrderPromotion(),                      // order
                8.3f,                     // orderPercentage
                orderLogService.getConversion(),                     // conversionRate
                1.1f,                     // conversionRatePercentage
                orderLogService.getCustomerCount(),                       // newUser
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

    @PostMapping("/setStatusActive")
    public void setStatusActive(@RequestBody StatusPromotionDTO statusPromotionDTO) {

        promotionService.setStatusActive(statusPromotionDTO.getId(),statusPromotionDTO.getStatus());
    }
    @PostMapping("/setStatusPaused")
    public void setStatusPaused(@RequestBody StatusPromotionDTO statusPromotionDTO) {

        promotionService.setStatusPaused(statusPromotionDTO.getId(),statusPromotionDTO.getStatus());
    }
    @PostMapping("/delete")
    public void deletePromotion(@RequestBody StatusPromotionDTO statusPromotionDTO) {
        System.out.println("ID: "+statusPromotionDTO.getId());
        promotionService.detelePromotion(statusPromotionDTO.getId());
    }
    @PostMapping("/template")
    public void templatePromotion(@RequestBody  String title){
            promotionService.templatePromotion(title);
    }
}
