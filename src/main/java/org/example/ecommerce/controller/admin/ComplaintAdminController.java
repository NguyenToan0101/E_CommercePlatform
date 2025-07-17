package org.example.ecommerce.controller.admin;

import org.example.ecommerce.common.dto.admin.complaintManagement.ComplaintListDTO;
import org.example.ecommerce.service.customer.complaint_service.ComplaintService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/admin/complaints")
public class ComplaintAdminController {
    private final ComplaintService complaintService;

    public ComplaintAdminController(ComplaintService complaintService) {
        this.complaintService = complaintService;
    }

    @GetMapping
    public ResponseEntity<List<ComplaintListDTO>> listComplaints() {
        List<ComplaintListDTO> dtos = complaintService.getAllComplaints();
        return ResponseEntity.ok(dtos);
    }
}
