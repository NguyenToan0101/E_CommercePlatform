package org.example.ecommerce.service.customer.review;

import jakarta.transaction.Transactional;
import org.example.ecommerce.entity.Review;
import org.example.ecommerce.entity.ReviewsImage;
import org.example.ecommerce.repository.ReviewRepository;
import org.example.ecommerce.repository.ReviewsImageRepository;
import org.example.ecommerce.service.UploadImageFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class ReviewServiceImpl implements ReviewService{
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private ReviewsImageRepository reviewsImageRepository;
    @Autowired
    private UploadImageFile uploadImageFile;

    @Transactional
    public boolean submitReview(Review review, MultipartFile[] mediaFiles) {
        if (reviewRepository.existsByProductidAndOrderitemid(review.getProductid(), review.getOrderitemid())) {
            return false;
        }
        review.setCreatedat(LocalDateTime.now());
        reviewRepository.save(review);
        // Xử lý upload file
        if (mediaFiles != null && mediaFiles.length > 0) {
            for (MultipartFile file : mediaFiles) {
                if (!file.isEmpty()) {
                    try {
                        String url = uploadImageFile.uploadImage(file); // upload lên Cloudinary
                        ReviewsImage img = new ReviewsImage();
                        img.setReviewid(review);
                        img.setImageUrl(url);
                        reviewsImageRepository.save(img);
                    } catch (IOException e) {
                        return  false;
                    }
                }
            }
        }
        return true;
    }
}
