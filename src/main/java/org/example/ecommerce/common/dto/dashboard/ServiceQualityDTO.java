package org.example.ecommerce.common.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceQualityDTO {
    private String metric;
    private int score;
    private int fullMark;


}

