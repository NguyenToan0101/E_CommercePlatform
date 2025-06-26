package org.example.ecommerce.controller.customer.review;

import org.example.ecommerce.entity.Order;
import org.example.ecommerce.entity.Product;
import org.example.ecommerce.entity.Review;
import org.example.ecommerce.service.customer.review.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/review")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @GetMapping
    public String showReviewForm(@ModelAttribute("productId") Product productId,
                                 @ModelAttribute("orderId") Order orderId,
                                 Model model) {
        Review review = new Review();
        review.setProductid(productId);
        review.setOrderid(orderId);
        model.addAttribute("review", review);
        return "customer/review/form";
    }


    @PostMapping
    public String submitReview(@ModelAttribute("review") Review review,
                               RedirectAttributes redirectAttributes) {
        if (reviewService.submitReview(review)) {
            redirectAttributes.addFlashAttribute("message", "Đánh giá sản phẩm thành công");
            return "redirect:/review";
        } else {
            redirectAttributes.addFlashAttribute("error","Bạn đã đánh giá sản phẩm này trong đơn hàng này rồi");
            return "redirect:/review";
        }
    }
}
