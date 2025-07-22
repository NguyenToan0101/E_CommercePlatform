package org.example.ecommerce.controller.seller.review;

import jakarta.servlet.http.HttpSession;
import org.example.ecommerce.common.dto.seller.SellerReviewDTO;
import org.example.ecommerce.entity.Customer;
import org.example.ecommerce.entity.Seller;
import org.example.ecommerce.entity.Product;
import org.example.ecommerce.repository.ProductRepository;
import org.example.ecommerce.service.seller.review.SellerReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;
import java.util.Arrays;

@Controller
@RequestMapping("/seller/reviews")
public class SellerReviewController {
    @Autowired
    private SellerReviewService sellerReviewService;

    @Autowired
    private ProductRepository productRepository;

    @GetMapping
    public String listReviews(HttpSession session,
                             @RequestParam(value = "productId", required = false) Integer productId,
                             @RequestParam(value = "rating", required = false) Integer rating,
                             @RequestParam(value = "hasImage", required = false) Boolean hasImage,
                             @RequestParam(value = "fromDate", required = false) String fromDate,
                             @RequestParam(value = "toDate", required = false) String toDate,
                             Model model) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null || customer.getSeller() == null) {
            return "redirect:/login";
        }
        Seller seller = customer.getSeller();
        Integer sellerId = seller.getId();
        List<SellerReviewDTO> reviews = sellerReviewService.getReviewsForSeller(sellerId, productId, rating, hasImage, fromDate, toDate);
        // Lấy danh sách sản phẩm của shop để filter
        List<Product> products = productRepository.findByShopidId(sellerId);
        // Lấy thống kê tổng quan review
        var stats = sellerReviewService.getReviewStatsForSeller(sellerId, productId);
        model.addAttribute("reviews", reviews);
        model.addAttribute("products", products);
        model.addAttribute("stats", stats);
        model.addAttribute("starList", java.util.Arrays.asList(5,4,3,2,1));
        return "seller/review/list";
    }

    @GetMapping("/{reviewId}")
    public String reviewDetail(HttpSession session, @PathVariable Integer reviewId, Model model) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null || customer.getSeller() == null) {
            return "redirect:/login";
        }
        Seller seller = customer.getSeller();
        Integer sellerId = seller.getId();
        // Lấy review theo id, kiểm tra có thuộc shop seller không
        SellerReviewDTO review = sellerReviewService.getReviewDetailForSeller(sellerId, reviewId);
        if (review == null) {
            return "redirect:/seller/reviews?notfound";
        }
        String replyContent = sellerReviewService.getReplyContentByReviewId(reviewId);
        model.addAttribute("review", review);
        model.addAttribute("replyContent", replyContent);
        return "seller/review/detail";
    }

    @PostMapping("/{reviewId}/reply")
    public String replyToReview(HttpSession session, @PathVariable Integer reviewId, @ModelAttribute("replyContent") String replyContent) {
        Customer customer = (Customer) session.getAttribute("customer");
        if (customer == null || customer.getSeller() == null) {
            return "redirect:/login";
        }
        Seller seller = customer.getSeller();
        sellerReviewService.replyToReview(seller.getId(), reviewId, replyContent);
        return "redirect:/seller/reviews/" + reviewId;
    }
} 