package org.example.ecommerce.service.admin;

import org.example.ecommerce.common.dto.analysis.MonthlyRevenueDTO;
import org.example.ecommerce.common.dto.analysis.OrderTimeDTO;
import org.example.ecommerce.common.dto.analysis.ProductCategoryDTO;
import org.example.ecommerce.common.dto.analysis.YearlyRevenueDTO;
import org.example.ecommerce.common.dto.dashboard.QuarterlyRevenueDTO;
import org.example.ecommerce.repository.CustomerRepository;
import org.example.ecommerce.repository.OderFactLogRepo;
import org.example.ecommerce.repository.OrdersRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.example.ecommerce.entity.OrderFactLog;

import java.awt.print.Pageable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

@Service
public class OderLogService {
    private final OderFactLogRepo oderFactLogRepo;
    private final VisitService visitService;
    private final CustomerRepository customerRepository;
    private final OrdersRepository ordersRepository;

    public OderLogService(OderFactLogRepo oderFactLogRepo, VisitService visitService, CustomerRepository customerRepository, OrdersRepository ordersRepository) {
        this.oderFactLogRepo = oderFactLogRepo;
        this.visitService = visitService;
        this.customerRepository = customerRepository;
        this.ordersRepository = ordersRepository;
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

    public Integer countPromtionUsedByCustomerid(int id, int promotionid){
        return oderFactLogRepo.countPromotionUsedByCustomerid(id,promotionid);
    }


    //Dashboard Revenue Admin
    public BigDecimal sumRevenueByMonth(int year, int month){
        YearMonth yearMonth = YearMonth.of(year, month);
        Instant start = yearMonth.atDay(1).atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant end = yearMonth.atEndOfMonth().atTime(LocalTime.MAX).atZone(ZoneOffset.UTC).toInstant();
        return oderFactLogRepo.sumRevenueByMonth(start,end);
    }

    public BigDecimal sumRevenueByRegion(String region){
        return oderFactLogRepo.sumRevenueByRegion(region);
    }
    public Integer countUserrByRegion(String region){
        return oderFactLogRepo.countUserByRegion(region);
    }


//    public String convertQuarterAndYear(LocalDate localDate){
//        return switch (localDate.getMonth()){
//            case JANUARY,FEBRUARY, MARCH-> "Q1 " + localDate.getYear();
//
//            case APRIL ,MAY,JUNE-> "Q2 " + localDate.getYear();
//
//            case JULY ,AUGUST,SEPTEMBER-> "Q3 " + localDate.getYear();
//
//            default -> "Q4 " + localDate.getYear();
//
//        };
//    }
    public ArrayList<QuarterlyRevenueDTO> quarterlyRevenueDTOArrayList(){
        List<Object[]> rawData = oderFactLogRepo.getQuarterRevenueRaw();

        ArrayList<QuarterlyRevenueDTO> dtos = new ArrayList<>();
        BigDecimal previousRevenue = null;

        for (Object[] r : rawData) {
            String quarter = (String) r[0];
            BigDecimal revenue = (BigDecimal) r[1];
            long orders = ((Number) r[2]).longValue();
            BigDecimal avgOrder = (BigDecimal) r[3];

            QuarterlyRevenueDTO dtoQuarter = new QuarterlyRevenueDTO(quarter, revenue, orders, avgOrder);

            // Tính tăng trưởng so với quý trước
            if (previousRevenue != null && previousRevenue.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal growth = revenue.subtract(previousRevenue)
                        .divide(previousRevenue, 4, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100));
                dtoQuarter.setGrowth(growth.doubleValue());
            } else {
                dtoQuarter.setGrowth(0);
            }

            previousRevenue = revenue;
            dtos.add(dtoQuarter);
        }
        return dtos;
    }

    public BigDecimal sumRevenueByAddress(String address){
        return  ordersRepository.sumRevenueByAddress(address);
    }

    public Integer countOrderByAddress(String address){
        return  ordersRepository.countOrderByAddress(address);
    }

    public BigDecimal sumDifferenceRevenueByAddress(){
        return ordersRepository.sumDifferenceRevenueByAddress().subtract(
                ordersRepository.sumRevenueByAddress("Thành phố Hà Nội")
                        .add(ordersRepository.sumRevenueByAddress("Thành phố Hồ Chí Minh"))
                        .add(ordersRepository.sumRevenueByAddress("Thành phố Đà Nẵng"))
                        .add(ordersRepository.sumRevenueByAddress("Thành phố Cần Thơ"))
                        .add(ordersRepository.sumRevenueByAddress("Thành phố Hải Phòng"))



        );
    }

