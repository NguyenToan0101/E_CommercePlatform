package org.example.ecommerce.service.customer.coin;

import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class BauCuaResultRequest {
    private List<String> result; // Kết quả 3 xúc xắc: ["bau", "cua", "tom"]
    private Map<String, Integer> bets; // Các con vật đặt cược: {"bau": 100, "cua": 50}
    private int netResult; // Kết quả thắng/thua tổng cộng
}