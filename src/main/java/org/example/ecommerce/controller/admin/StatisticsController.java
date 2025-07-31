package org.example.ecommerce.controller.admin;

import org.example.ecommerce.common.dto.analysis.*;
import org.example.ecommerce.repository.OderFactLogRepo;
import org.example.ecommerce.service.admin.OderLogService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/statistics")
public class StatisticsController {
    private final OderLogService orderLogService;
    private final OderFactLogRepo orderFactLogRepo;
    public StatisticsController(OderLogService orderLogService, OderFactLogRepo orderFactLogRepo) {
        this.orderLogService = orderLogService;
        this.orderFactLogRepo = orderFactLogRepo;
    }

    @GetMapping("/data")
    public OverviewStatisticsDTO getOverviewStatistics() {
        // Doanh thu theo tháng
        ArrayList<MonthlyRevenueDTO> monthlyRevenueDTOS = orderLogService.getMonthlyRevenue();

//                new ArrayList<>(List.of(
//                new MonthlyRevenueDTO("01/2024", new BigDecimal("320000000"), new BigDecimal("95000000"), new BigDecimal("225000000")),
//                new MonthlyRevenueDTO("02/2024", new BigDecimal("340000000"), new BigDecimal("102000000"), new BigDecimal("238000000")),
//                new MonthlyRevenueDTO("03/2024", new BigDecimal("380000000"), new BigDecimal("115000000"), new BigDecimal("265000000")),
//                new MonthlyRevenueDTO("04/2024", new BigDecimal("360000000"), new BigDecimal("108000000"), new BigDecimal("252000000")),
//                new MonthlyRevenueDTO("05/2024", new BigDecimal("410000000"), new BigDecimal("125000000"), new BigDecimal("285000000")),
//                new MonthlyRevenueDTO("06/2024", new BigDecimal("450000000"), new BigDecimal("135000000"), new BigDecimal("315000000"))
//        ));

        // Doanh thu theo năm
        ArrayList<YearlyRevenueDTO> yearlyRevenueDTOS = orderLogService.getYearlyRevenue();

//                new ArrayList<>(List.of(
//                new YearlyRevenueDTO("2020", new BigDecimal("1250000000"), new BigDecimal("320000000"), new BigDecimal("930000000")),
//                new YearlyRevenueDTO("2021", new BigDecimal("1850000000"), new BigDecimal("480000000"), new BigDecimal("1370000000")),
//                new YearlyRevenueDTO("2022", new BigDecimal("2450000000"), new BigDecimal("680000000"), new BigDecimal("1770000000")),
//                new YearlyRevenueDTO("2023", new BigDecimal("3200000000"), new BigDecimal("950000000"), new BigDecimal("2250000000")),
//                new YearlyRevenueDTO("2024", new BigDecimal("4100000000"), new BigDecimal("1250000000"), new BigDecimal("2850000000"))
//        ));

        // Trạng thái đơn hàng
        ArrayList<OrderStatusDTO> orderStatusDTOS =
                new ArrayList<>(List.of(
                        new OrderStatusDTO("Hoàn thành", (double) orderFactLogRepo.countOrderStatus("Đã giao") /orderFactLogRepo.countAllOrderStatus() *100, "#81C784"),
                        new OrderStatusDTO("Chờ lấy hàng", (double) orderFactLogRepo.countOrderStatus("Chờ lấy hàng") /orderFactLogRepo.countAllOrderStatus() *100, "#4DD0E1"),
                        new OrderStatusDTO("Đang giao hàng", (double) orderFactLogRepo.countOrderStatus("Đang giao hàng") /orderFactLogRepo.countAllOrderStatus() *100, "#FFB74D"),
                        new OrderStatusDTO("Đã hủy", (double) orderFactLogRepo.countOrderStatus("Đã hủy") /orderFactLogRepo.countAllOrderStatus() *100, "#E57373"),
                        new OrderStatusDTO("Đã xác nhận",(double) orderFactLogRepo.countOrderStatus("Đã xác nhận") /orderFactLogRepo.countAllOrderStatus() *100, "#001F54")
                ));

        // Phương thức thanh toán
        ArrayList<PaymentMethodDTO> paymentMethodDTOS = new ArrayList<>(List.of(
                new PaymentMethodDTO("Thẻ tín dụng", 0, "#001F54"),
                new PaymentMethodDTO("Ví điện tử", 0, "#4DD0E1"),
                new PaymentMethodDTO("COD", 13, "#81C784"),
                new PaymentMethodDTO("Chuyển khoản", 87, "#FFB74D")
        ));

        // Phân tích đơn hàng theo thời gian
        ArrayList<OrderTimeDTO> orderTimeDTOS = orderLogService.getOrderTimeStatistics();
//                new ArrayList<>(List.of(
//                new OrderTimeDTO("00:00 - 04:00", 850),
//                new OrderTimeDTO("04:00 - 08:00", 1200),
//                new OrderTimeDTO("08:00 - 12:00", 3500),
//                new OrderTimeDTO("12:00 - 16:00", 4200),
//                new OrderTimeDTO("16:00 - 20:00", 3800),
//                new OrderTimeDTO("20:00 - 24:00", 2500)
//        ));

        // Danh mục sản phẩm
        ArrayList<ProductCategoryDTO> productCategoryDTOS = orderLogService.getProductCategoryStatistics();

//                new ArrayList<>(List.of(
//                new ProductCategoryDTO("Điện tử", 1250, 45000, new BigDecimal("1350000000")),
//                new ProductCategoryDTO("Thời trang", 2800, 38000, new BigDecimal("950000000")),
//                new ProductCategoryDTO("Gia dụng", 1500, 25000, new BigDecimal("750000000")),
//                new ProductCategoryDTO("Mỹ phẩm", 950, 18000, new BigDecimal("650000000")),
//                new ProductCategoryDTO("Thực phẩm", 1200, 22000, new BigDecimal("550000000")),
//                new ProductCategoryDTO("Sách", 3500, 15000, new BigDecimal("450000000"))
//        ));

        // Đánh giá sản phẩm
        ArrayList<ProductRatingDTO> productRatingDTOS = new ArrayList<>(List.of(
                new ProductRatingDTO("5 sao", orderFactLogRepo.countRatingByStar(5), ((double) orderFactLogRepo.countRatingByStar(5) /orderFactLogRepo.countAllRatingByStar() *100)),
                new ProductRatingDTO("4 sao", orderFactLogRepo.countRatingByStar(4), ((double) orderFactLogRepo.countRatingByStar(4) /orderFactLogRepo.countAllRatingByStar() *100)),
                new ProductRatingDTO("3 sao", orderFactLogRepo.countRatingByStar(3), ((double) orderFactLogRepo.countRatingByStar(3) /orderFactLogRepo.countAllRatingByStar() *100)),
                new ProductRatingDTO("2 sao", orderFactLogRepo.countRatingByStar(2), ((double) orderFactLogRepo.countRatingByStar(2) /orderFactLogRepo.countAllRatingByStar() *100)),
                new ProductRatingDTO("1 sao", orderFactLogRepo.countRatingByStar(1), ((double) orderFactLogRepo.countRatingByStar(1) /orderFactLogRepo.countAllRatingByStar() *100))
        ));

        // Chất lượng dịch vụ
        ArrayList<ServiceQualityDTO> serviceQualityDTOS = new ArrayList<>(List.of(
                new ServiceQualityDTO("Độ tin cậy", 92),
                new ServiceQualityDTO("Khả năng đáp ứng", 85),
                new ServiceQualityDTO("Sự đảm bảo", 88),
                new ServiceQualityDTO("Sự đồng cảm", 90),
                new ServiceQualityDTO("Tính hữu hình", 82)
        ));

        // Thiết bị người dùng
        ArrayList<UserDeviceDTO> userDeviceDTOS =
                new ArrayList<>(List.of(
                        new UserDeviceDTO("Mobile", ((double) orderFactLogRepo.countDeviceByName("mobile") /orderFactLogRepo.countAllDevice() *100), "#4DD0E1"),
                        new UserDeviceDTO("Desktop",  ((double) orderFactLogRepo.countDeviceByName("desktop") /orderFactLogRepo.countAllDevice() *100), "#001F54"),
                        new UserDeviceDTO("Tablet",  ((double) orderFactLogRepo.countDeviceByName("tablet") /orderFactLogRepo.countAllDevice() *100), "#81C784")
                ));

        // Khu vực người dùng
        ArrayList<UserRegionDTO> userRegionDTOS =

                new ArrayList<>(List.of(
                        new UserRegionDTO("Miền Bắc", orderLogService.countUserrByRegion("Bắc"), (double) orderLogService.countUserrByRegion("Bắc") / orderFactLogRepo.countAllUserByRegion()*100),
                        new UserRegionDTO("Miền Trung", orderLogService.countUserrByRegion("Trung"), (double) orderLogService.countUserrByRegion("Trung") / orderFactLogRepo.countAllUserByRegion()*100),
                        new UserRegionDTO("Miền Nam", orderLogService.countUserrByRegion("Nam"), (double) orderLogService.countUserrByRegion("Nam") / orderFactLogRepo.countAllUserByRegion()*100)
                ));

        // Tổng hợp
        return new OverviewStatisticsDTO(
                monthlyRevenueDTOS,
                orderStatusDTOS,
                orderTimeDTOS,
                paymentMethodDTOS,
                productCategoryDTOS,
                productRatingDTOS,
                serviceQualityDTOS,
                userDeviceDTOS,
                userRegionDTOS,
                yearlyRevenueDTOS
        );
    }
}


