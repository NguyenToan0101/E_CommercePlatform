package org.example.ecommerce.controller.admin;

import org.example.ecommerce.common.dto.admin.promotion.OverviewDTO;
import org.example.ecommerce.common.dto.admin.promotion.PromotionDashboardDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/promotion")
public class PromotionRESTController {
        @GetMapping("/data")
        public PromotionDashboardDTO getPromotionData() {

            PromotionDashboardDTO dto = new PromotionDashboardDTO();
            dto.setOverview(new OverviewDTO(10000,20,4,100));
            return dto;
        }
}
