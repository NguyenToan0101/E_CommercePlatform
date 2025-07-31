package org.example.ecommerce.service.seller;

import org.example.ecommerce.common.dto.seller.dashboard.*;
import org.example.ecommerce.entity.*;
import org.example.ecommerce.repository.*;
import org.example.ecommerce.repository.seller.ShopRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SellerDashboardServiceImpl implements SellerDashboardService {

    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private OrderItemsRepository orderItemsRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ShopRepo shopRepo;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public SellerDashboardDTO getDashboardData(Integer sellerId) {
        SellerDashboardDTO dashboardDTO = new SellerDashboardDTO();
        
        // Lấy shop của seller
        Shop shop = shopRepo.findBySelleridId(sellerId);
        if (shop == null) {
            return dashboardDTO;
        }
        
        Integer shopId = shop.getId();
        
        // 1. Dashboard Overview
        dashboardDTO.setOverview(getDashboardOverview(shopId));
        
        // 2. Monthly Revenue
        dashboardDTO.setMonthlyRevenue(getMonthlyRevenue(shopId));
        
        // 3. Category Revenue
        dashboardDTO.setCategoryRevenue(getCategoryRevenue(shopId));
        
        // 4. Best Selling Products
        dashboardDTO.setBestSellingProducts(getBestSellingProducts(shopId));
        
        // 5. Recent Orders
        dashboardDTO.setRecentOrders(getRecentOrders(shopId));
        
        return dashboardDTO;
    }

    @Override
    public SellerDashboardDTO getDashboardDataByShopId(Integer shopId) {
        SellerDashboardDTO dashboardDTO = new SellerDashboardDTO();
        
        if (shopId == null) {
            return dashboardDTO;
        }
        
        // 1. Dashboard Overview
        dashboardDTO.setOverview(getDashboardOverview(shopId));
        
        // 2. Monthly Revenue
        dashboardDTO.setMonthlyRevenue(getMonthlyRevenue(shopId));
        
        // 3. Category Revenue
        dashboardDTO.setCategoryRevenue(getCategoryRevenue(shopId));
        
        // 4. Best Selling Products
        dashboardDTO.setBestSellingProducts(getBestSellingProducts(shopId));
        
        // 5. Recent Orders
        dashboardDTO.setRecentOrders(getRecentOrders(shopId));
        
        return dashboardDTO;
    }

    private DashboardOverviewDTO getDashboardOverview(Integer shopId) {
        DashboardOverviewDTO overview = new DashboardOverviewDTO();
        
        // Tính tổng doanh thu
        BigDecimal totalRevenue = ordersRepository.sumRevenueByShopId(shopId);
        overview.setTotalRevenue(totalRevenue != null ? totalRevenue : BigDecimal.ZERO);
        
        // Tính tổng số đơn hàng
        Long totalOrders = ordersRepository.countOrdersByShopId(shopId);
        overview.setTotalOrders(totalOrders != null ? totalOrders : 0L);
        
        // Tính tổng số khách hàng
        Long totalCustomers = ordersRepository.countDistinctCustomersByShopId(shopId);
        overview.setTotalCustomers(totalCustomers != null ? totalCustomers : 0L);
        
        // Tính tổng số sản phẩm
        Long totalProducts = productRepository.countByShopidId(shopId);
        overview.setTotalProducts(totalProducts != null ? totalProducts : 0L);
        
        return overview;
    }

    private List<MonthlyRevenueDTO> getMonthlyRevenue(Integer shopId) {
        List<MonthlyRevenueDTO> monthlyRevenueList = new ArrayList<>();
        
        // Lấy dữ liệu doanh thu theo tháng trong năm hiện tại
        int currentYear = LocalDate.now().getYear();
        
        for (int month = 1; month <= 12; month++) {
            MonthlyRevenueDTO monthlyRevenue = new MonthlyRevenueDTO();
            monthlyRevenue.setMonth(String.valueOf(month));
            
            // Tính doanh thu cho tháng này
            BigDecimal revenue = ordersRepository.sumRevenueByShopIdAndMonth(shopId, currentYear, month);
            monthlyRevenue.setRevenue(revenue != null ? revenue : BigDecimal.ZERO);
            
            monthlyRevenueList.add(monthlyRevenue);
        }
        
        return monthlyRevenueList;
    }

    private List<CategoryRevenueDTO> getCategoryRevenue(Integer shopId) {
        List<CategoryRevenueDTO> categoryRevenueList = new ArrayList<>();
        
        // Lấy dữ liệu doanh thu theo danh mục
        List<Object[]> categoryRevenueData = ordersRepository.sumRevenueByCategoryAndShopId(shopId);
        
        BigDecimal totalRevenue = BigDecimal.ZERO;
        Map<String, BigDecimal> categoryRevenueMap = new HashMap<>();
        
        for (Object[] data : categoryRevenueData) {
            String categoryName = (String) data[0];
            BigDecimal revenue = (BigDecimal) data[1];
            categoryRevenueMap.put(categoryName, revenue);
            totalRevenue = totalRevenue.add(revenue);
        }
        
        // Tạo danh sách màu cho các danh mục
        String[] colors = {"#8B5CF6", "#10B981", "#F59E0B", "#EF4444", "#3B82F6"};
        int colorIndex = 0;
        
        for (Map.Entry<String, BigDecimal> entry : categoryRevenueMap.entrySet()) {
            CategoryRevenueDTO categoryRevenue = new CategoryRevenueDTO();
            categoryRevenue.setCategoryName(entry.getKey());
            categoryRevenue.setRevenue(entry.getValue());
            
            // Tính phần trăm
            if (totalRevenue.compareTo(BigDecimal.ZERO) > 0) {
                double percentage = entry.getValue().divide(totalRevenue, 4, BigDecimal.ROUND_HALF_UP)
                        .multiply(BigDecimal.valueOf(100)).doubleValue();
                categoryRevenue.setPercentage(percentage);
            } else {
                categoryRevenue.setPercentage(0.0);
            }
            
            categoryRevenue.setColor(colors[colorIndex % colors.length]);
            colorIndex++;
            
            categoryRevenueList.add(categoryRevenue);
        }
        
        return categoryRevenueList;
    }

    private List<BestSellingProductDTO> getBestSellingProducts(Integer shopId) {
        List<BestSellingProductDTO> bestSellingProducts = new ArrayList<>();
        
        // Lấy top 5 sản phẩm bán chạy nhất
        List<Object[]> productSalesData = orderItemsRepository.findTopSellingProductsByShopId(shopId, 5);
        
        for (Object[] data : productSalesData) {
            BestSellingProductDTO product = new BestSellingProductDTO();
            product.setProductName((String) data[0]);
            product.setProductImage((String) data[1]);
            product.setQuantitySold((Long) data[2]);
            product.setTotalRevenue((BigDecimal) data[3]);
            
            // Tính phần trăm thay đổi (giả lập)
            Random random = new Random();
            double percentageChange = random.nextDouble() * 25; // 0-25%
            product.setPercentageChange(percentageChange);
            
            bestSellingProducts.add(product);
        }
        
        return bestSellingProducts;
    }

    private List<RecentOrderDTO> getRecentOrders(Integer shopId) {
        List<RecentOrderDTO> recentOrders = new ArrayList<>();
        
        // Lấy 5 đơn hàng gần nhất
        List<Order> orders = ordersRepository.findRecentOrdersByShopId(shopId, 5);
        
        for (Order order : orders) {
            RecentOrderDTO recentOrder = new RecentOrderDTO();
            recentOrder.setOrderId("#" + order.getId());
            recentOrder.setCustomerName(order.getFullname());
            recentOrder.setAmount(order.getTotalamount());
            recentOrder.setStatus(order.getStatus());
            
            recentOrders.add(recentOrder);
        }
        
        return recentOrders;
    }
} 