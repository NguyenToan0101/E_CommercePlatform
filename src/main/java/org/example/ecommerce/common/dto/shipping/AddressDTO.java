package org.example.ecommerce.common.dto.shipping;



import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class AddressDTO {
    private String provinceCode;
    private String provinceName;
    private String districtCode;
    private String districtName;
    private String wardName;

    // Getters và Setters
}


