package org.example.ecommerce.common.dto.dashboard;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OverviewDashboardDTO {


    private ArrayList<QuarterlyRevenueDTO> quarterlyRevenueDTOList;
    private ArrayList<RegionPerformanceDTO> regionPerformanceDTOList;
    private ArrayList<UserDemographicsDTO> userDemographicsDTOList;
    private ArrayList<ConversionFunnelDTO> conversionFunnelDTOList;
    private Set<TopProductDTO> topProductDTOList;
    private ArrayList<ServiceQualityDTO> serviceQualityDTOList;
    private ArrayList<RealTimeActivityDTO> realTimeActivityDTOList;
}
