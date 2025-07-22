package org.example.ecommerce.common.dto.seller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SellerReviewStatsDTO {
    private int totalReviews;
    private double averageRating;
    private Map<Integer, Integer> starDistribution; // key: số sao, value: số lượng
    private int reviewsWithImages;
} 