package org.example.ecommerce.common.dto.dashboard;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OverviewDashboardDTO {


    private ArrayList<QuarterlyRevenueDTO> quarterlyRevenueDTOList;
    private ArrayList<RegionPerformanceDTO> regionPerformanceDTOList;
    private ArrayList<UserDemographicsDTO> userDemographicsDTOList;
    private ArrayList<ConversionFunnelDTO> conversionFunnelDTOList;
    private ArrayList<TopProductDTO> topProductDTOList;
    private ArrayList<ServiceQualityDTO> serviceQualityDTOList;
    private ArrayList<RealTimeActivityDTO> realTimeActivityDTOList;
}
