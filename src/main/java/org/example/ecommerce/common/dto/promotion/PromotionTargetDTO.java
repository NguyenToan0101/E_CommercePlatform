package org.example.ecommerce.common.dto.promotion;

import lombok.Data;

@Data
public class PromotionTargetDTO {
    private int promotionid;
    private int productid;
    private int categoryid;
    private int shopid;

}
