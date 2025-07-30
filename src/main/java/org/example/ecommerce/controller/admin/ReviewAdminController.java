package org.example.ecommerce.controller.admin;

import lombok.RequiredArgsConstructor;
import org.example.ecommerce.service.customer.complaint_service.ComplaintService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/reviews")
@RequiredArgsConstructor
public class ReviewAdminController {
    private final ComplaintService complaintService;


    @PatchMapping("/{id}/hide")
    public ResponseEntity<Void> hide(@PathVariable Integer id) {
        complaintService.hideReview(id);
        return ResponseEntity.noContent().build();
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        complaintService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
}
