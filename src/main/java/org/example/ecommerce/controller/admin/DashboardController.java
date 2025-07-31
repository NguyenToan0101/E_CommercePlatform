package org.example.ecommerce.controller.admin;

import org.example.ecommerce.common.dto.dashboard.*;
import org.example.ecommerce.repository.OderFactLogRepo;
import org.example.ecommerce.service.admin.OderLogService;
import org.example.ecommerce.service.admin.VisitService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {
    private final OderLogService orderLogService;
    private final VisitService visitService;
    private final OderFactLogRepo orderFactLogRepo;
    public DashboardController(OderLogService orderLogService, VisitService visitService, OderFactLogRepo orderFactLogRepo) {
        this.orderLogService = orderLogService;
        this.visitService = visitService;
        this.orderFactLogRepo = orderFactLogRepo;
    }

    @GetMapping("/data")
    public OverviewDashboardDTO overviewDashboard() {
        OverviewDashboardDTO dto = new OverviewDashboardDTO();

        // Quarterly Revenue
        ArrayList<QuarterlyRevenueDTO> dtos = orderLogService.quarterlyRevenueDTOArrayList();
        dto.setQuarterlyRevenueDTOList(dtos);
//        quarterlyRevenueList.add(new QuarterlyRevenueDTO("Q1 2024", 450000000, 12.0, 15420, 29200));
//        quarterlyRevenueList.add(new QuarterlyRevenueDTO("Q2 2024", 520_000_000L, 15.5, 17800, 29200));
//        quarterlyRevenueList.add(new QuarterlyRevenueDTO("Q3 2024", 580_000_000L, 11.5, 19200, 30200));
//        quarterlyRevenueList.add(new QuarterlyRevenueDTO("Q4 2024", 680_000_000L, 17.2, 22100, 30800));
//        quarterlyRevenueList.add(new QuarterlyRevenueDTO("Q1 2025", 750_000_000L, 66.7, 24300, 30900));
//        quarterlyRevenueList.add(new QuarterlyRevenueDTO("Q2 2025", 820_000_000L, 57.7, 26500, 31000));
//        quarterlyRevenueList.add(new QuarterlyRevenueDTO("Q3 2025", 820_000_000L, 57.7, 26500, 31000));


        // Region Performance
        ArrayList<RegionPerformanceDTO> regionList = new ArrayList<>();
        regionList.add(new RegionPerformanceDTO("Hà Nội",orderLogService.sumRevenueByAddress("Thành phố Hà Nội") , orderLogService.countOrderByAddress("Thành phố Hà Nội")));
        regionList.add(new RegionPerformanceDTO("TP.HCM", orderLogService.sumRevenueByAddress("Thành phố Hồ Chí Minh"), orderLogService.countOrderByAddress("Thành phố Hồ Chí Minh")));
        regionList.add(new RegionPerformanceDTO("Đà Nẵng", orderLogService.sumRevenueByAddress("Thành phố Đà Nẵng"), orderLogService.countOrderByAddress("Thành phố Đà Nẵng")));
        regionList.add(new RegionPerformanceDTO("Cần Thơ", orderLogService.sumRevenueByAddress("Thành phố Cần Thơ"), orderLogService.countOrderByAddress("Thành phố Cần Thơ")));
        regionList.add(new RegionPerformanceDTO("Hải Phòng", orderLogService.sumRevenueByAddress("Thành phố Hải Phòng"), orderLogService.countOrderByAddress("Thành phố Hải Phòng")));
        regionList.add(new RegionPerformanceDTO("Khác", orderLogService.sumDifferenceRevenueByAddress(), orderLogService.countDifferenceCountByAddress()));
        dto.setRegionPerformanceDTOList(regionList);

        // User Demographics
        ArrayList<UserDemographicsDTO> demoList = new ArrayList<>();
        demoList.add(new UserDemographicsDTO("18-25", orderLogService.countMaleByAge(18,25), orderLogService.countFemaleByAge(18,25), orderLogService.countCustomerByAge(18,25)));
        demoList.add(new UserDemographicsDTO("26-35",  orderLogService.countMaleByAge(26,35), orderLogService.countFemaleByAge(26,35), orderLogService.countCustomerByAge(26,35)));
        demoList.add(new UserDemographicsDTO("36-45",  orderLogService.countMaleByAge(36,45), orderLogService.countFemaleByAge(36,45), orderLogService.countCustomerByAge(36,45)));
        demoList.add(new UserDemographicsDTO("46-55",  orderLogService.countMaleByAge(46,55), orderLogService.countFemaleByAge(46,55), orderLogService.countCustomerByAge(46,55)));
        demoList.add(new UserDemographicsDTO("55+",  orderLogService.countMaleByAge(55,100), orderLogService.countFemaleByAge(55,100), orderLogService.countCustomerByAge(55,100)));
        dto.setUserDemographicsDTOList(demoList);
        System.out.println("Test age---" + demoList);

        // Conversion Funnel
        ArrayList<ConversionFunnelDTO> funnelList = new ArrayList<>();
        funnelList.add(new ConversionFunnelDTO("Lượt truy cập", (int) visitService.getVisitHome(), "#001F54"));
        funnelList.add(new ConversionFunnelDTO("Xem sản phẩm", visitService.countPageVisit("/detailproduct"), "#1E3A8A"));
        funnelList.add(new ConversionFunnelDTO("Xem giỏ hàng", visitService.countPageVisit("/cart"), "#3B82F6"));
        funnelList.add(new ConversionFunnelDTO("Bắt đầu thanh toán", visitService.countPageVisit("/checkout/preview_realtime"), "#60A5FA"));
        funnelList.add(new ConversionFunnelDTO("Hoàn thành đơn hàng", visitService.countPageVisit("/checkout/realtime"), "#93C5FD"));
        dto.setConversionFunnelDTOList(funnelList);

        // Top Products
        System.out.println("----Top product selling" + orderLogService.topProductSelling());

        Set<TopProductDTO> productList = orderLogService.topProductSelling().stream().map(
                r -> new TopProductDTO((String) r[0],(BigDecimal) r[1], (String) r[2])

        ).collect(Collectors.toSet());

//        productList.add(new TopProductDTO("iPhone 15", 45_000_000L, "Điện tử"));
//        productList.add(new TopProductDTO("Samsung Galaxy", 38_000_000L, "Điện tử"));
//        productList.add(new TopProductDTO("Áo thun nam", 25_000_000L, "Thời trang"));
//        productList.add(new TopProductDTO("Giày sneaker", 32_000_000L, "Giày dép"));
//        productList.add(new TopProductDTO("Laptop Dell", 28_000_000L, "Máy tính"));
//        productList.add(new TopProductDTO("Túi xách nữ", 22_000_000L, "Thời trang"));
//        productList.add(new TopProductDTO("Đồng hồ thông minh", 18_000_000L, "Điện tử"));
//        productList.add(new TopProductDTO("Quần jeans", 15_000_000L, "Thời trang"));
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
        ArrayList<RealTimeActivityDTO> activityList = orderFactLogRepo.getRealTimeActivityData();
        System.out.println("time------" + orderFactLogRepo.getCurrentHourMinute());
//        activityList.add(new RealTimeActivityDTO("00:00", 1200, 45, 2_500_000L));
//        activityList.add(new RealTimeActivityDTO("04:00", 800, 28, 1_800_000L));
//        activityList.add(new RealTimeActivityDTO("08:00", 3500, 125, 6_200_000L));
//        activityList.add(new RealTimeActivityDTO("12:00", 5200, 180, 8_900_000L));
//        activityList.add(new RealTimeActivityDTO("16:00", 4800, 165, 7_800_000L));
//        activityList.add(new RealTimeActivityDTO("20:00", 6200, 220, 11_200_000L));
        dto.setRealTimeActivityDTOList(activityList);

        return dto;
    }
}
