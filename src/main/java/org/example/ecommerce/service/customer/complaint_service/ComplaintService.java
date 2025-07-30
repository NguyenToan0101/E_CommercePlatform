package org.example.ecommerce.service.customer.complaint_service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.ecommerce.common.dto.admin.complaintManagement.ComplaintListDTO;
import org.example.ecommerce.entity.Product;
import org.example.ecommerce.entity.Review;
import org.example.ecommerce.entity.conplaint.Complaint;
import org.example.ecommerce.repository.ComplaintRepository;
import org.example.ecommerce.repository.ProductRepository;
import org.example.ecommerce.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ComplaintService {
    private final ComplaintRepository complaintRepo;
    private final ReviewRepository reviewRepo;
    private final ProductRepository productRepo;


    public List<ComplaintListDTO> getAllComplaints() {
        return complaintRepo.findAllForList();
    }

    @Transactional
    public void changeStatus(Integer complaintId, String newStatus) {
        Complaint c = complaintRepo.findById(complaintId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy khiếu nại"));
        c.setStatus(newStatus);
        complaintRepo.save(c);
    }

    @Transactional
    public void hideReview(Integer reviewId) {
        Review r = reviewRepo.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy review"));
        r.setIsHidden(true);
        reviewRepo.save(r);
    }

    @Transactional
    public void deleteReview(Integer reviewId) {
        if (!reviewRepo.existsById(reviewId)) {
            throw new EntityNotFoundException("Không tìm thấy review");
        }
        reviewRepo.deleteById(reviewId);
    }

    @Transactional
    public void lockProduct(Integer productId) {
        Product p = productRepo.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy sản phẩm"));
        p.setStatus("locked");
        productRepo.save(p);
    }
}
