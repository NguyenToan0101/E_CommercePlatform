package org.example.ecommerce.service.customer.review;

import org.example.ecommerce.entity.Review;
import org.springframework.web.multipart.MultipartFile;

public interface ReviewService {
    boolean submitReview(Review review, MultipartFile[] mediaFiles);
}
