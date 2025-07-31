package org.example.ecommerce.common.dto.analysis;


import java.util.ArrayList;

public record OverviewStatisticsDTO(
        ArrayList<MonthlyRevenueDTO> monthlyRevenueDTOS,
        ArrayList<OrderStatusDTO> orderStatusDTOS,
        ArrayList<OrderTimeDTO> orderTimeDTOS,
        ArrayList<PaymentMethodDTO> paymentMethodDTOS,
        ArrayList<ProductCategoryDTO> productCategoryDTOS,
        ArrayList<ProductRatingDTO> productRatingDTOS,
        ArrayList<ServiceQualityDTO> serviceQualityDTOS,
        ArrayList<UserDeviceDTO> userDeviceDTOS,
        ArrayList<UserRegionDTO> userRegionDTOS,
        ArrayList<YearlyRevenueDTO> yearlyRevenueDTOS) {}
