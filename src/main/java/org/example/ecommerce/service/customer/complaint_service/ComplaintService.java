package org.example.ecommerce.service.customer.complaint_service;

import org.example.ecommerce.common.dto.admin.complaintManagement.ComplaintListDTO;
import org.example.ecommerce.repository.ComplaintRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComplaintService {
    private final ComplaintRepository complaintRepo;

    public ComplaintService(ComplaintRepository complaintRepo) {
        this.complaintRepo = complaintRepo;
    }


    public List<ComplaintListDTO> getAllComplaints() {
        return complaintRepo.findAllForList();
    }

}
