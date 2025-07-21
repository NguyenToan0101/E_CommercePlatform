package org.example.ecommerce.service.admin;

import lombok.RequiredArgsConstructor;
import org.example.ecommerce.common.dto.admin.complaintManagement.*;
import org.example.ecommerce.entity.conplaint.Complaint;
import org.example.ecommerce.entity.Product;
import org.example.ecommerce.entity.Productimage;
import org.example.ecommerce.entity.Review;
import org.example.ecommerce.entity.Payment;
import org.example.ecommerce.entity.Order;
import org.example.ecommerce.repository.*;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ComplaintDetailService {
    private final ComplaintRepository complaintRepository;
    private final ProductRepository productRepository;
    private final ProductimageRepository productimageRepository;
    private final ReviewRepository reviewRepository;
    private final PaymentRepository paymentRepository;
    private final OrdersRepository ordersRepository;


    public ComplaintDetailDTO getComplaintDetail(Integer complaintId) {
        Complaint complaint = complaintRepository.findById(complaintId)
            .orElseThrow(() -> new EntityNotFoundException("Complaint not found"));

        ComplaintDetailDTO dto = new ComplaintDetailDTO();
        // Trường chung
        dto.setComplaintId(complaint.getComplaintId());
        dto.setCustomerId(complaint.getCustomer().getId());
        dto.setCustomerName(complaint.getCustomer().getFirstname() + " " + complaint.getCustomer().getLastname());
        dto.setFirstname(complaint.getCustomer().getFirstname());
        dto.setLastname(complaint.getCustomer().getLastname());
        dto.setEmail(complaint.getCustomer().getEmail());
        dto.setPhone(complaint.getCustomer().getPhone());
        dto.setReasonDescription(complaint.getReason().getDescription());
        dto.setDescription(complaint.getDescription());
        dto.setCreatedAt(complaint.getCreatedAt());
        dto.setUpdatedAt(complaint.getUpdatedAt());

        String category = complaint.getCategory().getName();
        switch (category.toLowerCase()) {
            case "product":
                Integer productId = complaint.getProduct().getProduct_id();
                Product product = productRepository.findById(productId)
                        .orElseThrow(() -> new EntityNotFoundException("Product not found"));
                List<String> productImages = productimageRepository.findAllByProductid(product)
                        .stream().map(Productimage::getImageurl).collect(Collectors.toList());
                ProductInfo productInfo = new ProductInfo(
                        product.getId(),
                        product.getShopid().getId(),
                        product.getShopid().getShopname(),
                        product.getCategoryid().getCategoryname(),
                        product.getName(),
                        product.getDescription(),
                        productImages
                );
                dto.setProduct(productInfo);
                break;
            case "review":
                if (complaint.getReview() != null) {
                    Integer reviewId = complaint.getReview().getReviewId();
                    Review review = reviewRepository.findById(reviewId).orElse(null);
                    if (review != null) {
                        String productName = null;
                        Integer productId1 = review.getProductid();
                        if (productId1 != null) {
                            Product product1 = productRepository.findById(productId1).orElse(null);
                            if (product1 != null) {
                                productName = product1.getName();
                            }
                        }
                        ReviewInfo reviewInfo = new ReviewInfo(
                                review.getId(),
                                review.getProductid(),
                                productName,
                                review.getRating(),
                                review.getComment(),
                                null // reviewImages nếu có
                        );
                        dto.setReview(reviewInfo);
                    }
                }
                break;
            case "payment":
                Payment payment = paymentRepository.findById(complaint.getPayment().getPaymentId())
                        .orElseThrow(() -> new EntityNotFoundException("Payment not found"));
                PaymentInfo paymentInfo = new PaymentInfo(
                        payment.getId(),
                        payment.getAmount(),
                        payment.getMethod(),
                        payment.getPaymentstatus(),
                        payment.getTransactionId(),
                        payment.getGateway(),
                        payment.getPaidat()
                );
                dto.setPayment(paymentInfo);
                // List<String> complaintImages = complaintImageRepository.findByComplaintId(complaintId)
                //         .stream().map(ComplaintImage::getUrl).collect(Collectors.toList());
                // dto.setComplaintImages(complaintImages);
                break;
            case "shipping":
                Order order = ordersRepository.findById(complaint.getOrderId())
                        .orElseThrow(() -> new EntityNotFoundException("Order not found"));
                ShippingInfo shippingInfo = new ShippingInfo(
                        order.getId(),
                        order.getOrderdate(),
                        order.getTotalamount(),
                        order.getStatus(),
                        order.getPaymentStatus(),
                        order.getAddress()
                );
                dto.setShipping(shippingInfo);
                // List<String> shippingImages = complaintImageRepository.findByComplaintId(complaintId)
                //         .stream().map(ComplaintImage::getUrl).collect(Collectors.toList());
                // dto.setComplaintImages(shippingImages);
                break;
        }
        return dto;
    }
}