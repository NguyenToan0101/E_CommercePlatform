package org.example.ecommerce.controller.customer.review;

import jakarta.servlet.http.HttpSession;
import org.example.ecommerce.entity.*;
import org.example.ecommerce.repository.ReviewsImageRepository;
import org.example.ecommerce.service.customer.review.ReviewService;
import org.example.ecommerce.service.UploadImageFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.io.IOException;

@Controller
@RequestMapping("/review")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;
    @Autowired
    private UploadImageFile uploadImageFile;
    @Autowired
    private ReviewsImageRepository reviewsImageRepository;

    @GetMapping
    public String showReviewForm(@ModelAttribute("productId") Product productId,
                                 @ModelAttribute("orderItemsId") Orderitem orderitemsId,
                                 Model model, HttpSession session) {
        Customer customer = (Customer) session.getAttribute("customer");
        Review review = new Review();
        review.setProductid(productId);
        review.setOrderitemid(orderitemsId);
        model.addAttribute("review", review);
        model.addAttribute("customer", customer);
        return "customer/review/form";
    }


    @PostMapping
    public String submitReview(@ModelAttribute("review") Review review,
                               @RequestParam(value = "mediaFiles", required = false) MultipartFile[] mediaFiles,
                               RedirectAttributes redirectAttributes) {
        if (reviewService.submitReview(review, mediaFiles)) {
            redirectAttributes.addFlashAttribute("message", "Đánh giá sản phẩm thành công");
            return "redirect:/review";
        } else {
            redirectAttributes.addFlashAttribute("error","Bạn đã đánh giá sản phẩm này trong đơn hàng này rồi");
            return "redirect:/review";
        }
    }
}
