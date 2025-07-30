package org.example.ecommerce.controller.admin;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.ecommerce.common.dto.admin.complaintManagement.ComplaintDetailDTO;
import org.example.ecommerce.common.dto.admin.complaintManagement.ComplaintListDTO;
import org.example.ecommerce.repository.ComplaintRepository;
import org.example.ecommerce.service.customer.complaint_service.ComplaintService;
import org.example.ecommerce.service.admin.ComplaintDetailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/admin/complaints")
@RequiredArgsConstructor
public class ComplaintAdminController {
    private final ComplaintService complaintService;
    private final ComplaintDetailService complaintDetailService;


    @GetMapping
    public ResponseEntity<List<ComplaintListDTO>> listComplaints() {
        List<ComplaintListDTO> dtos = complaintService.getAllComplaints();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ComplaintDetailDTO> detail(@PathVariable Integer id) {
        try {
            ComplaintDetailDTO dto = complaintDetailService.getComplaintDetail(id);
            return ResponseEntity.ok(dto);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(
            @PathVariable Integer id,
            @RequestBody Map<String,String> body
    ) {
        String newStatus = body.get("status");
        complaintService.changeStatus(id, newStatus);
        return ResponseEntity.noContent().build();
    }

    // Ẩn review
    @PatchMapping("/reviews/{reviewId}/hide")
    public ResponseEntity<Void> hideReview(@PathVariable Integer reviewId) {
        complaintService.hideReview(reviewId);
        return ResponseEntity.noContent().build();
    }

    // Xóa review
    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Integer reviewId) {
        complaintService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }
}
