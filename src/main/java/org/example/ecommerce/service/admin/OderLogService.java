package org.example.ecommerce.service.admin;

import org.example.ecommerce.repository.CustomerRepository;
import org.example.ecommerce.repository.OderFactLogRepo;
import org.springframework.stereotype.Service;
import org.example.ecommerce.entity.OrderFactLog;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

@Service
public class OderLogService {
    private final OderFactLogRepo oderFactLogRepo;
    private  VisitService visitService;
    private final CustomerRepository customerRepository;

    public OderLogService(OderFactLogRepo oderFactLogRepo, VisitService visitService, CustomerRepository customerRepository) {
        this.oderFactLogRepo = oderFactLogRepo;
        this.visitService = visitService;
        this.customerRepository = customerRepository;
    }

    public void saveOderLog(OrderFactLog order){
        oderFactLogRepo.save(order);
    }
    public Optional<OrderFactLog> getOderFactLogById(int id){
        return oderFactLogRepo.findById(id);
    }

    public BigDecimal getSumProfitPromotion(){
        return oderFactLogRepo.sumRevenuePromotion();
    }
    public Integer countOrderPromotion(){
        return oderFactLogRepo.countOrderPromotion();
    }
    public Double getConversion(){
        return (double) oderFactLogRepo.countOrderFactLog() / visitService.getVisitHome();
    }


    public Integer getCustomerCount(){
        return customerRepository.countCustomer();
    }

    public BigDecimal getTotalProfitPromotionByMonth(int year, int month){
        YearMonth yearMonth = YearMonth.of(year, month);
        Instant start = yearMonth.atDay(1).atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant end = yearMonth.atEndOfMonth().atTime(LocalTime.MAX).atZone(ZoneOffset.UTC).toInstant();

        return oderFactLogRepo.sumRevenuePromotionByMonth(start, end);

    }
    public Integer getOderPromotionCountByMonth(int year, int month){
        YearMonth yearMonth = YearMonth.of(year, month);
        Instant start = yearMonth.atDay(1).atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant end = yearMonth.atEndOfMonth().atTime(LocalTime.MAX).atZone(ZoneOffset.UTC).toInstant();
        return oderFactLogRepo.countOrderPromotionByMonth(start,end);
    }

    public BigDecimal getDiscountAmountPromotionCountByMonth(int year, int month){
        YearMonth yearMonth = YearMonth.of(year, month);
        Instant start = yearMonth.atDay(1).atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant end = yearMonth.atEndOfMonth().atTime(LocalTime.MAX).atZone(ZoneOffset.UTC).toInstant();
        return oderFactLogRepo.sumDiscountAmountPromotionByMonth(start,end);
    }

    public Float getPercentTypePromotion(String type){
        return (oderFactLogRepo.getPercentageDiscountType(type)/oderFactLogRepo.getTotalPromotion())*100;
    }

    public Integer countOderByPromotionByID(int id){
        return oderFactLogRepo.countOrderPromotionById(id);
    }

    public BigDecimal sumRevenuePromotionById(int id){
        return oderFactLogRepo.sumRevenuePromotionByID(id);
    }

    public Double getConversionPromotionById(int id){
        return (double) oderFactLogRepo.countOrderPromotionById(id) / visitService.getVisitHome();
    }

    public List<OrderFactLog> getOrderFactLogByPromotionId(int id){
        return oderFactLogRepo.findByPromotionId(id);}

    public BigDecimal sumDiscountAmountPromotionById(int id){
        return oderFactLogRepo.sumDiscountPromotionByID(id);
    }

    public BigDecimal sumProfitAmountPromotionById(int id){
        return oderFactLogRepo.sumProfitPromotionByID(id);
    }


}
