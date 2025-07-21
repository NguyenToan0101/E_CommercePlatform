package org.example.ecommerce.common.dto.admin.complaintManagement;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PaymentInfo {
    private Integer paymentId;
    private BigDecimal amount;
    private String method;
    private String status;
    private String transactionId;
    private String gateway;
    private LocalDateTime paidAt;
}
