package org.example.ecommerce.controller.admin;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.example.ecommerce.common.dto.admin.complaintManagement.ComplaintDetailDTO;
import org.example.ecommerce.common.dto.admin.complaintManagement.ComplaintListDTO;
import org.example.ecommerce.repository.ComplaintRepository;
import org.example.ecommerce.service.customer.complaint_service.ComplaintService;
import org.example.ecommerce.service.admin.ComplaintDetailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/admin/complaints")
@RequiredArgsConstructor
public class ComplaintAdminController {
    private final ComplaintService complaintService;
    private final ComplaintRepository repo;
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
}
