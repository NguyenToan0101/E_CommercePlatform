package org.example.ecommerce.common.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TopProductDTO {
    private String name;
    private long value;
    private String category;


}

