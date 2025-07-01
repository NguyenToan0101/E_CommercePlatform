package org.example.ecommerce.common.dto.promotion;

import lombok.Data;
import org.example.ecommerce.common.dto.promotion.overview.PromotionTypeDTO;
import org.example.ecommerce.common.dto.promotion.overview.PromotionMonthlyDTO;
import org.example.ecommerce.common.dto.promotion.overview.PromotionTopDTO;

import java.math.BigDecimal;
import java.util.ArrayList;

@Data
public class OverviewDTO {
     //Card
    private BigDecimal revenue;
    private float revenuePercentage;
    private int order;
    private float orderPercentage;
    private float conversionRate;
    private float conversionRatePercentage;
    private int newUser;
    private int newUserPercentage;

    //Bieu do Hieu qua theo thang
    private ArrayList<PromotionMonthlyDTO> promotionEffectivenessData;
    //Phan loai KM
    private ArrayList<PromotionTypeDTO> promotionTypeData;
    //Top KM
    private ArrayList<PromotionTopDTO> topPromotionsData;

    public OverviewDTO(BigDecimal revenue, float revenuePercentage, int order, float orderPercentage, float conversionRate, float conversionRatePercentage, int newUser, int newUserPercentage, ArrayList<PromotionMonthlyDTO> promotionEffectivenessData, ArrayList<PromotionTypeDTO> promotionTypeData, ArrayList<PromotionTopDTO> topPromotionsData) {
        this.revenue = revenue;
        this.revenuePercentage = revenuePercentage;
        this.order = order;
        this.orderPercentage = orderPercentage;
        this.conversionRate = conversionRate;
        this.conversionRatePercentage = conversionRatePercentage;
        this.newUser = newUser;
        this.newUserPercentage = newUserPercentage;
        this.promotionEffectivenessData = promotionEffectivenessData;
        this.promotionTypeData = promotionTypeData;
        this.topPromotionsData = topPromotionsData;
    }
}
