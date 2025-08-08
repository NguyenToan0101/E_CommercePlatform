package org.example.ecommerce.service.customer.rank;

import org.example.ecommerce.entity.CustomerRank;

import java.util.List;

public interface CustomerRankService {
    
    CustomerRank getCustomerRank(Integer customerId);
    
    List<CustomerRank> getAllRanks();
    
    List<CustomerRank> getRanksByType(String rankType);
    
    void calculateAndUpdateRank(Integer customerId);
    
    String getRankTypeByPoints(Integer points);
    
    Integer getPointsBySpent(Double totalSpent, Integer orderCount);
}
