package org.example.ecommerce.service.seller.review;

import org.example.ecommerce.common.dto.seller.SellerReviewDTO;
import org.example.ecommerce.common.dto.seller.SellerReviewStatsDTO;

import java.util.List;

public interface SellerReviewService {
    /**
     * Lấy danh sách review cho seller (theo shopId), có thể filter theo productId, rating, hasImage, date range
     */
    List<SellerReviewDTO> getReviewsForSeller(Integer sellerId, Integer productId, Integer rating, Boolean hasImage, String fromDate, String toDate);
    SellerReviewDTO getReviewDetailForSeller(Integer sellerId, Integer reviewId);
    SellerReviewStatsDTO getReviewStatsForSeller(Integer sellerId, Integer productId);
    void replyToReview(Integer sellerId, Integer reviewId, String replyContent);
    String getReplyContentByReviewId(Integer reviewId);
} 