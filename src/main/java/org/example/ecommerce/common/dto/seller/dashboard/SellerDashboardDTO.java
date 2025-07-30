package org.example.ecommerce.common.dto.seller.dashboard;

import lombok.Data;

import java.util.List;

@Data
public class SellerDashboardDTO {
    private DashboardOverviewDTO overview;
    private List<MonthlyRevenueDTO> monthlyRevenue;
    private List<CategoryRevenueDTO> categoryRevenue;
    private List<BestSellingProductDTO> bestSellingProducts;
    private List<RecentOrderDTO> recentOrders;
}