    public Integer countDifferenceCountByAddress(){
        return ordersRepository.countDifferenceOrderByAddress() -
                ordersRepository.countOrderByAddress("Thành phố Hà Nội") -
                ordersRepository.countOrderByAddress("Thành phố Hồ Chí Minh") -
                ordersRepository.countOrderByAddress("Thành phố Đà Nẵng") -
                ordersRepository.countOrderByAddress("Thành phố Cần Thơ")-
                ordersRepository.countOrderByAddress("Thành phố Hải Phòng");
    }



    //DashBoard User
    public Integer countMaleByAge(int ageTo, int ageFrom){
        System.out.println("Male---" + oderFactLogRepo.countMaleByAge(LocalDate.now().minusYears(ageFrom), LocalDate.now().minusYears(ageTo)));
        return oderFactLogRepo.countMaleByAge(LocalDate.now().minusYears(ageFrom), LocalDate.now().minusYears(ageTo));
    }

    public Integer countFemaleByAge(int ageTo, int ageFrom){
        System.out.println("Female----" + oderFactLogRepo.countFemaleByAge(LocalDate.now().minusYears(ageFrom), LocalDate.now().minusYears(ageTo)));
        return oderFactLogRepo.countFemaleByAge(LocalDate.now().minusYears(ageFrom), LocalDate.now().minusYears(ageTo));
    }

    public Integer countCustomerByAge(int ageTo, int ageFrom){
        System.out.println("All----" + oderFactLogRepo.countCustomerByAge(LocalDate.now().minusYears(ageFrom), LocalDate.now().minusYears(ageTo)));
        return oderFactLogRepo.countCustomerByAge(LocalDate.now().minusYears(ageFrom), LocalDate.now().minusYears(ageTo));
    }
    //Dashboard top product
    public List<Object[]> topProductSelling(){
        PageRequest pageRequest = PageRequest.of(0, 8);
        return oderFactLogRepo.getTopSellingProductIds( pageRequest);
    }


    //Statistics
    public ArrayList<YearlyRevenueDTO> getYearlyRevenue() {
        List<Object[]> rawData = oderFactLogRepo.getYearlyRevenueRaw();
        ArrayList<YearlyRevenueDTO> result = new ArrayList<>();

        for (Object[] row : rawData) {
            BigDecimal yearValue = (BigDecimal) row[0];
            String year = yearValue.toString();
//            String year = String.valueOf(((Double) row[0]).doubleValue()); // EXTRACT trả về Double
            BigDecimal revenue = (BigDecimal) row[1];
            BigDecimal profit = (BigDecimal) row[2];
            BigDecimal cost = (BigDecimal) row[3];

            result.add(new YearlyRevenueDTO(year, revenue, profit, cost));
        }

        return result;
    }

    public ArrayList<MonthlyRevenueDTO> getMonthlyRevenue() {
        List<Object[]> rawData = oderFactLogRepo.getMonthlyRevenueRaw();
        ArrayList<MonthlyRevenueDTO> result = new ArrayList<>();

        for (Object[] row : rawData) {
//            String month = String.valueOf(((Double) row[0]).intValue()); // EXTRACT trả về Double
            BigDecimal monthValue = (BigDecimal) row[0];
            String month = String.valueOf(monthValue.intValue());

            BigDecimal revenue = (BigDecimal) row[1];
            BigDecimal profit = (BigDecimal) row[2];
            BigDecimal cost = (BigDecimal) row[3];

            result.add(new MonthlyRevenueDTO(month, revenue, profit, cost));
        }

        return result;
    }

    public ArrayList<OrderTimeDTO> getOrderTimeStatistics() {
        List<Object[]> raw = oderFactLogRepo.getOrderCountByTimeRange();
        ArrayList<OrderTimeDTO> result = new ArrayList<>();

        for (Object[] row : raw) {
            String time = (String) row[0];
            int orders = ((Number) row[1]).intValue();

            result.add(new OrderTimeDTO(time, orders));
        }

        return result;
    }

    public ArrayList<ProductCategoryDTO> getProductCategoryStatistics() {
        List<Object[]> raw = oderFactLogRepo.getProductCategoryStats();
        ArrayList<ProductCategoryDTO> result = new ArrayList<>();

        for (Object[] row : raw) {
            String category = (String) row[0];
            int products = ((Number) row[1]).intValue();
            int sales = ((Number) row[2]).intValue();
            BigDecimal revenue = (BigDecimal) row[3];

            result.add(new ProductCategoryDTO(category, products, sales, revenue));
        }

        return result;
    }





}
