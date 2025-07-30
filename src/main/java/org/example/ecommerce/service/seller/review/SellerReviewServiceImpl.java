package org.example.ecommerce.service.seller.review;

import org.example.ecommerce.common.dto.seller.SellerReviewDTO;
import org.example.ecommerce.entity.*;
import org.example.ecommerce.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;
import org.example.ecommerce.common.dto.seller.SellerReviewStatsDTO;

@Service
public class SellerReviewServiceImpl implements SellerReviewService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private ReviewsImageRepository reviewsImageRepository;

    @Autowired
    private org.example.ecommerce.repository.seller.ShopRepo shopRepo;

    @Autowired
    private org.example.ecommerce.repository.ReviewReplyRepository reviewReplyRepository;

    @Override
    public List<SellerReviewDTO> getReviewsForSeller(Integer sellerId, Integer productId, Integer rating, Boolean hasImage, String fromDate, String toDate) {
        // Lấy shopId từ sellerId
        Integer shopId = shopRepo.findById(sellerId).map(shop -> shop.getId()).orElse(null);
        if (shopId == null) return Collections.emptyList();
        List<Product> products = productRepository.findByShopidId(shopId);
        if (products.isEmpty()) return Collections.emptyList();

        List<Integer> productIds = products.stream().map(Product::getId).collect(Collectors.toList());
        List<Review> reviews;
        if (productId != null && productIds.contains(productId)) {
            reviews = reviewRepository.findAllByProductidOrderByCreatedatDesc(products.stream().filter(p -> p.getId().equals(productId)).findFirst().get());
        } else {
            // Lấy tất cả review của các sản phẩm thuộc shop
            reviews = productIds.stream()
                    .map(pid -> reviewRepository.findAllByProductidOrderByCreatedatDesc(products.stream().filter(p -> p.getId().equals(pid)).findFirst().get()))
                    .flatMap(List::stream)
                    .collect(Collectors.toList());
        }
        // Filter theo rating
        if (rating != null) {
            reviews = reviews.stream().filter(r -> Objects.equals(r.getRating(), rating)).collect(Collectors.toList());
        }
        // Filter theo hasImage
        if (hasImage != null && hasImage) {
            reviews = reviews.stream().filter(r -> r.getReviewsImages() != null && !r.getReviewsImages().isEmpty()).collect(Collectors.toList());
        }
        // Filter theo ngày
        if (fromDate != null && !fromDate.isEmpty()) {
            Instant from = LocalDate.parse(fromDate).atStartOfDay(ZoneId.systemDefault()).toInstant();
            reviews = reviews.stream().filter(r -> r.getCreatedat() != null && r.getCreatedat().isAfter(from)).collect(Collectors.toList());
        }
        if (toDate != null && !toDate.isEmpty()) {
            Instant to = LocalDate.parse(toDate).plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant();
            reviews = reviews.stream().filter(r -> r.getCreatedat() != null && r.getCreatedat().isBefore(to)).collect(Collectors.toList());
        }
        // Map sang DTO
        return reviews.stream().map(r -> {
            Product p = r.getProductid();
            String productImage = (p.getProductimages() != null && !p.getProductimages().isEmpty()) ? p.getProductimages().iterator().next().getImageurl() : null;
            List<String> reviewImages = r.getReviewsImages() != null ? r.getReviewsImages().stream().map(ReviewsImage::getImageUrl).collect(Collectors.toList()) : Collections.emptyList();
            String customerName = (r.getOrderitemsid() != null && r.getOrderitemsid().getOrderid() != null && r.getOrderitemsid().getOrderid().getCustomerid() != null)
                    ? r.getOrderitemsid().getOrderid().getCustomerid().getFirstname() : "Ẩn danh";
            int safeRating = r.getRating() != null ? Math.min(r.getRating(), 5) : 0;
            System.out.println("DEBUG rating: " + safeRating);
            return new SellerReviewDTO(
                    r.getId(),
                    p.getId(),
                    p.getName(),
                    productImage,
                    safeRating,
                    r.getComment(),
                    reviewImages,
                    r.getCreatedat(),
                    customerName
            );
        }).collect(Collectors.toList());
    }

    @Override
    public SellerReviewDTO getReviewDetailForSeller(Integer sellerId, Integer reviewId) {
        Integer shopId = shopRepo.findById(sellerId).map(shop -> shop.getId()).orElse(null);
        if (shopId == null) return null;
        List<Product> products = productRepository.findByShopidId(shopId);
        List<Integer> productIds = products.stream().map(Product::getId).toList();
        Optional<Review> reviewOpt = reviewRepository.findById(reviewId);
        if (reviewOpt.isEmpty()) return null;
        Review r = reviewOpt.get();
        if (r.getProductid() == null || !productIds.contains(r.getProductid().getId())) return null;
        Product p = r.getProductid();
        String productImage = (p.getProductimages() != null && !p.getProductimages().isEmpty()) ? p.getProductimages().iterator().next().getImageurl() : null;
        List<String> reviewImages = r.getReviewsImages() != null ? r.getReviewsImages().stream().map(ReviewsImage::getImageUrl).toList() : List.of();
        String customerName = (r.getOrderitemsid() != null && r.getOrderitemsid().getOrderid() != null && r.getOrderitemsid().getOrderid().getCustomerid() != null)
                ? r.getOrderitemsid().getOrderid().getCustomerid().getFirstname() : "Ẩn danh";
        int safeRating = r.getRating() != null ? Math.min(r.getRating(), 5) : 0;
        System.out.println("DEBUG rating (detail): " + safeRating);
        return new SellerReviewDTO(
                r.getId(),
                p.getId(),
                p.getName(),
                productImage,
                safeRating,
                r.getComment(),
                reviewImages,
                r.getCreatedat(),
                customerName
        );
    }

    @Override
    public SellerReviewStatsDTO getReviewStatsForSeller(Integer sellerId, Integer productId) {
        Integer shopId = shopRepo.findById(sellerId).map(shop -> shop.getId()).orElse(null);
        if (shopId == null) return new SellerReviewStatsDTO(0, 0, Map.of(), 0);
        List<Product> products = productRepository.findByShopidId(shopId);
        if (products.isEmpty()) return new SellerReviewStatsDTO(0, 0, Map.of(), 0);
        List<Review> reviews;
        if (productId != null) {
            reviews = products.stream()
                    .filter(p -> p.getId().equals(productId))
                    .findFirst()
                    .map(p -> reviewRepository.findAllByProductidOrderByCreatedatDesc(p))
                    .orElse(List.of());
        } else {
            reviews = products.stream()
                    .map(p -> reviewRepository.findAllByProductidOrderByCreatedatDesc(p))
                    .flatMap(List::stream)
                    .toList();
        }
        int total = reviews.size();
        double avg = total == 0 ? 0 : reviews.stream().mapToInt(r -> r.getRating() != null ? r.getRating() : 0).average().orElse(0);
        Map<Integer, Integer> starDist = new java.util.HashMap<>();
        for (int i = 1; i <= 5; i++) starDist.put(i, 0);
        for (Review r : reviews) {
            int star = r.getRating() != null ? r.getRating() : 0;
            if (star >= 1 && star <= 5) starDist.put(star, starDist.get(star) + 1);
        }
        int withImg = (int) reviews.stream().filter(r -> r.getReviewsImages() != null && !r.getReviewsImages().isEmpty()).count();
        return new SellerReviewStatsDTO(total, Math.round(avg * 100.0) / 100.0, starDist, withImg);
    }

    @Override
    public void replyToReview(Integer sellerId, Integer reviewId, String replyContent) {
        Review review = reviewRepository.findById(reviewId).orElse(null);
        if (review == null) return;
        Seller seller = review.getProductid().getShopid().getSellerid();
        if (seller == null || !seller.getId().equals(sellerId)) return;
        ReviewReply reply = reviewReplyRepository.findByReview(review).orElse(new ReviewReply());
        reply.setReview(review);
        reply.setSeller(seller);
        reply.setReply(replyContent);
        reply.setCreatedAt(java.time.Instant.now());
        reviewReplyRepository.save(reply);
    }

    @Override
    public String getReplyContentByReviewId(Integer reviewId) {
        Review review = reviewRepository.findById(reviewId).orElse(null);
        if (review == null) return null;
        return reviewReplyRepository.findByReview(review).map(ReviewReply::getReply).orElse(null);
    }
} 