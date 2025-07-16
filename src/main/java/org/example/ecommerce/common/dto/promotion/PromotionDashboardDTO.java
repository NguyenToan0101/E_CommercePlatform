package org.example.ecommerce.common.dto.promotion;

import lombok.Data;

import java.util.List;
@Data
public class PromotionDashboardDTO {
    private OverviewDTO overview;
    private List<PromotionDTO> promotions;

}
