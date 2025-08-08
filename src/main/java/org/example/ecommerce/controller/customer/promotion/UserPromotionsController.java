package org.example.ecommerce.controller.customer.promotion;

import org.example.ecommerce.common.dto.promotion.PromotionDTO;
import org.example.ecommerce.entity.Customer;
import org.example.ecommerce.service.customer.promotion.UserPromotionsService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.HashMap;
import java.util.Map;

import java.util.List;

@Controller
public class UserPromotionsController {

    @Autowired
    private UserPromotionsService userPromotionsService;


    @GetMapping("/promotions")
    public String showPromotions(HttpSession session, Model model) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            return "redirect:/login";
        }

        // Lấy tất cả promotions có sẵn
        List<PromotionDTO> availablePromotions = userPromotionsService.getAvailablePromotions(customer);
        
        // Lấy promotions đã sử dụng
        List<PromotionDTO> usedPromotions = userPromotionsService.getUsedPromotions(customer);

        model.addAttribute("customer", customer);
        model.addAttribute("availablePromotions", availablePromotions);
        model.addAttribute("usedPromotions", usedPromotions);
        model.addAttribute("totalAvailable", availablePromotions.size());
        model.addAttribute("totalUsed", usedPromotions.size());

        return "customer/promotion/user_promotions";
    }

    @GetMapping("/promotions/type/{type}")
    public String showPromotionsByType(@PathVariable String type, 
                                     HttpSession session, 
                                     Model model) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            return "redirect:/login";
        }

        List<PromotionDTO> promotions = userPromotionsService.getPromotionsByType(type);

        model.addAttribute("customer", customer);
        model.addAttribute("promotions", promotions);
        model.addAttribute("type", type);
        model.addAttribute("totalPromotions", promotions.size());

        return "customer/promotion/user_promotions";
    }


    @GetMapping("/promotions/{id}")
    public String showPromotionDetail(@PathVariable Integer id, 
                                    HttpSession session, 
                                    Model model) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            return "redirect:/login";
        }

        PromotionDTO promotion = userPromotionsService.getPromotionById(id);
        if (promotion == null) {
            model.addAttribute("error", "Không tìm thấy promotion");
            return "redirect:/promotions";
        }

        // Kiểm tra xem user có thể sử dụng promotion không
        boolean canUse = userPromotionsService.canUsePromotion(customer, id);
        int usageCount = userPromotionsService.getUsageCountByUser(customer, id);

        model.addAttribute("customer", customer);
        model.addAttribute("promotion", promotion);
        model.addAttribute("canUse", canUse);
        model.addAttribute("usageCount", usageCount);

        return "customer/promotion/promotion_detail";
    }

    /**
     * Tìm kiếm promotions
     */
    @GetMapping("/promotions/search")
    public String searchPromotions(@RequestParam String keyword, 
                                 HttpSession session, 
                                 Model model) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null) {
            return "redirect:/login";
        }

        // Lấy tất cả promotions và filter theo keyword
        List<PromotionDTO> allPromotions = userPromotionsService.getAvailablePromotions(customer);
        List<PromotionDTO> filteredPromotions = allPromotions.stream()
            .filter(promotion -> 
                promotion.getName().toLowerCase().contains(keyword.toLowerCase()) ||
                promotion.getDescription().toLowerCase().contains(keyword.toLowerCase()) ||
                promotion.getCode().toLowerCase().contains(keyword.toLowerCase())
            )
            .toList();

        model.addAttribute("customer", customer);
        model.addAttribute("promotions", filteredPromotions);
        model.addAttribute("keyword", keyword);
        model.addAttribute("totalResults", filteredPromotions.size());

                    return "customer/promotion/user_promotions";
        }

        /**
         * Hiển thị kho voucher của user
         */
        @GetMapping("/promotions/my-vouchers")
        public String showMyVouchers(HttpSession session, Model model) {
            Customer customer = (Customer) session.getAttribute("customer");
            if (customer == null) {
                return "redirect:/login";
            }

            // Lấy promotions đã lưu của user
            List<PromotionDTO> myVouchers = userPromotionsService.getUsedPromotions(customer);

            model.addAttribute("customer", customer);
            model.addAttribute("availablePromotions", myVouchers);
            model.addAttribute("totalAvailable", myVouchers.size());
            model.addAttribute("totalUsed", 0);
            model.addAttribute("type", "MY_VOUCHERS");

            return "customer/promotion/user_promotions";
        }

        /**
         * Lưu promotion vào kho voucher của user
         */
        @PostMapping("/promotions/save")
        @ResponseBody
        public Map<String, Object> savePromotion(@RequestBody Map<String, Object> request,
                                                HttpSession session) {
            Map<String, Object> response = new HashMap<>();
            
            Customer customer = (Customer) session.getAttribute("customer");
            if (customer == null) {
                response.put("success", false);
                response.put("message", "Vui lòng đăng nhập để lưu mã");
                return response;
            }

            try {
                Integer promotionId = Integer.parseInt(request.get("promotionId").toString());
                String code = request.get("code").toString();

                // Kiểm tra xem user có thể sử dụng promotion không
                if (!userPromotionsService.canUsePromotion(customer, promotionId)) {
                    response.put("success", false);
                    response.put("message", "Bạn đã sử dụng hết lượt cho mã này");
                    return response;
                }

                // Lưu promotion vào Userpromotion
                boolean saved = userPromotionsService.saveUserPromotion(customer, promotionId);
                
                if (saved) {
                    response.put("success", true);
                    response.put("message", "Đã lưu mã thành công");
                } else {
                    response.put("success", false);
                    response.put("message", "Không thể lưu mã. Vui lòng thử lại");
                }
                
            } catch (Exception e) {
                response.put("success", false);
                response.put("message", "Có lỗi xảy ra: " + e.getMessage());
            }
            
            return response;
        }
    } 