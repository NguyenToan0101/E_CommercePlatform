package org.example.ecommerce.controller.seller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.ecommerce.dto.promotion.CreatePromotionRequest;
import org.example.ecommerce.entity.Customer;
import org.example.ecommerce.entity.Promotion;
import org.example.ecommerce.entity.Shop;
import org.example.ecommerce.service.seller.promotion.PromotionSellerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequestMapping("/seller/promotions")
@RequiredArgsConstructor
public class PromotionController {
    
    private static final int DEFAULT_PAGE_SIZE = 10;

    private final PromotionSellerService promotionSellerService;


    @GetMapping
    public String listPromotions(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "keyword", required = false) String keyword,
            HttpSession session,
            Model model) {
            
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null || customer.getSeller() == null || customer.getSeller().getShop() == null) {
            return "redirect:/login";
        }
        
        Shop shop = customer.getSeller().getShop();
        
        try {
            // Lấy danh sách khuyến mãi
            List<Promotion> promotions = promotionSellerService.getPromotionsByShop(shop);
            
            // Lọc theo trạng thái nếu có
            if (status != null && !status.isEmpty()) {
                promotions = promotions.stream()
                        .filter(p -> p.getStatus().equalsIgnoreCase(status))
                        .collect(Collectors.toList());
            }
            
            // Tìm kiếm theo từ khóa nếu có
            if (keyword != null && !keyword.isEmpty()) {
                String lowerCaseKeyword = keyword.toLowerCase();
                promotions = promotions.stream()
                        .filter(p -> p.getName().toLowerCase().contains(lowerCaseKeyword) ||
                                   p.getCode().toLowerCase().contains(lowerCaseKeyword))
                        .collect(Collectors.toList());
            }
            
            // Phân trang
            Pageable pageable = PageRequest.of(page, size, Sort.by("startdate").descending());
            int start = (int) pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), promotions.size());
            
            Page<Promotion> promotionPage = new org.springframework.data.domain.PageImpl<>(
                    promotions.subList(start, end), 
                    pageable, 
                    promotions.size()
            );
            
            model.addAttribute("promotions", promotionPage);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", promotionPage.getTotalPages());
            model.addAttribute("totalItems", promotionPage.getTotalElements());
            model.addAttribute("activePage", "promotions"); // Thêm activePage cho sidebar
            
            // Tính toán stats cho từng trạng thái
            List<Promotion> allPromotions = promotionPage.getContent();
            long activeCount = allPromotions.stream().filter(p -> "ACTIVE".equals(p.getStatus())).count();
            long scheduledCount = allPromotions.stream().filter(p -> "SCHEDULED".equals(p.getStatus())).count();
            long pausedCount = allPromotions.stream().filter(p -> "PAUSED".equals(p.getStatus())).count();
            long expiredCount = allPromotions.stream().filter(p -> "EXPIRED".equals(p.getStatus())).count();
            
            model.addAttribute("activeCount", activeCount);
            model.addAttribute("scheduledCount", scheduledCount);
            model.addAttribute("pausedCount", pausedCount);
            model.addAttribute("expiredCount", expiredCount);
            
            // Tạo danh sách số trang để hiển thị
            int totalPages = promotionPage.getTotalPages();
            if (totalPages > 0) {
                List<Integer> pageNumbers = IntStream.rangeClosed(0, totalPages - 1)
                        .boxed()
                        .collect(Collectors.toList());
                model.addAttribute("pageNumbers", pageNumbers);
            }
            
            // Thêm các tham số tìm kiếm để giữ lại trên phân trang
            model.addAttribute("status", status);
            model.addAttribute("keyword", keyword);
            
            return "seller/promotions/list";
            
        } catch (Exception e) {
            model.addAttribute("error", "Có lỗi xảy ra khi tải danh sách khuyến mãi: " + e.getMessage());
            model.addAttribute("activePage", "promotions"); // Thêm activePage cho sidebar
            
            // Thêm stats mặc định khi có lỗi
            model.addAttribute("activeCount", 0);
            model.addAttribute("scheduledCount", 0);
            model.addAttribute("pausedCount", 0);
            model.addAttribute("expiredCount", 0);
            
            return "seller/promotions/list";
        }
    }
    

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("promotion", new CreatePromotionRequest());
        model.addAttribute("activePage", "promotions"); // Thêm activePage cho sidebar
        return "seller/promotions/create";
    }

    @PostMapping
    public String createPromotion(
            @Valid @ModelAttribute("promotion") CreatePromotionRequest request,
            BindingResult result,
            HttpSession session,
            Model model) {

        // Check if user is logged in and get shop ID
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null || customer.getSeller() == null || customer.getSeller().getShop() == null) {
            return "redirect:/login";
        }

        Shop shopId = customer.getSeller().getShop();

        // Validate request
        if (result.hasErrors()) {
            return "seller/promotions/create";
        }

        // Validate at least one of categoryIds or productIds is provided
        if ((request.getCategoryIds() == null || request.getCategoryIds().isEmpty()) &&
            (request.getProductIds() == null || request.getProductIds().isEmpty())) {
            model.addAttribute("error", "Bạn phải chọn ít nhất một ngành hàng hoặc sản phẩm");
            return "seller/promotions/create";
        }

        try {
            Promotion createdPromotion = promotionSellerService.createPromotion(request, shopId);
            return "redirect:/seller/promotions";
        } catch (Exception e) {
            model.addAttribute("error", "Có lỗi xảy ra khi tạo khuyến mãi: " + e.getMessage());
            return "seller/promotions/create";
        }
    }
    

}
