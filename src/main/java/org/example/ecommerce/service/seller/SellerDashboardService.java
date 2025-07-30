package org.example.ecommerce.service.seller;

import org.example.ecommerce.common.dto.seller.dashboard.SellerDashboardDTO;

public interface SellerDashboardService {
    /**
     * Lấy dữ liệu dashboard cho seller
     * @param sellerId ID của seller
     * @return SellerDashboardDTO chứa tất cả dữ liệu dashboard
     */
    SellerDashboardDTO getDashboardData(Integer sellerId);
    
    /**
     * Lấy dữ liệu dashboard cho shop
     * @param shopId ID của shop
     * @return SellerDashboardDTO chứa tất cả dữ liệu dashboard
     */
    SellerDashboardDTO getDashboardDataByShopId(Integer shopId);
} 