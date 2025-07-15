package org.example.ecommerce.controller.admin;

import org.example.ecommerce.common.dto.dashboard.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @GetMapping("/data")
    public OverviewDashboardDTO overviewDashboard() {
        OverviewDashboardDTO dto = new OverviewDashboardDTO();

        // Quarterly Revenue
        ArrayList<QuarterlyRevenueDTO> quarterlyRevenueList = new ArrayList<>();
        quarterlyRevenueList.add(new QuarterlyRevenueDTO("Q1 2023", 450_000_000L, 12.0, 15420, 29200));
        quarterlyRevenueList.add(new QuarterlyRevenueDTO("Q2 2023", 520_000_000L, 15.5, 17800, 29200));
        quarterlyRevenueList.add(new QuarterlyRevenueDTO("Q3 2023", 580_000_000L, 11.5, 19200, 30200));
        quarterlyRevenueList.add(new QuarterlyRevenueDTO("Q4 2023", 680_000_000L, 17.2, 22100, 30800));
        quarterlyRevenueList.add(new QuarterlyRevenueDTO("Q1 2024", 750_000_000L, 66.7, 24300, 30900));
        quarterlyRevenueList.add(new QuarterlyRevenueDTO("Q2 2024", 820_000_000L, 57.7, 26500, 31000));
        dto.setQuarterlyRevenueDTOList(quarterlyRevenueList);

        // Region Performance
        ArrayList<RegionPerformanceDTO> regionList = new ArrayList<>();
        regionList.add(new RegionPerformanceDTO("Hà Nội", 180_000_000L, 8500, 12000));
        regionList.add(new RegionPerformanceDTO("TP.HCM", 250_000_000L, 11200, 15800));
        regionList.add(new RegionPerformanceDTO("Đà Nẵng", 95_000_000L, 4200, 6500));
        regionList.add(new RegionPerformanceDTO("Cần Thơ", 75_000_000L, 3800, 5200));
        regionList.add(new RegionPerformanceDTO("Hải Phòng", 85_000_000L, 4100, 5800));
        regionList.add(new RegionPerformanceDTO("Khác", 65_000_000L, 3200, 4700));
        dto.setRegionPerformanceDTOList(regionList);

        // User Demographics
        ArrayList<UserDemographicsDTO> demoList = new ArrayList<>();
        demoList.add(new UserDemographicsDTO("18-25", 2400, 2800, 5200));
        demoList.add(new UserDemographicsDTO("26-35", 3200, 3600, 6800));
        demoList.add(new UserDemographicsDTO("36-45", 2800, 2400, 5200));
        demoList.add(new UserDemographicsDTO("46-55", 1800, 1600, 3400));
        demoList.add(new UserDemographicsDTO("55+", 1200, 1000, 2200));
        dto.setUserDemographicsDTOList(demoList);

        // Conversion Funnel
        ArrayList<ConversionFunnelDTO> funnelList = new ArrayList<>();
        funnelList.add(new ConversionFunnelDTO("Lượt truy cập", 100000, "#001F54"));
        funnelList.add(new ConversionFunnelDTO("Xem sản phẩm", 45000, "#1E3A8A"));
        funnelList.add(new ConversionFunnelDTO("Thêm vào giỏ", 18000, "#3B82F6"));
        funnelList.add(new ConversionFunnelDTO("Bắt đầu thanh toán", 12000, "#60A5FA"));
        funnelList.add(new ConversionFunnelDTO("Hoàn thành đơn hàng", 8500, "#93C5FD"));
        dto.setConversionFunnelDTOList(funnelList);

        // Top Products
        ArrayList<TopProductDTO> productList = new ArrayList<>();
        productList.add(new TopProductDTO("iPhone 15", 45_000_000L, "Điện tử"));
        productList.add(new TopProductDTO("Samsung Galaxy", 38_000_000L, "Điện tử"));
        productList.add(new TopProductDTO("Áo thun nam", 25_000_000L, "Thời trang"));
        productList.add(new TopProductDTO("Giày sneaker", 32_000_000L, "Giày dép"));
        productList.add(new TopProductDTO("Laptop Dell", 28_000_000L, "Máy tính"));
        productList.add(new TopProductDTO("Túi xách nữ", 22_000_000L, "Thời trang"));
        productList.add(new TopProductDTO("Đồng hồ thông minh", 18_000_000L, "Điện tử"));
        productList.add(new TopProductDTO("Quần jeans", 15_000_000L, "Thời trang"));
        dto.setTopProductDTOList(productList);

        // Service Quality
        ArrayList<ServiceQualityDTO> serviceList = new ArrayList<>();
        serviceList.add(new ServiceQualityDTO("Giao hàng", 85, 100));
        serviceList.add(new ServiceQualityDTO("Chất lượng SP", 92, 100));
        serviceList.add(new ServiceQualityDTO("Hỗ trợ KH", 78, 100));
        serviceList.add(new ServiceQualityDTO("Giá cả", 88, 100));
        serviceList.add(new ServiceQualityDTO("Giao diện", 95, 100));
        serviceList.add(new ServiceQualityDTO("Bảo mật", 90, 100));
        dto.setServiceQualityDTOList(serviceList);

        // Real-Time Activity
        ArrayList<RealTimeActivityDTO> activityList = new ArrayList<>();
        activityList.add(new RealTimeActivityDTO("00:00", 1200, 45, 2_500_000L));
        activityList.add(new RealTimeActivityDTO("04:00", 800, 28, 1_800_000L));
        activityList.add(new RealTimeActivityDTO("08:00", 3500, 125, 6_200_000L));
        activityList.add(new RealTimeActivityDTO("12:00", 5200, 180, 8_900_000L));
        activityList.add(new RealTimeActivityDTO("16:00", 4800, 165, 7_800_000L));
        activityList.add(new RealTimeActivityDTO("20:00", 6200, 220, 11_200_000L));
        dto.setRealTimeActivityDTOList(activityList);

        return dto;
    }
}

